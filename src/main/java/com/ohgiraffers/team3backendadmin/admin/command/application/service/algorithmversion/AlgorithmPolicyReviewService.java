package com.ohgiraffers.team3backendadmin.admin.command.application.service.algorithmversion;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.algorithmversion.AlgorithmPolicyReviewRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.response.algorithmversion.AlgorithmPolicyReviewResponse;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class AlgorithmPolicyReviewService {

    private static final String VERTEX_AI_ENDPOINT =
        "https://%s-aiplatform.googleapis.com/v1/projects/%s/locations/%s/publishers/google/models/%s:generateContent";
    private static final String GLOBAL_VERTEX_AI_ENDPOINT =
        "https://aiplatform.googleapis.com/v1/projects/%s/locations/global/publishers/google/models/%s:generateContent";
    private static final String CLOUD_PLATFORM_SCOPE = "https://www.googleapis.com/auth/cloud-platform";

    private final ObjectMapper objectMapper;
    private final ResourceLoader resourceLoader;

    @Value("${google.ai.credentials-location:${nlp.google.credentials-location:}}")
    private String credentialsLocation;

    @Value("${google.ai.project-id:}")
    private String configuredProjectId;

    @Value("${google.ai.location:global}")
    private String googleAiLocation;

    @Value("${google.ai.model:gemini-3-flash-preview}")
    private String googleAiModel;

    public AlgorithmPolicyReviewResponse review(AlgorithmPolicyReviewRequest request) {
        String normalizedPolicyConfig = normalizeJson(request.getPolicyConfig());

        if (!StringUtils.hasText(credentialsLocation)) {
            return localFallbackReview(normalizedPolicyConfig, "GCP 자격증명 경로가 설정되어 있지 않습니다.");
        }

        try {
            GoogleCredentials credentials = loadCredentials();
            String projectId = resolveProjectId(credentials);
            AccessToken token = credentials.refreshAccessToken();
            String endpoint = buildVertexAiEndpoint(projectId);

            JsonNode response = RestClient.create()
                .post()
                .uri(endpoint)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getTokenValue())
                .contentType(MediaType.APPLICATION_JSON)
                .body(buildVertexAiRequest(normalizedPolicyConfig))
                .retrieve()
                .body(JsonNode.class);

            return AlgorithmPolicyReviewResponse.builder()
                .provider("google-vertex-ai")
                .model(googleAiModel)
                .aiEnabled(true)
                .review(extractReviewText(response))
                .reviewedAt(LocalDateTime.now())
                .build();
        } catch (Exception exception) {
            return AlgorithmPolicyReviewResponse.builder()
                .provider("google-vertex-ai")
                .model(googleAiModel)
                .aiEnabled(false)
                .review(buildFailureReview(exception, normalizedPolicyConfig))
                .reviewedAt(LocalDateTime.now())
                .build();
        }
    }

    private GoogleCredentials loadCredentials() throws Exception {
        Resource resource = resolveResource(credentialsLocation);
        try (InputStream inputStream = resource.getInputStream()) {
            return GoogleCredentials.fromStream(inputStream)
                .createScoped(List.of(CLOUD_PLATFORM_SCOPE));
        }
    }

    private String resolveProjectId(GoogleCredentials credentials) {
        if (StringUtils.hasText(configuredProjectId)) {
            return configuredProjectId;
        }
        if (credentials instanceof ServiceAccountCredentials serviceAccountCredentials
            && StringUtils.hasText(serviceAccountCredentials.getProjectId())) {
            return serviceAccountCredentials.getProjectId();
        }
        throw new IllegalStateException("Google AI project id가 설정되어 있지 않고 gcp-key.json에서도 확인할 수 없습니다.");
    }

    private String buildVertexAiEndpoint(String projectId) {
        if ("global".equalsIgnoreCase(googleAiLocation)) {
            return GLOBAL_VERTEX_AI_ENDPOINT.formatted(projectId, googleAiModel);
        }
        return VERTEX_AI_ENDPOINT.formatted(
            googleAiLocation,
            projectId,
            googleAiLocation,
            googleAiModel
        );
    }

    private Resource resolveResource(String location) {
        if (location.startsWith("classpath:") || location.startsWith("file:")) {
            return resourceLoader.getResource(location);
        }
        return resourceLoader.getResource("file:" + location);
    }

    private Map<String, Object> buildVertexAiRequest(String policyConfig) {
        return Map.of(
            "contents",
            List.of(Map.of(
                "role", "user",
                "parts", List.of(Map.of("text", buildPrompt(policyConfig)))
            )),
            "generationConfig",
            Map.of(
                "temperature", 0.2,
                "topP", 0.8,
                "maxOutputTokens", 4096
            )
        );
    }

    private String buildPrompt(String policyConfig) {
        return """
            당신은 반도체 생산 프로젝트의 HR/제조 배치 점수 정책을 검토하는 한국어 리뷰어입니다.
            입력되는 policy_config는 JSON이며, 정량 평가와 정성 평가에 사용하는 계산 상수들을 포함합니다.

            반드시 지켜야 할 응답 규칙:
            1. 모든 답변은 한국어로만 작성합니다. 영어 섹션명도 사용하지 말고 한국어 섹션명을 사용합니다.
            2. 외부 기준값을 사실처럼 단정하지 말고, 제공된 정책값과 공학적 가정에 기반해 판단합니다.
            3. 설비별 등록값(보증 기간, 설계 수명, 마모 계수, 기준 성능률, 기준 오차율)은 이 JSON에 직접 저장되는 값이 아닙니다.
               equipment 그룹은 설비별 값을 배치 계산에 반영할 때 사용하는 공통 보정 상수라는 전제로 검토합니다.
            4. 위험하거나 의심스러운 값이 있을 때만 구체적인 대체값을 제안합니다.
            5. 다음 Markdown 섹션을 사용합니다: `## 요약`, `## 위험 요소`, `## 세부 검토`, `## 권장 수정안`.

            검토 관점:
            1. 정량 평가의 생산량/수율/리드타임 가중치 합과 균형이 적절한지 확인합니다.
            2. 설비 노후화, E_idx, 환경/자재 영향, 챌린지 보너스 값이 내부적으로 일관적인지 확인합니다.
            3. 정성 평가의 청크 가중치, 키워드 상한, 문맥 가중치, 정규화 범위, 보조 조정, 등급 기준이 과도하거나 약하지 않은지 확인합니다.
            4. 점수 변동이 너무 약하거나 강해질 수 있는 설정을 우선적으로 짚어줍니다.

            policy_config:
            %s
            """.formatted(policyConfig);
    }

    private String extractReviewText(JsonNode response) {
        if (response == null) {
            return "Google AI 응답에 검토 본문이 포함되어 있지 않습니다.";
        }

        JsonNode parts = response.at("/candidates/0/content/parts");
        StringBuilder reviewText = new StringBuilder();
        if (parts.isArray()) {
            for (JsonNode part : parts) {
                String text = part.path("text").asText("");
                if (!text.isBlank()) {
                    reviewText.append(text).append(System.lineSeparator());
                }
            }
        }

        String text = reviewText.toString().trim();
        if (text.isBlank()) {
            text = response.at("/candidates/0/content/parts/0/text").asText("").trim();
        }

        if (text.isBlank()) {
            return "Google AI 응답에 검토 본문이 포함되어 있지 않습니다.";
        }

        String finishReason = response.at("/candidates/0/finishReason").asText("");
        if ("MAX_TOKENS".equalsIgnoreCase(finishReason)) {
            return text + System.lineSeparator() + System.lineSeparator()
                + "> 참고: 모델 출력 토큰 상한에 도달해 응답 일부가 생략되었을 수 있습니다. 정책 검토가 계속 짧게 끝나면 maxOutputTokens를 더 늘리거나 검토 항목을 줄여야 합니다.";
        }
        return text;
    }

    private String normalizeJson(String policyConfig) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(objectMapper.readTree(policyConfig));
        } catch (Exception ignored) {
            return policyConfig;
        }
    }

    private AlgorithmPolicyReviewResponse localFallbackReview(String policyConfig, String reason) {
        return AlgorithmPolicyReviewResponse.builder()
            .provider("local-fallback")
            .model("policy-rule-check")
            .aiEnabled(false)
            .review(buildMissingCredentialsReview(reason, policyConfig))
            .reviewedAt(LocalDateTime.now())
            .build();
    }

    private String buildMissingCredentialsReview(String reason, String policyConfig) {
        return """
            ## 요약
            GCP 자격증명 경로가 설정되어 있지 않아 Google Vertex AI 검토를 실행하지 못했습니다.

            ## 사유
            %s

            ## 필요한 설정
            batch와 같은 방식으로 gcp-key.json 경로를 전달해야 합니다.
            - 로컬: `NLP_GOOGLE_CREDENTIALS_LOCATION=classpath:gcp-key.json` 또는 `file:/path/to/gcp-key.json`
            - Docker: `NLP_GOOGLE_CREDENTIALS_LOCATION=file:/run/secrets/gcp_key_json`

            ## 로컬 점검 포인트
            - 정량 평가의 생산량/수율/리드타임 가중치 합은 보통 1.0에 가깝게 맞추는 편이 안전합니다.
            - 설비 노후화 정책은 설계 수명에 가까운 설비는 눈에 띄게 하락하되, 초기 설비가 급격히 무너지지 않도록 조정해야 합니다.
            - 정성 평가의 키워드 합산 상한이 문맥 점수를 압도하지 않는지 확인해야 합니다.
            - 정성 등급 기준은 0~100 정규화 점수 스케일과 맞아야 합니다.

            ## policy_config 스냅샷
            ```json
            %s
            ```
            """.formatted(reason, policyConfig);
    }

    private String buildFailureReview(Exception exception, String policyConfig) {
        return """
            ## 요약
            Google Vertex AI 정책 검토 요청이 실패했습니다.

            ## 오류
            `%s: %s`

            ## 확인할 항목
            - `NLP_GOOGLE_CREDENTIALS_LOCATION`와 `GOOGLE_AI_PROJECT_ID` 설정을 확인하세요.
            - 서비스 계정에 Vertex AI Gemini 모델 호출 권한이 있는지 확인하세요.
            - admin 서비스 컨테이너에서 외부 네트워크 접근이 가능한지 확인하세요.

            ## policy_config 스냅샷
            ```json
            %s
            ```
            """.formatted(
            exception.getClass().getSimpleName(),
            exception.getMessage() == null ? "오류 메시지 없음" : exception.getMessage(),
            policyConfig
        );
    }
}

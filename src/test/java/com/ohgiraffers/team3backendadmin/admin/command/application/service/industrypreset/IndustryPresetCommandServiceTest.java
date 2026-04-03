package com.ohgiraffers.team3backendadmin.admin.command.application.service.industrypreset;

import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.IndustryPresetCreateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.IndustryPresetDeleteRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.IndustryPresetUpdateRequest;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.ocsaweightconfig.OCSAWeightConfig;
import com.ohgiraffers.team3backendadmin.admin.command.domain.repository.OCSAWeightConfigRepository;
import com.ohgiraffers.team3backendadmin.common.exception.DuplicateFieldException;
import com.ohgiraffers.team3backendadmin.common.exception.InvalidInputException;
import com.ohgiraffers.team3backendadmin.common.exception.OCSAWeightConfigNotFoundException;
import com.ohgiraffers.team3backendadmin.common.idgenerator.IdGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class IndustryPresetCommandServiceTest {

    @InjectMocks
    private IndustryPresetCommandService industryPresetCommandService;

    @Mock
    private OCSAWeightConfigRepository ocsaWeightConfigRepository;

    @Mock
    private IdGenerator idGenerator;

    @Nested
    @DisplayName("create 메서드")
    class Create {

        @Test
        @DisplayName("유효한 요청이면 OCSA 가중치 설정이 정상 저장된다")
        void createSuccess() {
            // given
            IndustryPresetCreateRequest request = new IndustryPresetCreateRequest(
                    "SEMICONDUCTOR",
                    new BigDecimal("0.25"),
                    new BigDecimal("0.25"),
                    new BigDecimal("0.25"),
                    new BigDecimal("0.25"),
                    new BigDecimal("0.3"),
                    LocalDate.of(2026, 4, 1)
            );

            given(ocsaWeightConfigRepository.existsByIndustryPresetName("SEMICONDUCTOR")).willReturn(false);
            given(idGenerator.generate()).willReturn(1000L);

            // when
            industryPresetCommandService.create(request);

            // then
            ArgumentCaptor<OCSAWeightConfig> captor = ArgumentCaptor.forClass(OCSAWeightConfig.class);
            verify(ocsaWeightConfigRepository).save(captor.capture());

            OCSAWeightConfig saved = captor.getValue();
            assertEquals(1000L, saved.getConfigId());
            assertEquals("SEMICONDUCTOR", saved.getIndustryPresetName());
            assertEquals(new BigDecimal("0.25"), saved.getWeightV1());
            assertEquals(new BigDecimal("0.25"), saved.getWeightV2());
            assertEquals(new BigDecimal("0.25"), saved.getWeightV3());
            assertEquals(new BigDecimal("0.25"), saved.getWeightV4());
            assertEquals(new BigDecimal("0.3"), saved.getAlphaWeight());
            assertEquals(LocalDate.of(2026, 4, 1), saved.getEffectiveDate());
        }

        @Test
        @DisplayName("알파 가중치가 0.0이면 정상 저장된다")
        void createWithAlphaZero() {
            // given
            IndustryPresetCreateRequest request = new IndustryPresetCreateRequest(
                    "DISPLAY",
                    new BigDecimal("0.40"),
                    new BigDecimal("0.30"),
                    new BigDecimal("0.20"),
                    new BigDecimal("0.10"),
                    new BigDecimal("0.0"),
                    LocalDate.of(2026, 5, 1)
            );

            given(ocsaWeightConfigRepository.existsByIndustryPresetName("DISPLAY")).willReturn(false);
            given(idGenerator.generate()).willReturn(2000L);

            // when
            industryPresetCommandService.create(request);

            // then
            verify(ocsaWeightConfigRepository).save(any(OCSAWeightConfig.class));
        }

        @Test
        @DisplayName("알파 가중치가 0.5이면 정상 저장된다")
        void createWithAlphaMax() {
            // given
            IndustryPresetCreateRequest request = new IndustryPresetCreateRequest(
                    "BATTERY",
                    new BigDecimal("0.10"),
                    new BigDecimal("0.20"),
                    new BigDecimal("0.30"),
                    new BigDecimal("0.40"),
                    new BigDecimal("0.5"),
                    LocalDate.of(2026, 6, 1)
            );

            given(ocsaWeightConfigRepository.existsByIndustryPresetName("BATTERY")).willReturn(false);
            given(idGenerator.generate()).willReturn(3000L);

            // when
            industryPresetCommandService.create(request);

            // then
            verify(ocsaWeightConfigRepository).save(any(OCSAWeightConfig.class));
        }

        @Test
        @DisplayName("이미 존재하는 프리셋 이름이면 예외가 발생한다")
        void createFailDuplicateName() {
            // given
            IndustryPresetCreateRequest request = new IndustryPresetCreateRequest(
                    "SEMICONDUCTOR",
                    new BigDecimal("0.25"),
                    new BigDecimal("0.25"),
                    new BigDecimal("0.25"),
                    new BigDecimal("0.25"),
                    new BigDecimal("0.3"),
                    LocalDate.of(2026, 4, 1)
            );

            given(ocsaWeightConfigRepository.existsByIndustryPresetName("SEMICONDUCTOR")).willReturn(true);

            // when & then
            DuplicateFieldException exception = assertThrows(
                    DuplicateFieldException.class,
                    () -> industryPresetCommandService.create(request)
            );
            assertEquals("이미 사용 중인 프리셋 이름입니다.", exception.getMessage());
        }

        @Test
        @DisplayName("가중치 합이 1.00이 아니면 예외가 발생한다")
        void createFailWeightSumNotOne() {
            // given
            IndustryPresetCreateRequest request = new IndustryPresetCreateRequest(
                    "SEMICONDUCTOR",
                    new BigDecimal("0.30"),
                    new BigDecimal("0.30"),
                    new BigDecimal("0.30"),
                    new BigDecimal("0.30"),
                    new BigDecimal("0.3"),
                    LocalDate.of(2026, 4, 1)
            );

            given(ocsaWeightConfigRepository.existsByIndustryPresetName("SEMICONDUCTOR")).willReturn(false);

            // when & then
            InvalidInputException exception = assertThrows(
                    InvalidInputException.class,
                    () -> industryPresetCommandService.create(request)
            );
            assertEquals("가중치(v1~v4)의 합은 1.00이어야 합니다.", exception.getMessage());
        }

        @Test
        @DisplayName("알파 가중치가 0.5를 초과하면 예외가 발생한다")
        void createFailAlphaWeightTooHigh() {
            // given
            IndustryPresetCreateRequest request = new IndustryPresetCreateRequest(
                    "VEHICLE",
                    new BigDecimal("0.25"),
                    new BigDecimal("0.25"),
                    new BigDecimal("0.25"),
                    new BigDecimal("0.25"),
                    new BigDecimal("0.6"),
                    LocalDate.of(2026, 4, 1)
            );

            given(ocsaWeightConfigRepository.existsByIndustryPresetName("VEHICLE")).willReturn(false);

            // when & then
            InvalidInputException exception = assertThrows(
                    InvalidInputException.class,
                    () -> industryPresetCommandService.create(request)
            );
            assertEquals("알파 가중치는 0.0~0.5 범위여야 합니다.", exception.getMessage());
        }

        @Test
        @DisplayName("알파 가중치가 음수이면 예외가 발생한다")
        void createFailAlphaWeightNegative() {
            // given
            IndustryPresetCreateRequest request = new IndustryPresetCreateRequest(
                    "CUSTOM",
                    new BigDecimal("0.25"),
                    new BigDecimal("0.25"),
                    new BigDecimal("0.25"),
                    new BigDecimal("0.25"),
                    new BigDecimal("-0.1"),
                    LocalDate.of(2026, 4, 1)
            );

            given(ocsaWeightConfigRepository.existsByIndustryPresetName("CUSTOM")).willReturn(false);

            // when & then
            InvalidInputException exception = assertThrows(
                    InvalidInputException.class,
                    () -> industryPresetCommandService.create(request)
            );
            assertEquals("알파 가중치는 0.0~0.5 범위여야 합니다.", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("update 메서드")
    class Update {

        private OCSAWeightConfig createExisting() {
            return OCSAWeightConfig.builder()
                    .configId(1000L)
                    .industryPresetName("SEMICONDUCTOR")
                    .weightV1(new BigDecimal("0.25"))
                    .weightV2(new BigDecimal("0.25"))
                    .weightV3(new BigDecimal("0.25"))
                    .weightV4(new BigDecimal("0.25"))
                    .alphaWeight(new BigDecimal("0.3"))
                    .effectiveDate(LocalDate.of(2026, 4, 1))
                    .build();
        }

        @Test
        @DisplayName("모든 필드가 제공되면 전체 수정된다")
        void updateAllFieldsSuccess() {
            // given
            OCSAWeightConfig existing = createExisting();

            IndustryPresetUpdateRequest request = new IndustryPresetUpdateRequest(
                    1000L,
                    "DISPLAY",
                    new BigDecimal("0.40"),
                    new BigDecimal("0.30"),
                    new BigDecimal("0.20"),
                    new BigDecimal("0.10"),
                    new BigDecimal("0.2"),
                    LocalDate.of(2026, 5, 1)
            );

            given(ocsaWeightConfigRepository.findById(1000L)).willReturn(Optional.of(existing));
            given(ocsaWeightConfigRepository.existsByIndustryPresetNameAndConfigIdNot("DISPLAY", 1000L))
                    .willReturn(false);

            // when
            industryPresetCommandService.update(request);

            // then
            assertEquals("DISPLAY", existing.getIndustryPresetName());
            assertEquals(new BigDecimal("0.40"), existing.getWeightV1());
            assertEquals(new BigDecimal("0.30"), existing.getWeightV2());
            assertEquals(new BigDecimal("0.20"), existing.getWeightV3());
            assertEquals(new BigDecimal("0.10"), existing.getWeightV4());
            assertEquals(new BigDecimal("0.2"), existing.getAlphaWeight());
            assertEquals(LocalDate.of(2026, 5, 1), existing.getEffectiveDate());
        }

        @Test
        @DisplayName("일부 필드만 제공되면 해당 필드만 수정된다")
        void updatePartialFieldsSuccess() {
            // given
            OCSAWeightConfig existing = createExisting();

            IndustryPresetUpdateRequest request = new IndustryPresetUpdateRequest(
                    1000L,
                    "DISPLAY",
                    null, null, null, null, null,
                    LocalDate.of(2026, 6, 1)
            );

            given(ocsaWeightConfigRepository.findById(1000L)).willReturn(Optional.of(existing));
            given(ocsaWeightConfigRepository.existsByIndustryPresetNameAndConfigIdNot("DISPLAY", 1000L))
                    .willReturn(false);

            // when
            industryPresetCommandService.update(request);

            // then
            assertEquals("DISPLAY", existing.getIndustryPresetName());
            assertEquals(new BigDecimal("0.25"), existing.getWeightV1());
            assertEquals(new BigDecimal("0.25"), existing.getWeightV2());
            assertEquals(new BigDecimal("0.25"), existing.getWeightV3());
            assertEquals(new BigDecimal("0.25"), existing.getWeightV4());
            assertEquals(new BigDecimal("0.3"), existing.getAlphaWeight());
            assertEquals(LocalDate.of(2026, 6, 1), existing.getEffectiveDate());
        }

        @Test
        @DisplayName("이름이 null이면 중복 검사를 건너뛰고 이름이 유지된다")
        void updateWithNullNameSkipsDuplicateCheck() {
            // given
            OCSAWeightConfig existing = createExisting();

            IndustryPresetUpdateRequest request = new IndustryPresetUpdateRequest(
                    1000L,
                    null,
                    null, null, null, null,
                    new BigDecimal("0.1"),
                    null
            );

            given(ocsaWeightConfigRepository.findById(1000L)).willReturn(Optional.of(existing));

            // when
            industryPresetCommandService.update(request);

            // then
            assertEquals("SEMICONDUCTOR", existing.getIndustryPresetName());
            assertEquals(new BigDecimal("0.1"), existing.getAlphaWeight());
        }

        @Test
        @DisplayName("존재하지 않는 설정 ID이면 예외가 발생한다")
        void updateFailNotFound() {
            // given
            IndustryPresetUpdateRequest request = new IndustryPresetUpdateRequest(
                    9999L, null, null, null, null, null, null, null
            );

            given(ocsaWeightConfigRepository.findById(9999L)).willReturn(Optional.empty());

            // when & then
            OCSAWeightConfigNotFoundException exception = assertThrows(
                    OCSAWeightConfigNotFoundException.class,
                    () -> industryPresetCommandService.update(request)
            );
            assertEquals("해당 OCSA 가중치 설정을 찾을 수 없습니다.", exception.getMessage());
        }

        @Test
        @DisplayName("다른 설정에서 이미 사용 중인 프리셋 이름이면 예외가 발생한다")
        void updateFailDuplicateName() {
            // given
            OCSAWeightConfig existing = createExisting();

            IndustryPresetUpdateRequest request = new IndustryPresetUpdateRequest(
                    1000L, "DISPLAY", null, null, null, null, null, null
            );

            given(ocsaWeightConfigRepository.findById(1000L)).willReturn(Optional.of(existing));
            given(ocsaWeightConfigRepository.existsByIndustryPresetNameAndConfigIdNot("DISPLAY", 1000L))
                    .willReturn(true);

            // when & then
            DuplicateFieldException exception = assertThrows(
                    DuplicateFieldException.class,
                    () -> industryPresetCommandService.update(request)
            );
            assertEquals("이미 사용 중인 프리셋 이름입니다.", exception.getMessage());
        }

        @Test
        @DisplayName("병합된 가중치 합이 1.00이 아니면 예외가 발생한다")
        void updateFailWeightSumNotOne() {
            // given
            OCSAWeightConfig existing = createExisting();

            IndustryPresetUpdateRequest request = new IndustryPresetUpdateRequest(
                    1000L, null,
                    new BigDecimal("0.50"), null, null, null, null, null
            );

            given(ocsaWeightConfigRepository.findById(1000L)).willReturn(Optional.of(existing));

            // when & then
            InvalidInputException exception = assertThrows(
                    InvalidInputException.class,
                    () -> industryPresetCommandService.update(request)
            );
            assertEquals("가중치(v1~v4)의 합은 1.00이어야 합니다.", exception.getMessage());
        }

        @Test
        @DisplayName("병합된 알파 가중치가 0.5를 초과하면 예외가 발생한다")
        void updateFailAlphaWeightTooHigh() {
            // given
            OCSAWeightConfig existing = createExisting();

            IndustryPresetUpdateRequest request = new IndustryPresetUpdateRequest(
                    1000L, null, null, null, null, null,
                    new BigDecimal("0.6"), null
            );

            given(ocsaWeightConfigRepository.findById(1000L)).willReturn(Optional.of(existing));

            // when & then
            InvalidInputException exception = assertThrows(
                    InvalidInputException.class,
                    () -> industryPresetCommandService.update(request)
            );
            assertEquals("알파 가중치는 0.0~0.5 범위여야 합니다.", exception.getMessage());
        }

        @Test
        @DisplayName("병합된 알파 가중치가 음수이면 예외가 발생한다")
        void updateFailAlphaWeightNegative() {
            // given
            OCSAWeightConfig existing = createExisting();

            IndustryPresetUpdateRequest request = new IndustryPresetUpdateRequest(
                    1000L, null, null, null, null, null,
                    new BigDecimal("-0.1"), null
            );

            given(ocsaWeightConfigRepository.findById(1000L)).willReturn(Optional.of(existing));

            // when & then
            InvalidInputException exception = assertThrows(
                    InvalidInputException.class,
                    () -> industryPresetCommandService.update(request)
            );
            assertEquals("알파 가중치는 0.0~0.5 범위여야 합니다.", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("delete 메서드")
    class Delete {

        @Test
        @DisplayName("존재하는 설정이면 프리셋 이름이 DELETED로 변경된다")
        void deleteSuccess() {
            // given
            OCSAWeightConfig existing = OCSAWeightConfig.builder()
                    .configId(1000L)
                    .industryPresetName("SEMICONDUCTOR")
                    .weightV1(new BigDecimal("0.25"))
                    .weightV2(new BigDecimal("0.25"))
                    .weightV3(new BigDecimal("0.25"))
                    .weightV4(new BigDecimal("0.25"))
                    .alphaWeight(new BigDecimal("0.3"))
                    .effectiveDate(LocalDate.of(2026, 4, 1))
                    .build();

            IndustryPresetDeleteRequest request = new IndustryPresetDeleteRequest(
                    1000L, "SEMICONDUCTOR"
            );

            given(ocsaWeightConfigRepository.findById(1000L)).willReturn(Optional.of(existing));

            // when
            industryPresetCommandService.delete(request);

            // then
            assertEquals("DELETED", existing.getIndustryPresetName());
        }

        @Test
        @DisplayName("존재하지 않는 설정 ID이면 예외가 발생한다")
        void deleteFailNotFound() {
            // given
            IndustryPresetDeleteRequest request = new IndustryPresetDeleteRequest(
                    9999L, "SEMICONDUCTOR"
            );

            given(ocsaWeightConfigRepository.findById(9999L)).willReturn(Optional.empty());

            // when & then
            OCSAWeightConfigNotFoundException exception = assertThrows(
                    OCSAWeightConfigNotFoundException.class,
                    () -> industryPresetCommandService.delete(request)
            );
            assertEquals("해당 OCSA 가중치 설정을 찾을 수 없습니다.", exception.getMessage());
        }
    }
}

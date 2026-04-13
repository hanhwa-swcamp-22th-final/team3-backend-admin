package com.ohgiraffers.team3backendadmin.admin.query.service.org;

import com.ohgiraffers.team3backendadmin.admin.query.dto.response.DepartmentResponse;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.org.OrgDepartmentDetailDto;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.org.OrgEmployeeItem;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.org.OrgTeamMembersDto;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.org.OrgUnitDto;
import com.ohgiraffers.team3backendadmin.admin.query.mapper.DepartmentMapper;
import com.ohgiraffers.team3backendadmin.admin.query.mapper.OrgMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminOrgQueryService {

    private final DepartmentMapper departmentMapper;
    private final OrgMapper orgMapper;

    /** HR-073: 조직도 트리 */
    public OrgUnitDto getOrgTree() {
        List<DepartmentResponse> all = departmentMapper.findAll();

        // 삭제된 부서 제외
        List<DepartmentResponse> active = all.stream()
                .filter(d -> d.getDepth() != null && !"삭제됨".equals(d.getDepartmentName()))
                .toList();

        // 부모별 자식 그룹화
        Map<Long, List<DepartmentResponse>> byParent = active.stream()
                .filter(d -> d.getParentDepartmentId() != null)
                .collect(Collectors.groupingBy(DepartmentResponse::getParentDepartmentId));

        // 루트 부서 목록 (parentDepartmentId == null)
        List<OrgUnitDto> roots = active.stream()
                .filter(d -> d.getParentDepartmentId() == null)
                .map(d -> buildDeptNode(d, byParent))
                .toList();

        // 단일 루트 래퍼 반환 (HR 클라이언트가 단일 OrgUnitTreeResponse 기대)
        return OrgUnitDto.builder()
                .unitId(0L)
                .unitName("전체 조직")
                .type("ROOT")
                .children(roots)
                .build();
    }

    private OrgUnitDto buildDeptNode(DepartmentResponse dept, Map<Long, List<DepartmentResponse>> byParent) {
        List<DepartmentResponse> childDepts = byParent.getOrDefault(dept.getDepartmentId(), List.of());
        List<OrgUnitDto> children = childDepts.stream()
                .map(child -> OrgUnitDto.builder()
                        .unitId(child.getDepartmentId())
                        .unitName(child.getTeamName() != null ? child.getTeamName() : child.getDepartmentName())
                        .type("TEAM")
                        .children(List.of())
                        .build())
                .toList();

        return OrgUnitDto.builder()
                .unitId(dept.getDepartmentId())
                .unitName(dept.getDepartmentName())
                .type("DEPARTMENT")
                .children(children)
                .build();
    }

    /** HR-074: 직원 목록 (필터) */
    public List<OrgEmployeeItem> getEmployees(Long departmentId, Long teamId, String keyword, int page, int size) {
        int offset = page * size;
        return orgMapper.findOrgEmployees(departmentId, teamId, keyword, offset, size);
    }

    /** HR-075: 팀원 목록 및 팀장 정보 */
    public OrgTeamMembersDto getTeamMembers(Long teamId) {
        List<OrgEmployeeItem> all = orgMapper.findEmployeesByTeamId(teamId);

        OrgEmployeeItem leader = all.stream()
                .filter(e -> "TL".equals(e.getRole()))
                .findFirst()
                .orElse(null);

        List<OrgEmployeeItem> members = all.stream()
                .filter(e -> !"TL".equals(e.getRole()))
                .collect(Collectors.toList());

        return OrgTeamMembersDto.builder()
                .leaderInfo(leader)
                .members(members)
                .build();
    }

    /** HR-082: 부서 상세 조회 */
    public OrgDepartmentDetailDto getDepartmentDetail(Long departmentId) {
        DepartmentResponse dept = departmentMapper.findById(departmentId);
        List<DepartmentResponse> all = departmentMapper.findAll();

        List<DepartmentResponse> teams = all.stream()
                .filter(d -> departmentId.equals(d.getParentDepartmentId())
                        && d.getDepth() != null
                        && !"삭제됨".equals(d.getDepartmentName()))
                .toList();

        List<OrgDepartmentDetailDto.TeamSummary> teamSummaries = new ArrayList<>();
        int totalMembers = 0;
        for (DepartmentResponse team : teams) {
            int count = orgMapper.findEmployeesByTeamId(team.getDepartmentId()).size();
            totalMembers += count;
            String teamName = team.getTeamName() != null ? team.getTeamName() : team.getDepartmentName();
            teamSummaries.add(OrgDepartmentDetailDto.TeamSummary.builder()
                    .teamId(team.getDepartmentId())
                    .teamName(teamName)
                    .memberCount(count)
                    .build());
        }

        String deptName = dept != null ? dept.getDepartmentName() : "";
        return OrgDepartmentDetailDto.builder()
                .departmentId(departmentId)
                .departmentName(deptName)
                .teamCount(teams.size())
                .totalMembers(totalMembers)
                .teams(teamSummaries)
                .build();
    }
}

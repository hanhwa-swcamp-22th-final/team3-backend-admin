package com.ohgiraffers.team3backendadmin.admin.command.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.org.OrgDepartmentRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.org.OrgTeamMemberRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.dto.request.org.OrgTeamRequest;
import com.ohgiraffers.team3backendadmin.admin.command.application.service.org.AdminOrgCommandService;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.org.OrgDepartmentDetailDto;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.org.OrgEmployeeItem;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.org.OrgTeamMembersDto;
import com.ohgiraffers.team3backendadmin.admin.query.dto.response.org.OrgUnitDto;
import com.ohgiraffers.team3backendadmin.admin.query.service.org.AdminOrgQueryService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminOrgController.class)
@AutoConfigureMockMvc(addFilters = false)
class AdminOrgControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AdminOrgQueryService orgQueryService;

    @MockitoBean
    private AdminOrgCommandService orgCommandService;

    @Test
    @DisplayName("조직도 트리 조회 API")
    void getOrgTree_success() throws Exception {
        OrgUnitDto tree = OrgUnitDto.builder()
            .unitId(0L)
            .unitName("전체 조직")
            .type("ROOT")
            .children(List.of())
            .build();
        when(orgQueryService.getOrgTree()).thenReturn(tree);

        mockMvc.perform(get("/api/v1/admin/org/units"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.unitId").value(0L))
            .andExpect(jsonPath("$.data.unitName").value("전체 조직"));
    }

    @Test
    @DisplayName("조직 직원 목록 조회 API")
    void getEmployees_success() throws Exception {
        OrgEmployeeItem employee = new OrgEmployeeItem();
        employee.setEmployeeId(10L);
        employee.setName("홍길동");
        employee.setRole("WORKER");
        employee.setCurrentTier("A");

        when(orgQueryService.getEmployees(1L, 2L, "홍", 0, 20)).thenReturn(List.of(employee));

        mockMvc.perform(get("/api/v1/admin/org/employees")
                .param("departmentId", "1")
                .param("teamId", "2")
                .param("keyword", "홍")
                .param("page", "0")
                .param("size", "20"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data[0].employeeId").value(10L))
            .andExpect(jsonPath("$.data[0].name").value("홍길동"));
    }

    @Test
    @DisplayName("팀원 목록 조회 API")
    void getTeamMembers_success() throws Exception {
        OrgEmployeeItem leader = new OrgEmployeeItem();
        leader.setEmployeeId(20L);
        leader.setName("팀장");

        OrgTeamMembersDto response = OrgTeamMembersDto.builder()
            .leaderInfo(leader)
            .members(List.of())
            .build();
        when(orgQueryService.getTeamMembers(2L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/admin/org/teams/2/members"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.leaderInfo.employeeId").value(20L));
    }

    @Test
    @DisplayName("부서 상세 조회 API")
    void getDepartmentDetail_success() throws Exception {
        OrgDepartmentDetailDto detail = OrgDepartmentDetailDto.builder()
            .departmentId(1L)
            .departmentName("생산본부")
            .teamCount(1)
            .totalMembers(3)
            .teams(List.of())
            .build();
        when(orgQueryService.getDepartmentDetail(1L)).thenReturn(detail);

        mockMvc.perform(get("/api/v1/admin/org/departments/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.departmentId").value(1L))
            .andExpect(jsonPath("$.data.departmentName").value("생산본부"));
    }

    @Test
    @DisplayName("부서 생성 API")
    void createDepartment_success() throws Exception {
        OrgDepartmentRequest request = new OrgDepartmentRequest();
        request.setDepartmentName("생산본부");
        request.setDepth("DEPARTMENT");
        when(orgCommandService.createDepartment(any(OrgDepartmentRequest.class))).thenReturn(100L);

        mockMvc.perform(post("/api/v1/admin/org/departments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(100L));
    }

    @Test
    @DisplayName("부서 수정 API")
    void updateDepartment_success() throws Exception {
        OrgDepartmentRequest request = new OrgDepartmentRequest();
        request.setDepartmentName("생산본부 수정");
        when(orgCommandService.updateDepartment(eq(100L), any(OrgDepartmentRequest.class))).thenReturn(100L);

        mockMvc.perform(put("/api/v1/admin/org/departments/100")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(100L));
    }

    @Test
    @DisplayName("팀 생성 API")
    void createTeam_success() throws Exception {
        OrgTeamRequest request = new OrgTeamRequest();
        request.setTeamName("1팀");
        request.setLeaderId(10L);
        when(orgCommandService.createTeam(eq(100L), any(OrgTeamRequest.class))).thenReturn(200L);

        mockMvc.perform(post("/api/v1/admin/org/departments/100/teams")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(200L));
    }

    @Test
    @DisplayName("팀 수정 API")
    void updateTeam_success() throws Exception {
        OrgTeamRequest request = new OrgTeamRequest();
        request.setTeamName("1팀 수정");

        mockMvc.perform(put("/api/v1/admin/org/teams/200")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isNoContent());

        verify(orgCommandService).updateTeam(eq(200L), any(OrgTeamRequest.class));
    }

    @Test
    @DisplayName("팀원 추가 API")
    void addTeamMembers_success() throws Exception {
        OrgTeamMemberRequest request = new OrgTeamMemberRequest();
        request.setEmployeeIds(List.of(10L, 11L));

        mockMvc.perform(post("/api/v1/admin/org/teams/200/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isNoContent());

        verify(orgCommandService).addTeamMembers(eq(200L), any(OrgTeamMemberRequest.class));
    }

    @Test
    @DisplayName("팀원 제거 API")
    void removeTeamMember_success() throws Exception {
        mockMvc.perform(delete("/api/v1/admin/org/teams/200/members/10"))
            .andExpect(status().isNoContent());

        verify(orgCommandService).removeTeamMember(200L, 10L);
    }
}

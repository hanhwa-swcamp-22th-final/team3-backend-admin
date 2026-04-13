package com.ohgiraffers.team3backendadmin.admin.query.mapper;

import com.ohgiraffers.team3backendadmin.admin.query.dto.response.org.OrgEmployeeItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrgMapper {

    /** 직원 목록 (부서/팀/키워드 필터, 티어 포함) */
    List<OrgEmployeeItem> findOrgEmployees(
            @Param("departmentId") Long departmentId,
            @Param("teamId")       Long teamId,
            @Param("keyword")      String keyword,
            @Param("offset")       int offset,
            @Param("size")         int size
    );

    /** 특정 팀 소속 직원 전체 */
    List<OrgEmployeeItem> findEmployeesByTeamId(@Param("teamId") Long teamId);
}

package com.ohgiraffers.team3backendadmin.admin.command.domain.repository;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.skill.Skill;
import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.skill.SkillCategory;
import com.ohgiraffers.team3backendadmin.admin.command.infrastructure.repository.JpaSkillRepository;

import java.util.Optional;

public interface SkillRepository extends JpaSkillRepository {
    Optional<Skill> findByEmployeeIdAndSkillCategory(Long employeeId, SkillCategory skillCategory);
}

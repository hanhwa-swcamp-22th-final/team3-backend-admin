package com.ohgiraffers.team3backendadmin.admin.command.infrastructure.repository;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.skill.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaSkillRepository extends JpaRepository<Skill, Long> {
}

package com.ohgiraffers.team3backendadmin.admin.command.domain.repository;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.domainkeyword.DomainKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DomainKeywordRepository extends JpaRepository<DomainKeyword, Long> {
}

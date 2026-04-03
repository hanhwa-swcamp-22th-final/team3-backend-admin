package com.ohgiraffers.team3backendadmin.admin.command.infrastructure.repository;

import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.consent.Consent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaConsentRepository extends JpaRepository<Consent, Long> {
}

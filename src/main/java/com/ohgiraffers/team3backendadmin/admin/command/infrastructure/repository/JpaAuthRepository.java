package com.ohgiraffers.team3backendadmin.admin.command.infrastructure.repository;



import com.ohgiraffers.team3backendadmin.admin.command.domain.aggregate.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaAuthRepository extends JpaRepository<RefreshToken, String> {

}

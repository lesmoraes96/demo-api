package com.example.demo.infrastructure.repository;

import com.example.demo.domain.model.Insurance;
import com.example.demo.domain.ports.InsuranceRepositoryPort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InsuranceRepositoryImpl extends JpaRepository<Insurance, Long>, InsuranceRepositoryPort {
}

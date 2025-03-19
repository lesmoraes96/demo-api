package com.example.demo.infrastructure.adapters;

import com.example.demo.domain.model.Insurance;
import com.example.demo.application.ports.InsuranceRepositoryPort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InsuranceRepositoryImpl extends JpaRepository<Insurance, Long>, InsuranceRepositoryPort {
}

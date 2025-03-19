package com.example.demo.application.ports;

import com.example.demo.domain.model.Insurance;

import java.util.List;
import java.util.Optional;

public interface InsuranceRepositoryPort {
    Insurance save(Insurance insurance);
    Optional<Insurance> findById(Long id);
    List<Insurance> findAll();
    void deleteById(Long id);
}

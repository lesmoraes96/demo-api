package com.example.demo.application.service;


import com.example.demo.domain.model.Insurance;
import com.example.demo.domain.ports.InsuranceRepositoryPort;
import com.example.demo.domain.ports.InsuranceServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class InsuranceServiceImpl implements InsuranceServicePort {

    @Autowired
    private final InsuranceRepositoryPort repository;

    @Override
    public Insurance createInsurance(Insurance insurance) {
        return repository.save(insurance);
    }

    @Override
    public Optional<Insurance> getInsuranceById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Insurance> getAllInsurances() {
        return repository.findAll();
    }

    @Override
    public Insurance updateInsurance(Long id, Insurance insurance) {
        if (repository.findById(id).isPresent()) {
            insurance.setId(id);
            return repository.save(insurance);
        }
        throw new RuntimeException("Insurance not found");
    }

    @Override
    public void deleteInsurance(Long id) {
        if (repository.findById(id).isPresent()) {
            repository.deleteById(id);
        }
        else{
            throw new RuntimeException("Insurance not found");
        }
    }

    @Override
    public double calculatePremium(Long id) {
        return repository.findById(id)
                .map(Insurance::calculatePremium)
                .orElseThrow(() -> new RuntimeException("Insurance not found"));
    }

    @Override
    public Map<Integer, Double> calculateTotalPremiumByMonth() {
        List<Insurance> allInsurances = repository.findAll();
        return Insurance.calculateTotalPremiumByMonth(allInsurances);
    }
}

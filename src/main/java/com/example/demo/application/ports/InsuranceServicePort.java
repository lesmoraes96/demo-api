package com.example.demo.application.ports;

import com.example.demo.domain.model.Insurance;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface InsuranceServicePort {
    Insurance createInsurance(Insurance insurance);
    Optional<Insurance> getInsuranceById(Long id);
    List<Insurance> getAllInsurances();
    Insurance updateInsurance(Long id, Insurance insurance);
    void deleteInsurance(Long id);
    double calculatePremium(Long id);

    Map<Integer, Double> calculateTotalPremiumByMonth();
}

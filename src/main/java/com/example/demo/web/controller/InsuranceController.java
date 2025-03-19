package com.example.demo.web.controller;


import com.example.demo.application.service.InsuranceServiceImpl;
import com.example.demo.domain.model.Insurance;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/insurance")
@RequiredArgsConstructor
public class InsuranceController {

    @Autowired
    private final InsuranceServiceImpl service;

    @PostMapping
    public ResponseEntity<Insurance> createInsurance(@RequestBody Insurance insurance) {
        Insurance createdInsurance = service.createInsurance(insurance);
        return ResponseEntity.status(201).body(createdInsurance);
    }

    @GetMapping
    public ResponseEntity<List<Insurance>> getAllInsurances() {
        List<Insurance> insurances = service.getAllInsurances();
        return ResponseEntity.ok(insurances);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Insurance> getInsurance(@PathVariable Long id) {
        Optional<Insurance> insurance = service.getInsuranceById(id);
        return insurance.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Insurance> updateInsurance(@PathVariable Long id, @RequestBody Insurance updatedInsurance) {
        try {
            Insurance insurance = service.updateInsurance(id, updatedInsurance);
            return ResponseEntity.ok(insurance);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInsurance(@PathVariable Long id) {
        try {
            service.deleteInsurance(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/premium/{id}")
    public ResponseEntity<Double> calculatePremium(@PathVariable Long id) {
        try {
            double premium = service.calculatePremium(id);
            return ResponseEntity.ok(premium);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    @GetMapping("/total-premium-by-month")
    public ResponseEntity<Map<Integer, Double>> calculateTotalPremiumByMonth() {
        Map<Integer, Double> premiumsByMonth = service.calculateTotalPremiumByMonth();
        if (premiumsByMonth.isEmpty()) {
            return ResponseEntity.status(204).body(null);
        }
        return ResponseEntity.ok(premiumsByMonth);
    }
}

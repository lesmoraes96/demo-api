package com.example.demo.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class InsuranceTest {

    private Insurance insurance;

    @BeforeEach
    void setUp() {
        insurance = new Insurance();
    }

    @Test
    void shouldCalculatePremiumCorrectly() {
        // Given
        insurance = Insurance.builder()
                .policyNumber("ABC123")
                .holderName("João Silva")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusYears(1))
                .insuredAmount(100000.0)
                .riskFactor(1.2)
                .build();

        // When
        double premium = insurance.calculatePremium();

        // Then
        assertEquals(6000.0, premium, 0.01); // insuredAmount * riskFactor * 0.05
    }

    @Test
    void shouldCalculateTotalPremiumForEmptyList(){
        // When
        Map<Integer, Double> result = insurance.calculateTotalPremiumByMonth(new ArrayList<>());

        //Then
        assertEquals(new HashMap<>(), result);
    }

    @Test
    void shouldCalculateTotalPremiumForSingleResult(){

        // Given
        Insurance insurance1 = new Insurance();
        insurance1.setClaimDate(LocalDate.of(2025, 3, 15));
        insurance1.setInsuredAmount(100000.0);
        insurance1.setRiskFactor(1.2);

        List<Insurance> allInsurances = List.of(insurance1);

        // When
        Map<Integer, Double> result = insurance.calculateTotalPremiumByMonth(allInsurances);

        //Then
        assertEquals(6000.0, result.get(3));
    }

    @Test
    void shouldCalculateTotalPremiumForMultipleResults() {

        // Given
        Insurance insurance1 = new Insurance();
        insurance1.setClaimDate(LocalDate.of(2025, 3, 15));
        insurance1.setInsuredAmount(100000.0);
        insurance1.setRiskFactor(1.2);

        Insurance insurance2 = new Insurance();
        insurance2.setClaimDate(LocalDate.of(2025, 3, 15));
        insurance2.setInsuredAmount(100000.0);
        insurance2.setRiskFactor(1.2);

        Insurance insurance3 = new Insurance();
        insurance3.setClaimDate(LocalDate.of(2025, 4, 15));
        insurance3.setInsuredAmount(100000.0);
        insurance3.setRiskFactor(1.2);

        List<Insurance> allInsurances = List.of(insurance1, insurance2, insurance3);

        // When
        Map<Integer, Double> result = insurance.calculateTotalPremiumByMonth(allInsurances);

        //Then
        assertEquals(12000.0, result.get(3));
        assertEquals(6000.0, result.get(4));
    }
}

package com.example.demo.service;

import com.example.demo.application.service.InsuranceServiceImpl;
import com.example.demo.domain.model.Insurance;
import com.example.demo.domain.ports.InsuranceRepositoryPort;
import com.example.demo.domain.ports.InsuranceServicePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InsuranceServiceTest {

    @Mock
    private InsuranceRepositoryPort insuranceRepository;

    @InjectMocks
    private InsuranceServiceImpl insuranceService;

    private Insurance insurance;
    private List<Insurance> insuranceList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        insurance = Insurance.builder()
                .policyNumber("ABC123")
                .holderName("Maria Souza")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusYears(1))
                .insuredAmount(50000.0)
                .riskFactor(1.1)
                .build();

        insuranceList.add(insurance);
    }

    @Test
    void shouldCreateInsuranceSuccessfully() {
        // Given
        when(insuranceRepository.save(any(Insurance.class))).thenReturn(insurance);

        // When
        Insurance savedInsurance = insuranceService.createInsurance(insurance);

        // Then
        assertNotNull(savedInsurance);
        assertEquals("ABC123", savedInsurance.getPolicyNumber());
        verify(insuranceRepository, times(1)).save(any(Insurance.class));
    }

    @Test
    void shouldFindInsuranceById() {
        // Given
        when(insuranceRepository.findById(any(Long.class))).thenReturn(Optional.of(insurance));

        // When
        Optional<Insurance> found = insuranceService.getInsuranceById(1L);

        // Then
        assertTrue(found.isPresent());
        assertEquals("Maria Souza", found.get().getHolderName());
    }

    @Test
    void shouldReturnEmptyWhenInsuranceNotFoundById() {
        // Given
        when(insuranceRepository.findById(3L)).thenReturn(Optional.empty());

        // When
        Optional<Insurance> found = insuranceService.getInsuranceById(3L);

        // Then
        assertFalse(found.isPresent());
        verify(insuranceRepository, times(1)).findById(3L);
    }

    @Test
    void shouldFindAllInsurances() {
        // Given
        when(insuranceRepository.findAll()).thenReturn(insuranceList);

        // When
        List<Insurance> found = insuranceService.getAllInsurances();

        // Then
        assertNotNull(found);
        assertEquals("Maria Souza", found.get(0).getHolderName());
        assertEquals(1, found.size());
        verify(insuranceRepository, times(1)).findAll();
    }

    @Test
    void shouldUpdateInsuranceSuccessfully() {
        // Given

        Insurance updatedInsurance = Insurance.builder()
                .id(1L)
                .policyNumber("ABC123")
                .holderName("João Atualizado")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusYears(1))
                .insuredAmount(120000.0)
                .riskFactor(1.3)
                .build();

        when(insuranceRepository.findById(1L)).thenReturn(Optional.of(insurance));
        when(insuranceRepository.save(any(Insurance.class))).thenReturn(updatedInsurance);

        // When
        Insurance result = insuranceService.updateInsurance(1L, updatedInsurance);

        // Then
        assertNotNull(result);
        assertEquals("João Atualizado", result.getHolderName());
        assertEquals(120000.0, result.getInsuredAmount());
        verify(insuranceRepository, times(1)).findById(1L);
        verify(insuranceRepository, times(1)).save(any(Insurance.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingInsurance() {
        // Given
        when(insuranceRepository.findById(3L)).thenReturn(Optional.empty());

        Insurance updatedInsurance = Insurance.builder()
                .id(3L)
                .policyNumber("XYZ000")
                .holderName("Nome Fictício")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusYears(1))
                .insuredAmount(75000.0)
                .riskFactor(1.2)
                .build();

        // When & Then
        assertThrows(RuntimeException.class, () -> insuranceService.updateInsurance(3L, updatedInsurance));
        verify(insuranceRepository, times(1)).findById(3L);
        verify(insuranceRepository, never()).save(any(Insurance.class));
    }

    @Test
    void shouldDeleteInsuranceSuccessfully() {
        // Given
        when(insuranceRepository.findById(1L)).thenReturn(Optional.of(insurance));
        doNothing().when(insuranceRepository).deleteById(1L);

        // When
        insuranceService.deleteInsurance(1L);

        // Then
        verify(insuranceRepository, times(1)).findById(1L);
        verify(insuranceRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingInsurance() {
        // Given
        when(insuranceRepository.findById(3L)).thenReturn(Optional.empty());

        // When & Then.
        assertThrows(RuntimeException.class, () -> insuranceService.deleteInsurance(3L));
        verify(insuranceRepository, times(1)).findById(3L);
        verify(insuranceRepository, never()).deleteById(anyLong());
    }

    @Test
    void shouldCalculatePremiumWhenInsuranceExists() {
        // Given
        when(insuranceRepository.findById(1L)).thenReturn(Optional.of(insurance));

        // When
        double premium = insuranceService.calculatePremium(1L);

        // Then
        double expectedPremium = insurance.calculatePremium();
        assertEquals(expectedPremium, premium);
        verify(insuranceRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenInsuranceNotFound() {
        // Given
        when(insuranceRepository.findById(1L)).thenReturn(Optional.empty());

        // When &Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            insuranceService.calculatePremium(1L);
        });
        assertEquals("Insurance not found", exception.getMessage());
        verify(insuranceRepository, times(1)).findById(1L);
    }

    @Test
    void shouldCalculateTotalPremiumByMonth() {
        // Given
        Insurance insurance1 = new Insurance();
        insurance1.setClaimDate(LocalDate.of(2025, 3, 15));
        insurance1.setInsuredAmount(100000.0);
        insurance1.setRiskFactor(1.2);

        Insurance insurance2 = new Insurance();
        insurance2.setClaimDate(LocalDate.of(2025, 3, 15));
        insurance2.setInsuredAmount(100000.0);
        insurance2.setRiskFactor(1.2);

        List<Insurance> allInsurances = List.of(insurance1, insurance2);

        // When
        when(insuranceRepository.findAll()).thenReturn(allInsurances);
        Map<Integer, Double> result = insuranceService.calculateTotalPremiumByMonth();

        // Then
        assertEquals(12000.0, result.get(3));
    }
}

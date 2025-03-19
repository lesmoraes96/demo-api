package com.example.demo.web.controller;

import com.example.demo.domain.model.Insurance;
import com.example.demo.application.ports.InsuranceServicePort;
import com.example.demo.web.controller.InsuranceController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class InsuranceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private InsuranceServicePort service;

    @InjectMocks
    private InsuranceController controller;

    private Insurance insurance;
    private List<Insurance> insuranceList = new ArrayList<>();
    private String insuranceJson;

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

        insuranceJson = """
        {
            "policyNumber": "ABC123",
            "holderName": "Maria Souza",
            "startDate": "2025-03-15",
            "endDate": "2026-03-15",
            "insuredAmount": 50000.0,
            "riskFactor": 1.1
        }
        """;
    }

    @Test
    void shouldCreateInsurance() throws Exception {
        when(service.createInsurance(any(Insurance.class))).thenReturn(insurance);

        mockMvc.perform(post("/api/insurance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(insuranceJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.policyNumber").value("ABC123"))
                .andExpect(jsonPath("$.holderName").value("Maria Souza"))
                .andExpect(jsonPath("$.insuredAmount").value(50000.0));
    }

    @Test
    void shouldGetAllInsurances() throws Exception {
        when(service.getAllInsurances()).thenReturn(List.of(insurance));

        mockMvc.perform(get("/api/insurance"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].policyNumber").value("ABC123"));
    }

    @Test
    void shouldGetInsuranceById() throws Exception {
        when(service.getInsuranceById(anyLong())).thenReturn(Optional.of(insurance));

        mockMvc.perform(get("/api/insurance/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.policyNumber").value("ABC123"));

    }

    @Test
    void shouldReturnNotFoundForNonexistentInsurance() throws Exception {
        when(service.getInsuranceById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/insurance/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Disabled
    void shouldUpdateInsurance() throws Exception {
        when(service.getInsuranceById(1L)).thenReturn(Optional.of(insurance));
        when(service.updateInsurance(eq(1L), any(Insurance.class))).thenReturn(insurance);

        mockMvc.perform(put("/api/insurance/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(insuranceJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.policyNumber").value("ABC123"))
                .andExpect(jsonPath("$.holderName").value("Maria Souza"));
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonexistentInsurance() throws Exception {
        when(service.updateInsurance(anyLong(), any(Insurance.class))).thenThrow(new RuntimeException("Insurance not found"));

        mockMvc.perform(put("/api/insurance/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(insuranceJson))
                .andExpect(status().isNotFound());
    }

    @Test
    @Disabled
    void shouldDeleteInsurance() throws Exception {
        when(service.getInsuranceById(1L)).thenReturn(Optional.of(insurance));
        doNothing().when(service).deleteInsurance(1L);

        mockMvc.perform(delete("/api/insurance/1"))
                .andExpect(status().isNoContent());

        verify(service, times(1)).deleteInsurance(1L);
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonexistentInsurance() throws Exception {
        doThrow(new RuntimeException("Insurance not found")).when(service).deleteInsurance(anyLong());

        mockMvc.perform(delete("/api/insurance/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Disabled
    void shouldReturnPremiumForInsurance() throws Exception {
        // Dado
        Long insuranceId = 1L;
        double premium = 100.0;
        when(service.calculatePremium(insuranceId)).thenReturn(premium);

        // Quando e Então
        mockMvc.perform(get("/insurance/premium/{id}", insuranceId))
                .andExpect(status().isOk())  // Verifica se o status é 200 OK
                .andExpect(content().string(String.valueOf(premium)));  // Verifica o valor do prêmio no corpo da resposta
    }

    @Test
    void shouldReturnNotFoundForInsuranceNotFound() throws Exception {
        // Given
        Long insuranceId = 1L;
        when(service.calculatePremium(insuranceId)).thenThrow(new RuntimeException("Insurance not found"));

        // When & Then
        mockMvc.perform(get("/insurance/premium/{id}", insuranceId))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

    @Test
    @Disabled
    void shouldReturnTotalPremiumByMonth() throws Exception {
        // Given
        Map<Integer, Double> premiumsByMonth = Map.of(
                3, 100.0,  // Março
                4, 150.0   // Abril
        );
        when(service.calculateTotalPremiumByMonth()).thenReturn(premiumsByMonth);

        // When & Then
        mockMvc.perform(get("/insurance/total-premium-by-month"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.3").value(100.0))
                .andExpect(jsonPath("$.4").value(150.0));
    }

    @Test
    @Disabled
    void shouldReturnNoContentWhenNoPremiums() throws Exception {
        // Given
        Map<Integer, Double> premiumsByMonth = Map.of();
        when(service.calculateTotalPremiumByMonth()).thenReturn(premiumsByMonth);

        // When & Then
        mockMvc.perform(get("/insurance/total-premium-by-month"))
                .andExpect(status().isNoContent());
    }
}

package com.example.demo.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Entity
@Table(name = "insurance")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Builder
public class Insurance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String policyNumber;

    @Column(nullable = false)
    private String holderName;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = true)
    private LocalDate claimDate;

    @Column(nullable = false)
    private double insuredAmount;

    @Column(nullable = false)
    private double riskFactor; // Exemplo: 1.2 para maior risco, 0.8 para menor risco

    /**
     * Calcula o valor do prêmio do seguro com base no valor segurado e fator de risco.
     * @return Valor do prêmio do seguro.
     */
    public double calculatePremium() {
        return insuredAmount * riskFactor * 0.05; // Fórmula fictícia
    }
    public double calculatePremium(double insuredAmount, double riskFactor) {
        return insuredAmount * riskFactor * 0.05; // Fórmula fictícia
    }

    public static Map<Integer, Double> calculateTotalPremiumByMonth(List<Insurance> allInsurances) {
        // Cria um mapa para armazenar a somatória do prêmio por mês
        Map<Integer, Double> monthlyPremiums = new ConcurrentHashMap<>();

        // Itera sobre todos os seguros
        for (Insurance insurance : allInsurances) {
            // Verifica se o seguro tem uma data de sinistro (claimDate)
            if (insurance.getClaimDate() != null) {
                // Obtém o mês do claimDate
                int month = insurance.getClaimDate().getMonthValue();

                // Calcula o prêmio do seguro
                double premium = insurance.calculatePremium(insurance.getInsuredAmount(), insurance.getRiskFactor());

                // Adiciona o prêmio ao total do mês correspondente
                monthlyPremiums.merge(month, premium, Double::sum);  // Atualiza o valor do mês somando o prêmio
            }
        }
        // Retorna o mapa contendo o prêmio total por mês
        return monthlyPremiums;
    }
}

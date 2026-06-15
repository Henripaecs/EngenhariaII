package com.saude.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "atendimentos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Atendimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Data é obrigatória")
    @Column(nullable = false)
    private LocalDate data;

    private LocalTime horario;

    @Column(name = "problema_texto", columnDefinition = "TEXT")
    private String problemaTexto;

    /**
     * Receitas de saúde associadas ao atendimento.
     * Pode ser: Remédio (médico), Atividade Física (fisioterapeuta), Atividades Mentais (psicólogo)
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "atendimento_receitas",
        joinColumns = @JoinColumn(name = "atendimento_id")
    )
    @Column(name = "receita", columnDefinition = "TEXT")
    private List<String> receitaSaude = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "profissional_id")
    private ProfissionalDeSaude profissional;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm = LocalDateTime.now();
}

package com.saude.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "profissionais_saude")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfissionalDeSaude {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Column(length = 100, nullable = false)
    private String nome;

    @Column(length = 20)
    private String telefone;

    @Column(length = 200)
    private String endereco;

    /**
     * Categorias possíveis: PSICOLOGO, FISIOTERAPEUTA, MEDICO
     * Armazenadas como lista de strings em tabela separada (ElementCollection)
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "profissional_categorias",
        joinColumns = @JoinColumn(name = "profissional_id")
    )
    @Column(name = "categoria", length = 50)
    @Enumerated
    private List<String> categoria = new ArrayList<>();

    @Column(name = "criado_em")
    private LocalDateTime criadoEm = LocalDateTime.now();
}

package com.saude;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saude.model.Atendimento;
import com.saude.model.ProfissionalDeSaude;
import com.saude.repository.AtendimentoRepository;
import com.saude.repository.ProfissionalDeSaudeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AtendimentoControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private AtendimentoRepository atendimentoRepository;
    @Autowired private ProfissionalDeSaudeRepository profissionalRepository;
    @Autowired private ObjectMapper objectMapper;

    private ProfissionalDeSaude profissional;

    @BeforeEach
    void setUp() {
        atendimentoRepository.deleteAll();
        profissionalRepository.deleteAll();

        profissional = new ProfissionalDeSaude();
        profissional.setNome("Dra. Ana Costa");
        profissional.setCategoria(List.of("MEDICO"));
        profissional = profissionalRepository.save(profissional);
    }

    @Test
    void deveInserirAtendimento() throws Exception {
        Atendimento atend = new Atendimento();
        atend.setData(LocalDate.now());
        atend.setHorario(LocalTime.of(10, 0));
        atend.setProblemaTexto("Dor de cabeça frequente");
        atend.setReceitaSaude(List.of("Paracetamol 500mg - 1 comprimido de 8 em 8 horas"));
        atend.setProfissional(profissional);

        mockMvc.perform(post("/api/atendimentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(atend)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.problemaTexto").value("Dor de cabeça frequente"));
    }

    @Test
    void deveListarAtendimentos() throws Exception {
        Atendimento atend = new Atendimento();
        atend.setData(LocalDate.now());
        atend.setProfissional(profissional);
        atendimentoRepository.save(atend);

        mockMvc.perform(get("/api/atendimentos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void deveExcluirAtendimento() throws Exception {
        Atendimento atend = new Atendimento();
        atend.setData(LocalDate.now());
        atend.setProfissional(profissional);
        Atendimento salvo = atendimentoRepository.save(atend);

        mockMvc.perform(delete("/api/atendimentos/" + salvo.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensagem").value("Atendimento removido com sucesso"));
    }
}

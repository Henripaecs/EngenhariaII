package com.saude;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saude.model.ProfissionalDeSaude;
import com.saude.repository.ProfissionalDeSaudeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ProfissionalDeSaudeControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ProfissionalDeSaudeRepository repository;
    @Autowired private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    void deveInserirProfissional() throws Exception {
        ProfissionalDeSaude prof = new ProfissionalDeSaude();
        prof.setNome("Dr. João Silva");
        prof.setTelefone("11999990000");
        prof.setEndereco("Rua das Flores, 100");
        prof.setCategoria(List.of("MEDICO"));

        mockMvc.perform(post("/api/profissionais")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(prof)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Dr. João Silva"))
                .andExpect(jsonPath("$.categoria[0]").value("MEDICO"));
    }

    @Test
    void deveListarProfissionais() throws Exception {
        ProfissionalDeSaude prof = new ProfissionalDeSaude();
        prof.setNome("Dra. Maria");
        prof.setCategoria(List.of("PSICOLOGO"));
        repository.save(prof);

        mockMvc.perform(get("/api/profissionais"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void deveBuscarProfissionalPorId() throws Exception {
        ProfissionalDeSaude prof = new ProfissionalDeSaude();
        prof.setNome("Dr. Pedro");
        prof.setCategoria(List.of("FISIOTERAPEUTA"));
        ProfissionalDeSaude salvo = repository.save(prof);

        mockMvc.perform(get("/api/profissionais/" + salvo.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Dr. Pedro"));
    }

    @Test
    void deveAlterarProfissional() throws Exception {
        ProfissionalDeSaude prof = new ProfissionalDeSaude();
        prof.setNome("Dr. Antigo");
        prof.setCategoria(List.of("MEDICO"));
        ProfissionalDeSaude salvo = repository.save(prof);

        salvo.setNome("Dr. Novo");
        mockMvc.perform(put("/api/profissionais/" + salvo.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(salvo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Dr. Novo"));
    }

    @Test
    void deveExcluirProfissional() throws Exception {
        ProfissionalDeSaude prof = new ProfissionalDeSaude();
        prof.setNome("Dr. Excluir");
        prof.setCategoria(List.of("MEDICO"));
        ProfissionalDeSaude salvo = repository.save(prof);

        mockMvc.perform(delete("/api/profissionais/" + salvo.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensagem").value("Profissional removido com sucesso"));
    }

    @Test
    void deveRetornar404QuandoNaoEncontrado() throws Exception {
        mockMvc.perform(get("/api/profissionais/9999"))
                .andExpect(status().isNotFound());
    }
}

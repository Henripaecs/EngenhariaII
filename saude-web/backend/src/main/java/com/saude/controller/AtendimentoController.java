package com.saude.controller;

import com.saude.model.Atendimento;
import com.saude.repository.AtendimentoRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/atendimentos")
@CrossOrigin(origins = "*")
public class AtendimentoController {

    private final AtendimentoRepository repository;

    public AtendimentoController(AtendimentoRepository repository) {
        this.repository = repository;
    }

    // INSERIR - POST /api/atendimentos
    @PostMapping
    public ResponseEntity<Atendimento> inserir(@Valid @RequestBody Atendimento atendimento) {
        Atendimento salvo = repository.save(atendimento);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    // LISTAR TODOS - GET /api/atendimentos
    @GetMapping
    public ResponseEntity<List<Atendimento>> listar() {
        return ResponseEntity.ok(repository.findAllByOrderByDataAscHorarioAsc());
    }

    // CONSULTAR(id) - GET /api/atendimentos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> buscar(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    // LISTAR POR PROFISSIONAL - GET /api/atendimentos/profissional/{profissionalId}
    @GetMapping("/profissional/{profissionalId}")
    public ResponseEntity<List<Atendimento>> listarPorProfissional(
            @PathVariable Long profissionalId) {
        return ResponseEntity.ok(repository.findByProfissionalId(profissionalId));
    }

    // ALTERAR(id) - PUT /api/atendimentos/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> alterar(@PathVariable Long id,
                                     @Valid @RequestBody Atendimento dados) {
        return repository.findById(id)
                .map(atend -> {
                    atend.setData(dados.getData());
                    atend.setHorario(dados.getHorario());
                    atend.setProblemaTexto(dados.getProblemaTexto());
                    atend.setReceitaSaude(dados.getReceitaSaude());
                    atend.setProfissional(dados.getProfissional());
                    return ResponseEntity.ok(repository.save(atend));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // EXCLUIR(id) - DELETE /api/atendimentos/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id) {
        return repository.findById(id)
                .map(atend -> {
                    repository.delete(atend);
                    return ResponseEntity.ok(
                        Map.of("mensagem", "Atendimento removido com sucesso"));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}

package com.saude.controller;

import com.saude.model.ProfissionalDeSaude;
import com.saude.repository.ProfissionalDeSaudeRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/profissionais")
@CrossOrigin(origins = "*")
public class ProfissionalDeSaudeController {

    private final ProfissionalDeSaudeRepository repository;

    public ProfissionalDeSaudeController(ProfissionalDeSaudeRepository repository) {
        this.repository = repository;
    }

    // INSERIR - POST /api/profissionais
    @PostMapping
    public ResponseEntity<ProfissionalDeSaude> inserir(
            @Valid @RequestBody ProfissionalDeSaude profissional) {
        ProfissionalDeSaude salvo = repository.save(profissional);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    // ALTERAR(id) - PUT /api/profissionais/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> alterar(@PathVariable Long id,
                                     @Valid @RequestBody ProfissionalDeSaude dados) {
        return repository.findById(id)
                .map(prof -> {
                    prof.setNome(dados.getNome());
                    prof.setTelefone(dados.getTelefone());
                    prof.setEndereco(dados.getEndereco());
                    prof.setCategoria(dados.getCategoria());
                    return ResponseEntity.ok(repository.save(prof));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // CONSULTAR(Nome) - GET /api/profissionais?nome=xxx
    @GetMapping
    public ResponseEntity<List<ProfissionalDeSaude>> consultarPorNome(
            @RequestParam(required = false) String nome) {
        List<ProfissionalDeSaude> lista;
        if (nome != null && !nome.isBlank()) {
            lista = repository.findByNomeContainingIgnoreCase(nome);
        } else {
            lista = repository.findAllByOrderByNomeAsc();
        }
        return ResponseEntity.ok(lista);
    }

    // CONSULTAR(id) - GET /api/profissionais/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> consultarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    // CONSULTAR(Categoria) - GET /api/profissionais/categoria/{categoria}
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<ProfissionalDeSaude>> consultarPorCategoria(
            @PathVariable String categoria) {
        List<ProfissionalDeSaude> lista = repository.findByCategoriaContaining(categoria.toUpperCase());
        return ResponseEntity.ok(lista);
    }

    // EXCLUIR(id) - DELETE /api/profissionais/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id) {
        return repository.findById(id)
                .map(prof -> {
                    repository.delete(prof);
                    return ResponseEntity.ok(
                        Map.of("mensagem", "Profissional removido com sucesso"));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}

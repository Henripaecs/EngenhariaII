package com.saude.repository;

import com.saude.model.ProfissionalDeSaude;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfissionalDeSaudeRepository extends JpaRepository<ProfissionalDeSaude, Long> {

    List<ProfissionalDeSaude> findAllByOrderByNomeAsc();

    List<ProfissionalDeSaude> findByNomeContainingIgnoreCase(String nome);

    List<ProfissionalDeSaude> findByCategoriaContaining(String categoria);
}

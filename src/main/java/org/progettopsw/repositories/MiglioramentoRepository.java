package org.progettopsw.repositories;

import org.progettopsw.models.Miglioramento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MiglioramentoRepository extends JpaRepository<Miglioramento, Long>
{

    List<Miglioramento> findAllByOrderByNomeAsc();

    Miglioramento findByNome(String nome);

    List<Miglioramento> findMiglioramentoByNomeContaining(String nome);

    @Query("SELECT M FROM Miglioramento M WHERE M.crediti <= ?1")
    List<Miglioramento> findByCrediti(int crediti);
}


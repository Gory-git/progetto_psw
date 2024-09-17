package org.progettopsw.repositories;

import org.progettopsw.models.Partita;
import org.progettopsw.models.Utente;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PartitaRepository extends JpaRepository<Partita, Long>
{
    @Query("SELECT P FROM Partita P WHERE P.utente = ?1")
    public List<Partita> findByUtenteOrderByDataPartitaDesc(Utente utente, Pageable pageable);

    @Query("SELECT P FROM Partita P WHERE P.utente = ?1")
    public List<Partita> findByUtenteOrderByDCreditiOttenutiDesc(Utente utente, Pageable pageable);

    @Query("SELECT P FROM Partita P WHERE P.utente = ?1 AND P.data_partita = ?2")
    public List<Partita> findByUtenteDataPartitaDesc(Utente utente, Date dataPartita, Pageable pageable);

    public boolean existsById(Long id);
}

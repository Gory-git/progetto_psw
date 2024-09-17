package org.progettopsw.repositories;

import org.progettopsw.models.Miglioramento;
import org.progettopsw.models.Utente;
import org.progettopsw.models.UtenteMiglioramento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UtenteMiglioramentoRepository extends JpaRepository<UtenteMiglioramento, Long>
{
    @Query("SELECT UM.miglioramento FROM UtenteMiglioramento UM WHERE UM.utente = ?1")
    public List<Miglioramento> findMiglioramentiByUtente(Utente utente);

    @Query("SELECT UM.quantita FROM UtenteMiglioramento UM WHERE UM.utente = ?1 AND UM.miglioramento = ?2")
    public int quantitaMiglioramentoPerUtente(Utente utente, Miglioramento miglioramento);

    @Query("UPDATE UtenteMiglioramento UM SET UM.quantita = ?1 WHERE UM.utente = ?2 AND UM.miglioramento = ?3")
    public void updateQuantita(int quantita, Utente utente, Miglioramento miglioramento);
}
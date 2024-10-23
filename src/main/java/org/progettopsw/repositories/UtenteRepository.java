package org.progettopsw.repositories;

import org.progettopsw.models.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, Long>
{

    @Modifying
    @Query("UPDATE Utente U SET U.crediti = ?2 WHERE U = ?1")
    void updateCrediti(Utente utente, int crediti);

    boolean existsByEmail(String email);

    Utente findByEmailIgnoreCase(String email);
}

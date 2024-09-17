package org.progettopsw.repositories;

import org.progettopsw.models.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, Long>
{
    @Query("SELECT U FROM Utente U WHERE U.email like ?1")
    Utente findByEmailLike(String email);

    @Query("UPDATE Utente U SET U.crediti = ?1 WHERE U = ?2")
    void updateCrediti(Utente utente, double crediti);

    boolean existsByEmail(String email);

    Utente findByEmailIgnoreCaseAndPassword(String email, String password);
}

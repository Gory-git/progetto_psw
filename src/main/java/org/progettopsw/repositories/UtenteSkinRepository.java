package org.progettopsw.repositories;

import org.progettopsw.models.Skin;
import org.progettopsw.models.Utente;
import org.progettopsw.models.UtenteSkin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UtenteSkinRepository extends JpaRepository<UtenteSkin, Long>
{
    @Query("SELECT UM.skin FROM UtenteSkin UM WHERE UM.utente = ?1")
    public List<Skin> findSkinByUtente(Utente utente);

}

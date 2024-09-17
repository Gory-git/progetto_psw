package org.progettopsw.repositories;

import org.progettopsw.models.Skin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkinRepository extends JpaRepository<Skin, Long>
{
    @Query("SELECT S FROM Skin S WHERE S.nome LIKE ?1")
    List<Skin> findByNome(String nome);

    @Query("SELECT S FROM Skin S WHERE S.crediti <= ?1")
    List<Skin> findByCrediti(int crediti);
}

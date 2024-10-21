package org.progettopsw.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "skin")
public class Skin
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_skin", nullable = false)
    private long id;

    @Column(name = "crediti", nullable = false)
    private int crediti;

    @Column(name = "nome", nullable = false, unique = true)
    private String nome;

    @OneToMany(mappedBy = "skin", orphanRemoval = true)
    private List<UtenteSkin> utenti;
}

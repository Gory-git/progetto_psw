package org.progettopsw.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "utente")
public class Utente
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_utente", nullable = false)
    private long id_utente;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "nome")
    private String nome;

    @Column(name = "cognome")
    private String cognome;

    @Column(name = "e_mail", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "crediti")
    private int crediti;

    @OneToMany(mappedBy = "utente", orphanRemoval = true)
    private List<UtenteMiglioramento> miglioramenti;

    @OneToMany(mappedBy = "utente", orphanRemoval = true)
    private List<UtenteSkin> skin;
}

package org.progettopsw.models;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@Entity
@Table(name = "partita")
public class Partita
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_partita", nullable = false)
    private long id;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "utente", nullable = false, unique = true)
    private Utente utente;

    @Column(name = "crediti_ottenuti", nullable = false)
    private int crediti_ottenuti;

    @Column(name = "esito", nullable = false)
    private String esito;

    @CreationTimestamp
    @Column(name = "data_partita", nullable = false)
    private Date data_partita;
}

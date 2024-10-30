package org.progettopsw.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SortComparator;

import java.io.Serializable;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "miglioramento")
public class Miglioramento
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_miglioramento", nullable = false)
    private long id;

    @Column(name = "crediti", nullable = false)
    private int crediti;

    @Column(name = "descrizione", nullable = false, unique = true, length = 512)
    private String descrizione;

    @Column(name = "nome", nullable = false, unique = true)
    private String nome;

    @Column(name = "quantita_massima", nullable = false)
    private int quantita_massima;

    @Column(name = "tipologia", nullable = false)
    private String tipologia;

    @OneToMany(mappedBy = "miglioramento", orphanRemoval = true)
    private List<UtenteMiglioramento> utenti = new LinkedList<>();
}

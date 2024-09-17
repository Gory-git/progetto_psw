package org.progettopsw.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.progettopsw.support.embeddables.UtenteMiglioramentoKey;

@Getter
@Setter
@Entity
@Table(name = "utente_miglioramento")
public class UtenteMiglioramento
{
    @EmbeddedId
    private UtenteMiglioramentoKey id;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @MapsId("utente")
    @JoinColumn(name = "utente", nullable = false)
    private Utente utente;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @MapsId("miglioramento")
    @JoinColumn(name = "miglioramento", nullable = false)
    private Miglioramento miglioramento;

    @Column(name = "quantita")
    private int quantita;
}

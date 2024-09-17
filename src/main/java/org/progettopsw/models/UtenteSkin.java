package org.progettopsw.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.progettopsw.support.embeddables.UtenteSkinKey;

@Getter
@Setter
@Entity
@Table(name = "utente_skin")
public class UtenteSkin
{
    @EmbeddedId
    private UtenteSkinKey id;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @MapsId("utente")
    @JoinColumn(name = "utente", nullable = false)
    private Utente utente;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @MapsId("skin")
    @JoinColumn(name = "skin", nullable = false)
    private Skin skin;
}


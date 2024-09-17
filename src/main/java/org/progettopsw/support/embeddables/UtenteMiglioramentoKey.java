package org.progettopsw.support.embeddables;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Embeddable
public class UtenteMiglioramentoKey implements Serializable
{
    @Column(name = "utente")
    private long utente;

    @Column(name = "miglioramento")
    private long miglioramento;

    @Override
    public int hashCode()
    {
        int M = 31;
        return (int) (utente * M + miglioramento * M);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof UtenteMiglioramentoKey)
            return this.utente == ((UtenteMiglioramentoKey) obj).utente && this.miglioramento == ((UtenteMiglioramentoKey) obj).miglioramento;
        return false;
    }
}

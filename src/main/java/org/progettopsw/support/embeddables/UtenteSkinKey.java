package org.progettopsw.support.embeddables;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Embeddable
public class UtenteSkinKey implements Serializable
{
    @Column(name = "utente")
    private long utente;

    @Column(name = "skin")
    private long skin;

    @Override
    public int hashCode()
    {
        int M = 31;
        return (int) (utente * M + skin * M);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof UtenteSkinKey)
            return this.utente == ((UtenteSkinKey) obj).utente && this.skin == ((UtenteSkinKey) obj).skin;
        return false;
    }
}

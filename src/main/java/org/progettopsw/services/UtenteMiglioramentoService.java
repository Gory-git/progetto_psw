package org.progettopsw.services;


import jakarta.persistence.LockModeType;
import org.progettopsw.models.Miglioramento;
import org.progettopsw.models.Utente;
import org.progettopsw.models.UtenteMiglioramento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.progettopsw.repositories.UtenteMiglioramentoRepository;
import org.progettopsw.support.exceptions.MiglioramentoDoesNotExistsException;
import org.progettopsw.support.exceptions.MiglioramentoMaxLevelReachedException;

import java.util.List;

@Service
public class UtenteMiglioramentoService
{
    @Autowired
    private UtenteMiglioramentoRepository utenteMiglioramentoRepository;

    @Transactional(readOnly = true)
    public List<Miglioramento> miglioramentiPerUtente(Utente utente)
    {
        return utenteMiglioramentoRepository.findMiglioramentiByUtente(utente);
    }

    @Transactional(readOnly = true)
    public int quantitaPerUtente(Utente utente, Miglioramento miglioramento)
    {
        return utenteMiglioramentoRepository.quantitaMiglioramentoPerUtente(utente, miglioramento);
    }

    @Lock(LockModeType.OPTIMISTIC)
    @Transactional(readOnly = false)
    public void aggiungiMiglioramentoAdUtente(UtenteMiglioramento utenteMiglioramento) throws IllegalArgumentException
    {
        if (utenteMiglioramento == null)
            throw new IllegalArgumentException();
        if (!utenteMiglioramentoRepository.findAll().contains(utenteMiglioramento))
            utenteMiglioramentoRepository.save(utenteMiglioramento);
    }

    @Lock(LockModeType.OPTIMISTIC)
    @Transactional(readOnly = false)
    public void updateQuantitaMiglioramentoAdUtente(UtenteMiglioramento utenteMiglioramento, int quantita) throws IllegalArgumentException, MiglioramentoMaxLevelReachedException
    {
        if (utenteMiglioramento == null || quantita < 0 || !utenteMiglioramentoRepository.findAll().contains(utenteMiglioramento))
            throw new IllegalArgumentException();
        if (utenteMiglioramentoRepository.quantitaMiglioramentoPerUtente(utenteMiglioramento.getUtente(), utenteMiglioramento.getMiglioramento()) + quantita > utenteMiglioramento.getMiglioramento().getQuantita_massima())
            throw new MiglioramentoMaxLevelReachedException();
        utenteMiglioramentoRepository.updateQuantita(quantita, utenteMiglioramento);
    }

    @Transactional(readOnly = true)
    public UtenteMiglioramento miglioramentoUtente(Utente utente, Miglioramento miglioramento)
    {
        return utenteMiglioramentoRepository.findUtenteMiglioramentoByUtenteAndMiglioramento(utente, miglioramento);
    }
}

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
    public void updateQuantitaMiglioramentoAdUtente(Utente utente, Miglioramento miglioramento, int quantita) throws IllegalArgumentException, MiglioramentoDoesNotExistsException, MiglioramentoMaxLevelReachedException
    {
        if (utente == null || miglioramento == null || quantita < 0 || utenteMiglioramentoRepository.findMiglioramentiByUtente(utente).contains(miglioramento))
            throw new IllegalArgumentException();
        if (!utenteMiglioramentoRepository.findMiglioramentiByUtente(utente).contains(miglioramento))
            throw new MiglioramentoDoesNotExistsException();
        if (utenteMiglioramentoRepository.quantitaMiglioramentoPerUtente(utente, miglioramento) + quantita > miglioramento.getQuantita_massima())
            throw new MiglioramentoMaxLevelReachedException();
        utenteMiglioramentoRepository.updateQuantita(quantita, utente, miglioramento);
    }
}

package org.progettopsw.services;


import jakarta.persistence.LockModeType;
import org.progettopsw.models.Miglioramento;
import org.progettopsw.models.Utente;
import org.progettopsw.models.UtenteMiglioramento;
import org.progettopsw.repositories.MiglioramentoRepository;
import org.progettopsw.support.embeddables.UtenteMiglioramentoKey;
import org.progettopsw.support.exceptions.NotEnoughCreditsException;
import org.progettopsw.support.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.progettopsw.repositories.UtenteMiglioramentoRepository;
import org.progettopsw.support.exceptions.MiglioramentoMaxLevelReachedException;

import java.util.List;

@Service
public class UtenteMiglioramentoService
{
    @Autowired
    private UtenteMiglioramentoRepository utenteMiglioramentoRepository;
    @Autowired
    private UtenteService utenteService;
    @Autowired
    private MiglioramentoRepository miglioramentoRepository;

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
    public void updateQuantitaMiglioramentoAdUtente(String nome, int quantita) throws IllegalArgumentException, MiglioramentoMaxLevelReachedException, UserNotFoundException, NotEnoughCreditsException
    {
        Utente utente = utenteService.trovaUtente();
        Miglioramento miglioramento = miglioramentoRepository.findByNome(nome);

        if (quantita > 0)
            utenteService.aggiungiCrediti(utente, - (miglioramento.getCrediti() * quantita));

        if (!miglioramentiPerUtente(utente).contains(miglioramento))
        {
            UtenteMiglioramento utenteMiglioramento = new UtenteMiglioramento();
            UtenteMiglioramentoKey utenteMiglioramentoKey = new UtenteMiglioramentoKey();
            utenteMiglioramentoKey.setMiglioramento(miglioramento.getId());
            utenteMiglioramentoKey.setUtente(utente.getId_utente());
            utenteMiglioramento.setId(utenteMiglioramentoKey);
            utenteMiglioramento.setUtente(utente);
            utenteMiglioramento.setMiglioramento(miglioramento);
            utenteMiglioramento.setQuantita(1);

            aggiungiMiglioramentoAdUtente(utenteMiglioramento);
        }
        if ((utenteMiglioramentoRepository.quantitaMiglioramentoPerUtente(utente, miglioramento) + quantita) > miglioramento.getQuantita_massima())
                throw new MiglioramentoMaxLevelReachedException();
        utenteMiglioramentoRepository.updateQuantita((utenteMiglioramentoRepository.quantitaMiglioramentoPerUtente(utente, miglioramento) + quantita), miglioramentoUtente(utente, miglioramento));
    }

    @Transactional(readOnly = true)
    public UtenteMiglioramento miglioramentoUtente(Utente utente, Miglioramento miglioramento)
    {
        return utenteMiglioramentoRepository.findUtenteMiglioramentoByUtenteAndMiglioramento(utente, miglioramento);
    }
}

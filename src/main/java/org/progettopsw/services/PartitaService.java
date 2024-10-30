package org.progettopsw.services;

import org.progettopsw.models.Miglioramento;
import org.progettopsw.models.Partita;
import org.progettopsw.models.Utente;
import org.progettopsw.support.exceptions.MiglioramentoMaxLevelReachedException;
import org.progettopsw.support.exceptions.NotEnoughCreditsException;
import org.progettopsw.support.exceptions.UserNotFoundException;
import org.progettopsw.support.jwt.CustomJWT;
import org.progettopsw.support.messages.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.progettopsw.repositories.PartitaRepository;
import org.progettopsw.support.exceptions.AlreadySavedGameException;

import java.util.Date;
import java.util.List;

@Service
public class PartitaService
{
    @Autowired
    private PartitaRepository partitaRepository;
    @Autowired
    private UtenteService utenteService;
    @Autowired
    UtenteMiglioramentoService utenteMiglioramentoService;

    @Transactional(readOnly = false)
    public void salvaPartita(int punti) throws AlreadySavedGameException, MiglioramentoMaxLevelReachedException, UserNotFoundException, NotEnoughCreditsException
    {
        int creditiAggiunti = Math.max(punti, 0);
        Utente utente = utenteService.trovaUtente();
        for (Miglioramento miglioramento : utenteMiglioramentoService.miglioramentiPerUtente(utente))
            if (utenteMiglioramentoService.quantitaPerUtente(utente, miglioramento) > 0)
            {
                utenteMiglioramentoService.updateQuantitaMiglioramentoAdUtente(miglioramento.getNome(),- 1);
                switch (miglioramento.getTipologia())
                {
                    case "a" -> creditiAggiunti += (punti / 100) * 5;
                    case "b" -> creditiAggiunti += (punti / 100) * 6;
                    case "c" -> creditiAggiunti += (punti / 100) * 7;
                    case "d" -> creditiAggiunti += (punti / 100) * 8;
                    case "e" -> creditiAggiunti += (punti / 100) * 9;
                }
            }
        Partita partita = new Partita();
        partita.setData_partita(new java.util.Date());
        partita.setEsito(punti == 0 ? "sconfitta" : "vittoria");
        partita.setCrediti_ottenuti(creditiAggiunti);
        partita.setUtente(utente);
        if (partitaRepository.existsById(partita.getId()))
            throw new AlreadySavedGameException();
        partitaRepository.save(partita);

        utenteService.aggiungiCrediti(utente, creditiAggiunti);
    }

    @Transactional(readOnly = true)
    public List<Partita> getPartitePerUtente(Utente utente, int numeroPagina, int dimensionePagina, String sortBy)
    {
        Pageable pageable = PageRequest.of(numeroPagina, dimensionePagina, Sort.by(sortBy));
        return partitaRepository.findByUtenteOrderByDataPartitaDesc(utente, pageable);
    }

    @Transactional(readOnly = true)
    public List<Partita> getPartitePerData(Utente utente, Date data, int numeroPagina, int dimensionePagina, String sortBy)
    {
        Pageable pageable = PageRequest.of(numeroPagina, dimensionePagina, Sort.by(sortBy));
        return partitaRepository.findByUtenteDataPartitaDesc(utente, data, pageable);
    }
    @Transactional(readOnly = true)
    public List<Partita> getPartitePerPunti(Utente utente, int numeroPagina, int dimensionePagina, String sortBy)
    {
        Pageable pageable = PageRequest.of(numeroPagina, dimensionePagina, Sort.by(sortBy));
        return partitaRepository.findByUtenteOrderByDCreditiOttenutiDesc(utente, pageable);
    }
}

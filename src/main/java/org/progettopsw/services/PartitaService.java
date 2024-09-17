package org.progettopsw.services;

import org.progettopsw.models.Partita;
import org.progettopsw.models.Utente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @Transactional(readOnly = true)
    public Partita salvaPartita(Partita partita) throws AlreadySavedGameException
    {
        if (partitaRepository.existsById(partita.getId()))
            throw new AlreadySavedGameException();
        return partitaRepository.save(partita);
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

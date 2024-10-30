package org.progettopsw.services;

import org.progettopsw.models.Miglioramento;
import org.progettopsw.models.Utente;
import org.progettopsw.models.UtenteMiglioramento;
import org.progettopsw.repositories.UtenteMiglioramentoRepository;
import org.progettopsw.support.dto.MiglioramentoDTO;
import org.progettopsw.support.exceptions.MiglioramentoAlreadyExistsException;
import org.progettopsw.support.exceptions.NoMiglioramentiException;
import org.progettopsw.support.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.progettopsw.repositories.MiglioramentoRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class MiglioramentoService
{
    @Autowired
    private MiglioramentoRepository miglioramentoRepository;
    @Autowired
    UtenteService utenteService;
    @Autowired
    UtenteMiglioramentoRepository utenteMiglioramentoRepository;

    @Transactional(readOnly = true)
    public List<MiglioramentoDTO> findAll() throws UserNotFoundException, NoMiglioramentiException
    {
        List<Miglioramento> miglioramenti = miglioramentoRepository.findAllByOrderByNomeAsc();
        if (miglioramenti.isEmpty())
            throw new NoMiglioramentiException();
        Utente utente = utenteService.trovaUtente();
        List<MiglioramentoDTO> ret = new ArrayList<>();
        for (Miglioramento miglioramento : miglioramenti)
        {
            MiglioramentoDTO dto = new MiglioramentoDTO();
            dto.setId(miglioramento.getId());
            dto.setNome(miglioramento.getNome());
            dto.setCrediti(miglioramento.getCrediti());
            dto.setDescrizione(miglioramento.getDescrizione());
            dto.setTipologia(miglioramento.getTipologia());
            dto.setQuantitaMassima(miglioramento.getQuantita_massima());
            UtenteMiglioramento utenteMiglioramento = utenteMiglioramentoRepository.findUtenteMiglioramentoByUtenteAndMiglioramento(utente, miglioramento);
            int posseduti = 0;
            if (utenteMiglioramento != null)
                posseduti = utenteMiglioramento.getQuantita();
            dto.setQuantitaPosseduta(posseduti);
            ret.add(dto);
        }
        return ret;
    }

    @Transactional(readOnly = true)
    public Miglioramento miglioramentoPerNome(String nome)
    {

        return miglioramentoRepository.findByNome(nome);
    }

    @Transactional(readOnly = true)
    public List<MiglioramentoDTO> miglioramentoPerNomeContaining(String nome) throws UserNotFoundException, NoMiglioramentiException
    {
        List<Miglioramento> miglioramenti = miglioramentoRepository.findMiglioramentoByNomeContaining(nome);
        if (miglioramenti == null)
            throw new NoMiglioramentiException();
        Utente utente = utenteService.trovaUtente();
        List<MiglioramentoDTO> ret = new ArrayList<>();
        for(Miglioramento miglioramento : miglioramenti)
        {
            MiglioramentoDTO dto = new MiglioramentoDTO();
            dto.setId(miglioramento.getId());
            dto.setNome(miglioramento.getNome());
            dto.setCrediti(miglioramento.getCrediti());
            dto.setDescrizione(miglioramento.getDescrizione());
            dto.setTipologia(miglioramento.getTipologia());
            dto.setQuantitaMassima(miglioramento.getQuantita_massima());
            UtenteMiglioramento utenteMiglioramento = utenteMiglioramentoRepository.findUtenteMiglioramentoByUtenteAndMiglioramento(utente, miglioramento);
            int posseduti = 0;
            if (utenteMiglioramento != null)
                posseduti = utenteMiglioramento.getQuantita();
            dto.setQuantitaPosseduta(posseduti);
            ret.add(dto);
        }


        return ret;
    }

    @Transactional(readOnly = true)
    public List<MiglioramentoDTO> miglioramentoPerCrediti(int crediti) throws UserNotFoundException, NoMiglioramentiException
    {
        List<Miglioramento> miglioramenti = miglioramentoRepository.findByCrediti(crediti);

        if (miglioramenti.isEmpty())
            throw new NoMiglioramentiException();

        Utente utente = utenteService.trovaUtente();

        List<MiglioramentoDTO> ret = new ArrayList<>();
        for (Miglioramento miglioramento : miglioramenti)
        {
            MiglioramentoDTO dto = new MiglioramentoDTO();
            dto.setId(miglioramento.getId());
            dto.setNome(miglioramento.getNome());
            dto.setCrediti(miglioramento.getCrediti());
            dto.setDescrizione(miglioramento.getDescrizione());
            dto.setTipologia(miglioramento.getTipologia());
            dto.setQuantitaMassima(miglioramento.getQuantita_massima());
            UtenteMiglioramento utenteMiglioramento = utenteMiglioramentoRepository.findUtenteMiglioramentoByUtenteAndMiglioramento(utente, miglioramento);
            int posseduti = 0;
            if (utenteMiglioramento != null)
                posseduti = utenteMiglioramento.getQuantita();
            dto.setQuantitaPosseduta(posseduti);
            ret.add(dto);
        }
        return ret;
    }

    @Transactional(readOnly = false)
    public void nuovoMiglioramento(String nome, String descrizione, String tipologia, int quantitaMassima, int crediti) throws MiglioramentoAlreadyExistsException, IllegalArgumentException
    {
        if (nome.isEmpty())
            throw new IllegalArgumentException("Nome invalido");
        if (miglioramentoRepository.findByNome(nome) != null)
            throw new MiglioramentoAlreadyExistsException();
        if (descrizione.isEmpty())
            throw new IllegalArgumentException("Descrizione invalida");
        if (!tipologia.equalsIgnoreCase("a") && !tipologia.equalsIgnoreCase("b") && !tipologia.equalsIgnoreCase("c") && !tipologia.equalsIgnoreCase("d") && !tipologia.equalsIgnoreCase("e"))
            throw new IllegalArgumentException("Tipologia invalida ");
        if (crediti <= 0)
            throw new IllegalArgumentException("Inserire un valore di crediti maggiore di 0");
        if (quantitaMassima <= 0)
            throw new IllegalArgumentException("Inserire una quantità massima maggiore di 0");

        Miglioramento miglioramento = new Miglioramento();
        miglioramento.setCrediti(crediti);
        miglioramento.setNome(nome);
        miglioramento.setDescrizione(descrizione);
        miglioramento.setTipologia(tipologia.toLowerCase());
        miglioramento.setQuantita_massima(quantitaMassima);
        miglioramentoRepository.save(miglioramento);
    }
}

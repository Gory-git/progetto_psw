package org.progettopsw.services;

import jakarta.persistence.LockModeType;
import org.progettopsw.models.Utente;
import org.progettopsw.support.dto.UtenteDTO;
import org.progettopsw.support.jwt.CustomJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.progettopsw.repositories.UtenteRepository;
import org.progettopsw.support.exceptions.NotEnoughCreditsException;
import org.progettopsw.support.exceptions.UserAlreadyExistsException;
import org.progettopsw.support.exceptions.UserNotFoundException;

@Service
public class UtenteService
{
    @Autowired
    private UtenteRepository utenteRepository;

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Utente registraUtente() throws UserAlreadyExistsException
    {
        CustomJWT cJWT = (CustomJWT) SecurityContextHolder.getContext().getAuthentication();

        if(utenteRepository.existsByEmail(cJWT.getEmail()))
            throw new UserAlreadyExistsException();

        Utente utente = new Utente();
        utente.setEmail(cJWT.getEmail());
        utente.setNome(cJWT.getNome());
        utente.setCognome(cJWT.getCognome());
        utente.setCrediti(0);

        return utenteRepository.save(utente);
    }

    @Lock(LockModeType.OPTIMISTIC)
    @Transactional(readOnly = false)
    public void aggiungiCrediti(Utente utente, int crediti) throws UserNotFoundException, NotEnoughCreditsException
    {
        if (utente == null || !utenteRepository.existsById(utente.getId_utente()))
            throw new UserNotFoundException();
        if (utente.getCrediti() + crediti < 0)
            throw new NotEnoughCreditsException();
        utenteRepository.updateCrediti(utente, utente.getCrediti() + crediti);
    }

    @Transactional(readOnly = false)
    public Utente trovaUtente() throws UserNotFoundException
    {

        CustomJWT cJWT = (CustomJWT) SecurityContextHolder.getContext().getAuthentication();

        Utente utente = utenteRepository.findByEmailIgnoreCase(cJWT.getEmail());
        if (utente == null)
            throw new UserNotFoundException();
        return utente;
    }

    @Transactional(readOnly = true)
    public UtenteDTO getDTO() throws UserNotFoundException
    {
        CustomJWT cJWT = (CustomJWT) SecurityContextHolder.getContext().getAuthentication();

        Utente utente = trovaUtente();
        UtenteDTO ret = new UtenteDTO();
        ret.setId(utente.getId_utente());
        ret.setEmail(utente.getEmail());
        ret.setNome(utente.getNome());
        ret.setCognome(utente.getCognome());
        ret.setCrediti(utente.getCrediti());
        ret.setRuolo(cJWT.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_admin")));
        return ret;
    }
}

package org.progettopsw.services;

import jakarta.persistence.LockModeType;
import org.progettopsw.models.Utente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
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
    public Utente registraUtente(Utente utente) throws UserAlreadyExistsException
    {
        if(utenteRepository.existsByEmail(utente.getEmail()))
            throw new UserAlreadyExistsException();
        return utenteRepository.save(utente);
    }

    @Lock(LockModeType.OPTIMISTIC)
    @Transactional(readOnly = false)
    public void agiornaCrediti(Utente utente, int crediti) throws UserNotFoundException, NotEnoughCreditsException
    {
        if (utente == null || !utenteRepository.existsById(utente.getId_utente()))
            throw new UserNotFoundException();
        if (utente.getCrediti() + crediti < 0)
            throw new NotEnoughCreditsException();
        utenteRepository.updateCrediti(utente, utente.getCrediti() + crediti);
    }

    @Transactional(readOnly = false)
    public Utente trovaUtente(String email) throws UserNotFoundException
    {
        Utente ret = utenteRepository.findByEmailIgnoreCase(email);
        if (ret == null)
            throw new UserNotFoundException();
        return ret;
    }

    @Transactional(readOnly = false)
    public void nuovoUtente(Utente utente) throws UserAlreadyExistsException {
        if (utenteRepository.existsByEmail(utente.getEmail()))
            throw new UserAlreadyExistsException();
        utenteRepository.save(utente);
    }
}

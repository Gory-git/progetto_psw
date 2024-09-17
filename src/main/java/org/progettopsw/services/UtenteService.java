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

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Utente accediUtente(Utente utente) throws UserNotFoundException
    {
        Utente u = utenteRepository.findByEmailIgnoreCaseAndPassword(utente.getEmail(), utente.getPassword());
        if (u == null)
            throw new UserNotFoundException();
        return u;
    }
    @Lock(LockModeType.OPTIMISTIC)
    @Transactional(readOnly = false)
    public void agiornaCrediti(Utente utente, double crediti) throws UserNotFoundException, NotEnoughCreditsException
    {
        if (utente == null || !utenteRepository.existsById(utente.getId_utente()))
            throw new UserNotFoundException();
        if (utente.getCrediti() + crediti < 0)
            throw new NotEnoughCreditsException();
        utenteRepository.updateCrediti(utente, crediti);
    }
}

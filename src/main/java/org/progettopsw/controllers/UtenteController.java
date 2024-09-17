package org.progettopsw.controllers;

import org.progettopsw.models.Utente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.progettopsw.services.UtenteService;
import org.progettopsw.support.exceptions.UserAlreadyExistsException;
import org.progettopsw.support.exceptions.UserNotFoundException;

@RestController
@RequestMapping(value = "/user")
public class UtenteController
{
    @Autowired
    private UtenteService utenteService;

    @GetMapping("/register")
    public ResponseEntity registerUtente(Utente utente) throws UserAlreadyExistsException
    {
        utenteService.registraUtente(utente);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/login")
    public ResponseEntity loginUtente(Utente utente) throws UserNotFoundException
    {
        utenteService.accediUtente(utente);
        return new ResponseEntity<>(HttpStatus.FOUND);
    }

    // TODO mostrare i miglioramenti e le skin possedute
}

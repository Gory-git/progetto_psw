package org.progettopsw.controllers;

import org.progettopsw.models.Utente;
import org.progettopsw.support.dto.UtenteDTO;
import org.progettopsw.support.jwt.CustomJWT;
import org.progettopsw.support.jwt.CustomJWTConverter;
import org.progettopsw.support.messages.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.progettopsw.services.UtenteService;
import org.progettopsw.support.exceptions.UserAlreadyExistsException;
import org.progettopsw.support.exceptions.UserNotFoundException;

import javax.validation.Valid;
import java.util.ArrayList;

@RestController
@RequestMapping(value = "/user")
public class UtenteController
{
    @Autowired
    private UtenteService utenteService;

    @PostMapping("/register")
    public ResponseEntity registerUtente()
    {
        try
        {
            CustomJWT cJWT = (CustomJWT) SecurityContextHolder.getContext().getAuthentication();
            if (cJWT == null)
                return new ResponseEntity<>(new ResponseMessage("JWT error!"), HttpStatus.UNAUTHORIZED);
            Utente utente = new Utente();
            utente.setEmail(cJWT.getEmail());
            utente.setNome(cJWT.getNome());
            utente.setCognome(cJWT.getCognome());
            utente.setCrediti(0);
            utente.setMiglioramenti(new ArrayList<>());
            utente.setSkin(new ArrayList<>());
            utenteService.registraUtente(utente);

            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (UserAlreadyExistsException e)
        {
            return new ResponseEntity<>(new ResponseMessage("User already exists"), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/page")
    public ResponseEntity infoUtente()
    {
        try
        {
            CustomJWT cJWT = (CustomJWT) SecurityContextHolder.getContext().getAuthentication();
            if (cJWT == null)
                return new ResponseEntity<>(new ResponseMessage("JWT error!"), HttpStatus.UNAUTHORIZED);
            
            Utente utente = utenteService.trovaUtente(cJWT.getEmail());


            UtenteDTO ret = new UtenteDTO();
            
            ret.setId(utente.getId_utente());
            ret.setEmail(utente.getEmail());
            ret.setNome(utente.getNome());
            ret.setCognome(utente.getCognome());
            ret.setCrediti(utente.getCrediti());

            return new ResponseEntity<>(ret, HttpStatus.FOUND);
        } catch (UserNotFoundException e)
        {
            return new ResponseEntity<>(new ResponseMessage("User don't exists"), HttpStatus.NOT_FOUND);
        }
    }

}

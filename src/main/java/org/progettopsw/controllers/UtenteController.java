package org.progettopsw.controllers;

import org.progettopsw.models.Utente;
import org.progettopsw.support.dto.UtenteDTO;
import org.progettopsw.support.jwt.CustomJWT;
import org.progettopsw.support.jwt.CustomJWTConverter;
import org.progettopsw.support.messages.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.progettopsw.services.UtenteService;
import org.progettopsw.support.exceptions.UserAlreadyExistsException;
import org.progettopsw.support.exceptions.UserNotFoundException;

import javax.validation.Valid;
import java.util.ArrayList;

@RestController
@RequestMapping(value = "/user")
@CrossOrigin(
        origins = "http://localhost:4200",
        allowedHeaders = "*",
        methods = { RequestMethod.GET, RequestMethod.POST }
)
public class UtenteController
{
    @Autowired
    private UtenteService utenteService;

    @PostMapping("/register")
    @PreAuthorize("hasAnyRole('ROLE_user','ROLE_admin')")
    public ResponseEntity registerUtente()
    {
        CustomJWT cJWT = (CustomJWT) SecurityContextHolder.getContext().getAuthentication();
        try
        {
            if (utenteService.trovaUtente(cJWT.getEmail()) != null)
                return new ResponseEntity<>(new ResponseMessage("Utente already exists"), HttpStatus.OK);

        }catch (UserNotFoundException e)
        {
            try
            {
                Utente utente = new Utente();
                utente.setEmail(cJWT.getEmail());
                utente.setNome(cJWT.getNome());
                utente.setCognome(cJWT.getCognome());
                utente.setCrediti(0);
                //utente.setMiglioramenti(new ArrayList<>());
                //utente.setSkin(new ArrayList<>());
                utenteService.registraUtente(utente);

                return new ResponseEntity<>(HttpStatus.OK);
            } catch (UserAlreadyExistsException ex)
            {
                return new ResponseEntity<>(new ResponseMessage("User already exists"), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(new ResponseMessage("Error!"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/page")
    @PreAuthorize("hasAnyRole('ROLE_user','ROLE_admin')")
    public ResponseEntity infoUtente()
    {
        try
        {
            CustomJWT cJWT = (CustomJWT) SecurityContextHolder.getContext().getAuthentication();
            
            Utente utente = utenteService.trovaUtente(cJWT.getEmail());


            UtenteDTO ret = new UtenteDTO();
            
            ret.setId(utente.getId_utente());
            ret.setEmail(utente.getEmail());
            ret.setNome(utente.getNome());
            ret.setCognome(utente.getCognome());
            ret.setCrediti(utente.getCrediti());

            return new ResponseEntity<>(ret, HttpStatus.OK);
        } catch (UserNotFoundException e)
        {
            return new ResponseEntity<>(new ResponseMessage("User don't exists"), HttpStatus.NOT_FOUND);
        }
    }

}

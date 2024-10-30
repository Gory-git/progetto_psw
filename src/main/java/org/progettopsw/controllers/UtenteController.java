package org.progettopsw.controllers;

import org.progettopsw.support.messages.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.progettopsw.services.UtenteService;
import org.progettopsw.support.exceptions.UserAlreadyExistsException;
import org.progettopsw.support.exceptions.UserNotFoundException;

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
        try
        {
            utenteService.registraUtente();

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (UserAlreadyExistsException e)
        {
            return new ResponseEntity<>(new ResponseMessage("User already exists"), HttpStatus.OK);
        }
    }

    @GetMapping("/page")
    @PreAuthorize("hasAnyRole('ROLE_user','ROLE_admin')")
    public ResponseEntity infoUtente()
    {
        try
        {
            return new ResponseEntity<>(utenteService.getDTO(), HttpStatus.OK);
        } catch (UserNotFoundException e)
        {
            return new ResponseEntity<>(new ResponseMessage("User don't exists"), HttpStatus.NOT_FOUND);
        }
    }

}

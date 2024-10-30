package org.progettopsw.controllers;

import org.progettopsw.services.PartitaService;
import org.progettopsw.support.exceptions.AlreadySavedGameException;
import org.progettopsw.support.exceptions.MiglioramentoMaxLevelReachedException;
import org.progettopsw.support.exceptions.NotEnoughCreditsException;
import org.progettopsw.support.exceptions.UserNotFoundException;
import org.progettopsw.support.messages.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/game")
public class GameController
{
    @Autowired
    private PartitaService partitaService;

    @PostMapping("/save")
    @PreAuthorize("hasAnyRole('ROLE_user','ROLE_admin')")
    public ResponseEntity salvaPartita(@Valid int punti)
    {
        try {

            partitaService.salvaPartita(punti);

            return new ResponseEntity<>(new ResponseMessage("Partita saved"), HttpStatus.OK);
        } catch (UserNotFoundException e)
        {
            return new ResponseEntity<>(new ResponseMessage("User don't exists"), HttpStatus.NOT_FOUND);
        } catch (MiglioramentoMaxLevelReachedException | NotEnoughCreditsException e)
        {
            return new ResponseEntity<>(new ResponseMessage("Unexpected error"), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (AlreadySavedGameException e)
        {
            return new ResponseEntity<>(new ResponseMessage("Game already saved"), HttpStatus.BAD_REQUEST);
        }
    }
}

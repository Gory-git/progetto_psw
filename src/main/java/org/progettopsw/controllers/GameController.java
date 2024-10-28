package org.progettopsw.controllers;

import org.progettopsw.models.Miglioramento;
import org.progettopsw.models.Partita;
import org.progettopsw.models.Utente;
import org.progettopsw.services.PartitaService;
import org.progettopsw.support.exceptions.AlreadySavedGameException;
import org.progettopsw.support.exceptions.MiglioramentoMaxLevelReachedException;
import org.progettopsw.support.exceptions.NotEnoughCreditsException;
import org.progettopsw.support.exceptions.UserNotFoundException;
import org.progettopsw.support.jwt.CustomJWT;
import org.progettopsw.support.messages.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.progettopsw.services.UtenteMiglioramentoService;
import org.progettopsw.services.UtenteService;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/game")
public class GameController
{
    @Autowired
    private UtenteService utenteService;
    @Autowired
    private UtenteMiglioramentoService utenteMiglioramentoService;
    @Autowired
    private PartitaService partitaService;

    @PostMapping("/save")
    @PreAuthorize("hasAnyRole('ROLE_user','ROLE_admin')")
    public ResponseEntity salvaPartita(@Valid int punti)
    {
        try {
            int creditiAggiunti = Math.max(punti, 0);
            CustomJWT cJWT = (CustomJWT) SecurityContextHolder.getContext().getAuthentication();
            if (cJWT == null)
                return new ResponseEntity<>(new ResponseMessage("JWT error!"), HttpStatus.OK);
            Utente utente = utenteService.trovaUtente(cJWT.getEmail());
            for (Miglioramento miglioramento : utenteMiglioramentoService.miglioramentiPerUtente(utente))
                if (utenteMiglioramentoService.quantitaPerUtente(utente, miglioramento) > 0)
                {
                    utenteMiglioramentoService.updateQuantitaMiglioramentoAdUtente(utenteMiglioramentoService.miglioramentoUtente(utente, miglioramento) ,- 1);
                    switch (miglioramento.getTipologia())
                    {
                        case "a" -> creditiAggiunti += (punti / 100) * 5;
                        case "b" -> creditiAggiunti += (punti / 100) * 6;
                        case "c" -> creditiAggiunti += (punti / 100) * 7;
                        case "d" -> creditiAggiunti += (punti / 100) * 8;
                        case "e" -> creditiAggiunti += (punti / 100) * 9;
                    }
                }
            Partita partita = new Partita();
            partita.setData_partita(new java.util.Date());
            partita.setEsito(punti == 0 ? "sconfitta" : "vittoria");
            partita.setCrediti_ottenuti(creditiAggiunti);
            partita.setUtente(utente);
            partitaService.salvaPartita(partita);

            utenteService.aggiungiCrediti(utente, creditiAggiunti);

            return new ResponseEntity<>(new ResponseMessage("Partita saved"), HttpStatus.OK);
        } catch (UserNotFoundException e)
        {
            return new ResponseEntity<>(new ResponseMessage("User don't exists"), HttpStatus.NOT_FOUND);
        } catch (NotEnoughCreditsException | MiglioramentoMaxLevelReachedException e)
        {
            return new ResponseEntity<>(new ResponseMessage("wtf"), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (AlreadySavedGameException e)
        {
            return new ResponseEntity<>(new ResponseMessage("Game already saved"), HttpStatus.BAD_REQUEST);
        }
    }
}

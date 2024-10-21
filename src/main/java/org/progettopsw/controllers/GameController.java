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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.progettopsw.services.UtenteMiglioramentoService;
import org.progettopsw.services.UtenteService;
import java.sql.Date;

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
    public ResponseEntity salvaPartita(int puntiAcquisiti){
        try {
            int creditiAggiunti = Math.max(puntiAcquisiti, 0);
            CustomJWT cJWT = (CustomJWT) SecurityContextHolder.getContext().getAuthentication();
            if (cJWT == null)
                return new ResponseEntity<>(new ResponseMessage("JWT error!"), HttpStatus.OK);
            Utente utente = utenteService.trovaUtente(cJWT.getEmail());
            for (Miglioramento miglioramento : utenteMiglioramentoService.miglioramentiPerUtente(utente))
                if (miglioramento != null && utenteMiglioramentoService.quantitaPerUtente(utente, miglioramento) > 0) {
                    utenteMiglioramentoService.updateQuantitaMiglioramentoAdUtente(utente, miglioramento, (utenteMiglioramentoService.quantitaPerUtente(utente, miglioramento) - 1));
                    switch (miglioramento.getTipologia()) {
                        case "a" -> creditiAggiunti += (puntiAcquisiti / 100) * 5;
                        case "b" -> creditiAggiunti += (puntiAcquisiti / 100) * 10;
                        case "c" -> creditiAggiunti += (puntiAcquisiti / 100) * 15;
                        case "d" -> creditiAggiunti += (puntiAcquisiti / 100) * 20;
                        case "e" -> creditiAggiunti += (puntiAcquisiti / 100) * 25;
                    }
                }
            utenteService.agiornaCrediti(utente, (utente.getCrediti() + creditiAggiunti));
            Partita partita = new Partita();
            partita.setData_partita(Date.valueOf(String.valueOf(new java.util.Date())));
            partita.setEsito(puntiAcquisiti == 0 ? "sconfitta" : "vittoria");
            partita.setCrediti_ottenuti(creditiAggiunti);
            partita.setUtente(utente);
            partitaService.salvaPartita(partita);

            return new ResponseEntity<>(new ResponseMessage("Partita saved"), HttpStatus.CREATED);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(new ResponseMessage("User don't exists"), HttpStatus.NOT_FOUND);
        } catch (NotEnoughCreditsException | MiglioramentoMaxLevelReachedException e) {
            return new ResponseEntity<>(new ResponseMessage("wtf"), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (AlreadySavedGameException e) {
            return new ResponseEntity<>(new ResponseMessage("Game already saved"), HttpStatus.BAD_REQUEST);
        }
    }
}

package org.progettopsw.controllers;

import org.progettopsw.models.Miglioramento;
import org.progettopsw.models.Utente;
import org.progettopsw.support.exceptions.MiglioramentoDoesNotExistsException;
import org.progettopsw.support.exceptions.MiglioramentoMaxLevelReachedException;
import org.progettopsw.support.exceptions.NotEnoughCreditsException;
import org.progettopsw.support.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.progettopsw.services.UtenteMiglioramentoService;
import org.progettopsw.services.UtenteService;
import org.progettopsw.services.UtenteSkinService;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/game")
public class GameController
{
    @Autowired
    private UtenteService utenteService;
    @Autowired
    private UtenteMiglioramentoService utenteMiglioramentoService;
/*
    @PostMapping("/start")
    public void avviaPartita(@Valid @RequestBody Utente utente)
    {

    }
* TODO capire se ha senso
*/
    @PostMapping("/save")
    public void salvaPartita(@Valid @RequestBody Utente utente, int puntiAcquisiti) throws MiglioramentoMaxLevelReachedException, MiglioramentoDoesNotExistsException, UserNotFoundException, NotEnoughCreditsException {
        double creditiAggiunti = puntiAcquisiti;
        for (Miglioramento miglioramento : utenteMiglioramentoService.miglioramentiPerUtente(utente))
            if (miglioramento != null && utenteMiglioramentoService.quantitaPerUtente(utente, miglioramento) > 0)
            {
                utenteMiglioramentoService.updateQuantitaMiglioramentoAdUtente(utente, miglioramento, (utenteMiglioramentoService.quantitaPerUtente(utente, miglioramento) - 1));
                switch (miglioramento.getTipologia())
                {
                    case "a" -> creditiAggiunti += ((double) puntiAcquisiti /100)*5;
                    case "b" -> creditiAggiunti += ((double) puntiAcquisiti /100)*10;
                    case "c" -> creditiAggiunti += ((double) puntiAcquisiti /100)*15;
                    case "d" -> creditiAggiunti += ((double) puntiAcquisiti /100)*20;
                    case "e" -> creditiAggiunti += ((double) puntiAcquisiti /100)*25;
                }
            }
        utenteService.agiornaCrediti(utente, (utente.getCrediti() + creditiAggiunti));
    }
}

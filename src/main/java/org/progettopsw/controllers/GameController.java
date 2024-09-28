package org.progettopsw.controllers;

import org.progettopsw.models.Miglioramento;
import org.progettopsw.models.Partita;
import org.progettopsw.models.Utente;
import org.progettopsw.services.PartitaService;
import org.progettopsw.support.exceptions.*;
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
    @Autowired
    private PartitaService partitaService;
/*
    @PostMapping("/start")
    public void avviaPartita(@Valid @RequestBody Utente utente)
    {

    }
* TODO capire se ha senso
*/
    @PostMapping("/save")
    public void salvaPartita(@Valid @RequestBody Utente utente, @Valid @RequestBody Partita partita) throws MiglioramentoMaxLevelReachedException, MiglioramentoDoesNotExistsException, UserNotFoundException, NotEnoughCreditsException, AlreadySavedGameException {
        double puntiAcquisiti = partita.getCrediti_ottenuti();
        double creditiAggiunti = puntiAcquisiti;
        for (Miglioramento miglioramento : utenteMiglioramentoService.miglioramentiPerUtente(utente))
            if (miglioramento != null && utenteMiglioramentoService.quantitaPerUtente(utente, miglioramento) > 0)
            {
                utenteMiglioramentoService.updateQuantitaMiglioramentoAdUtente(utente, miglioramento, (utenteMiglioramentoService.quantitaPerUtente(utente, miglioramento) - 1));
                switch (miglioramento.getTipologia())
                {
                    case "a" -> creditiAggiunti += (puntiAcquisiti /100)*5;
                    case "b" -> creditiAggiunti += (puntiAcquisiti /100)*10;
                    case "c" -> creditiAggiunti += (puntiAcquisiti /100)*15;
                    case "d" -> creditiAggiunti += (puntiAcquisiti /100)*20;
                    case "e" -> creditiAggiunti += (puntiAcquisiti /100)*25;
                }
            }
        utenteService.agiornaCrediti(utente, (utente.getCrediti() + creditiAggiunti));
        partitaService.salvaPartita(partita);
        // FIXME sistemare le eccezioni
    }
}

package org.progettopsw.controllers;

import org.progettopsw.models.Utente;
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
    private UtenteSkinService utenteSkinService;

    @PostMapping("/start")
    public void avviaPartita(@Valid @RequestBody Utente utente)
    {

    }

    @PostMapping("/end")
    public void salvaPartita(@Valid @RequestBody Utente utente, int creditiAcquisiti)
    {

    }
}

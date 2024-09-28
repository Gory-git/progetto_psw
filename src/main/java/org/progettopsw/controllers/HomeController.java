package org.progettopsw.controllers;

import org.progettopsw.services.PartitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class HomeController
{
    @Autowired
    private PartitaService partitaService;

    // TODO sistemare le partite con maggiori punti
}

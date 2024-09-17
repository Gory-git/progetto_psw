package org.progettopsw.controllers;

import org.progettopsw.models.Miglioramento;
import org.progettopsw.models.UtenteMiglioramento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.progettopsw.services.MiglioramentoService;
import org.progettopsw.services.UtenteMiglioramentoService;
import org.progettopsw.services.UtenteService;
import org.progettopsw.support.exceptions.MiglioramentoDoesNotExistsException;
import org.progettopsw.support.exceptions.MiglioramentoMaxLevelReachedException;
import org.progettopsw.support.exceptions.NotEnoughCreditsException;
import org.progettopsw.support.exceptions.UserNotFoundException;
import org.progettopsw.support.messages.ResponseMessage;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/miglioramenti")
public class MiglioramentoController
{
    @Autowired
    private MiglioramentoService miglioramentoService;
    @Autowired
    private UtenteMiglioramentoService utenteMiglioramentoService;
    @Autowired
    private UtenteService utenteService;

    @GetMapping("/all")
    public List<Miglioramento> getMiglioramentiDisponibili()
    {
        return miglioramentoService.findAll();
    }

    @GetMapping("/{nome}")
    public List<Miglioramento> getMiglioramentoByNome(String nome)
    {
        return miglioramentoService.miglioramentoPerNome(nome);
    }

    @GetMapping("{crediti}")
    public List<Miglioramento> getMiglioramentoByCrediti(int crediti)
    {
        return miglioramentoService.miglioramentoPerCrediti(crediti);
    }

    @GetMapping("/acquire")
    public ResponseEntity acquireMiglioramento(@Valid @RequestBody UtenteMiglioramento utenteMiglioramento)
    {
        try
        {
            if (!utenteMiglioramentoService.miglioramentiPerUtente(utenteMiglioramento.getUtente()).contains(utenteMiglioramento.getMiglioramento()))
                utenteMiglioramentoService.aggiungiMiglioramentoAdUtente(utenteMiglioramento);
            else
                utenteMiglioramentoService.updateQuantitaMiglioramentoAdUtente(utenteMiglioramento.getUtente(), utenteMiglioramento.getMiglioramento(), utenteMiglioramento.getQuantita());
            utenteService.agiornaCrediti(utenteMiglioramento.getUtente(), -utenteMiglioramento.getMiglioramento().getCrediti());

        } catch (UserNotFoundException e)
        {
            return new ResponseEntity<>(new ResponseMessage("User doesn't exists"), HttpStatus.BAD_REQUEST);
        } catch (NotEnoughCreditsException e)
        {
            return new ResponseEntity<>(new ResponseMessage("Not enough credits"), HttpStatus.BAD_REQUEST);
        } catch (MiglioramentoMaxLevelReachedException e)
        {
            return new ResponseEntity<>(new ResponseMessage("You can't buy this much miglioramento!"), HttpStatus.BAD_REQUEST);
        } catch (MiglioramentoDoesNotExistsException e)
        {
            return new ResponseEntity<>(new ResponseMessage("Miglioramento doesn't exists"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ResponseMessage("Miglioramento acquired!"), HttpStatus.CREATED);
    }
}

package org.progettopsw.controllers;

import org.progettopsw.support.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.progettopsw.services.MiglioramentoService;
import org.progettopsw.services.UtenteMiglioramentoService;
import org.progettopsw.support.messages.ResponseMessage;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/miglioramenti")
@CrossOrigin(
        origins = "http://localhost:4200",
        allowedHeaders = "*",
        methods = { RequestMethod.GET, RequestMethod.POST }
)
public class MiglioramentoController
{
    @Autowired
    private MiglioramentoService miglioramentoService;
    @Autowired
    private UtenteMiglioramentoService utenteMiglioramentoService;

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ROLE_user','ROLE_admin')")
    @Transactional
    public ResponseEntity getMiglioramentiDisponibili()
    {
        try
        {
            return new ResponseEntity<>(miglioramentoService.findAll(), HttpStatus.OK);
        } catch (UserNotFoundException e)
        {
            return new ResponseEntity<>(new ResponseMessage("User doesn't exists"), HttpStatus.BAD_REQUEST);
        } catch (NoMiglioramentiException e)
        {
            return new ResponseEntity<>(new ResponseMessage("No results!"), HttpStatus.OK);
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAnyRole('ROLE_user','ROLE_admin')")
    public ResponseEntity getMiglioramentoByNome(@RequestParam(value="nome") String nome)
    {
        try
        {
            return new ResponseEntity<>(miglioramentoService.miglioramentoPerNomeContaining(nome), HttpStatus.OK);
        } catch (UserNotFoundException e)
        {
            return new ResponseEntity<>(new ResponseMessage("User doesn't exists"), HttpStatus.BAD_REQUEST);
        } catch (NoMiglioramentiException e)
        {
            return new ResponseEntity<>(new ResponseMessage("No results!"), HttpStatus.OK);
        }
    }

    @GetMapping("/search/{crediti}")
    @PreAuthorize("hasAnyRole('ROLE_user','ROLE_admin')")
    public ResponseEntity getMiglioramentoByCrediti(@PathVariable("crediti") int crediti)
    {
        try
        {
            return new ResponseEntity<>(miglioramentoService.miglioramentoPerCrediti(crediti), HttpStatus.OK);
        } catch (UserNotFoundException e)
        {
            return new ResponseEntity<>(new ResponseMessage("User doesn't exists"), HttpStatus.BAD_REQUEST);
        } catch (NoMiglioramentiException e)
        {
            return new ResponseEntity<>(new ResponseMessage("No results!"), HttpStatus.OK);
        }
    }

    @PostMapping("/acquire")
    @PreAuthorize("hasAnyRole('ROLE_user','ROLE_admin')")
    public ResponseEntity acquireMiglioramento(@Valid @RequestParam("nome") String nome)
    {
        try
        {
            utenteMiglioramentoService.updateQuantitaMiglioramentoAdUtente(nome, 1);

            return new ResponseEntity<>(new ResponseMessage("Miglioramento acquired!"), HttpStatus.OK);

        } catch (UserNotFoundException e)
        {
            return new ResponseEntity<>(new ResponseMessage("User doesn't exists"), HttpStatus.BAD_REQUEST);
        } catch (NotEnoughCreditsException e)
        {
            return new ResponseEntity<>(new ResponseMessage("Not enough credits"), HttpStatus.BAD_REQUEST);
        } catch (MiglioramentoMaxLevelReachedException e)
        {
            return new ResponseEntity<>(new ResponseMessage("You can't buy this much miglioramento!"), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/save")
    @PreAuthorize("hasAuthority('ROLE_admin')")
    public ResponseEntity salvaMiglioramento(@Valid @RequestParam("nome") String nome, @Valid @RequestParam("descrizione") String descrizione, @Valid @RequestParam("tipologia") String tipologia, @Valid @RequestParam("quantitaMassima") int quantitaMassima, @Valid @RequestParam("crediti") int crediti)
    {
        try
        {
            miglioramentoService.nuovoMiglioramento(nome, descrizione, tipologia, quantitaMassima, crediti);
            return new ResponseEntity<>(new ResponseMessage("Miglioramento added!"), HttpStatus.CREATED);
        } catch (MiglioramentoAlreadyExistsException e)
        {
            return new ResponseEntity<>(new ResponseMessage("Miglioramento already exists"), HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e)
        {
            return new ResponseEntity<>(new ResponseMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}

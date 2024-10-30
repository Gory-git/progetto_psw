package org.progettopsw.controllers;

import org.progettopsw.support.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.progettopsw.services.SkinService;
import org.progettopsw.services.UtenteSkinService;
import org.progettopsw.support.messages.ResponseMessage;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/skins")
@CrossOrigin(
        origins = "http://localhost:4200",
        allowedHeaders = "*",
        methods = { RequestMethod.GET, RequestMethod.POST }
)
public class SkinController
{
    @Autowired
    private SkinService skinService;
    @Autowired
    private UtenteSkinService utenteSkinService;
    @GetMapping("/owned")
    @PreAuthorize("hasAnyRole('ROLE_user','ROLE_admin')")
    public ResponseEntity getSkinPossedute()
    {
        try
        {
            return new ResponseEntity<>(utenteSkinService.getUtenteSkin(), HttpStatus.OK);
        } catch (UserNotFoundException e)
        {
            return new ResponseEntity<>(new ResponseMessage("Uer not found!"), HttpStatus.NOT_FOUND);
        } catch (NoSkinsException e)
        {
            return new ResponseEntity<>(new ResponseMessage("No results!"), HttpStatus.OK);
        }
    }

    @GetMapping("/notowned")
    @PreAuthorize("hasAnyRole('ROLE_user','ROLE_admin')")
    public ResponseEntity getSkinNonPossedute()
    {
        try
        {
            return new ResponseEntity<>(utenteSkinService.getSkinNonPossedute(), HttpStatus.OK);
        } catch (UserNotFoundException e)
        {
            return new ResponseEntity<>(new ResponseMessage("Uer not found!"), HttpStatus.NOT_FOUND);
        } catch (NoSkinsException e)
        {
            return new ResponseEntity<>(new ResponseMessage("No results!"), HttpStatus.OK);
        }
    }

    @PostMapping("/acquire")
    @PreAuthorize("hasAnyRole('ROLE_user','ROLE_admin')")
    public ResponseEntity acquireSkin(@Valid @RequestParam("nome") String nome)
    {
        try
        {
            utenteSkinService.aggiungiSkinUtente(nome);
            return new ResponseEntity<>(new ResponseMessage("Skin acquired!"), HttpStatus.OK);
        }catch (SkinAlreadyOwnedException e)
        {
            return new ResponseEntity<>(new ResponseMessage("You already own this skin!"), HttpStatus.BAD_REQUEST);
        } catch (UserNotFoundException e)
        {
            return new ResponseEntity<>(new ResponseMessage("User don't exists"), HttpStatus.NOT_FOUND);
        } catch (NotEnoughCreditsException e)
        {
            return new ResponseEntity<>(new ResponseMessage("Not enough credits"), HttpStatus.BAD_REQUEST);
        } catch (SkinDoesNotExistsException e)
        {
            return new ResponseEntity<>(new ResponseMessage("Skin don't exists"), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/save")
    @PreAuthorize("hasAuthority('ROLE_admin')")
    public ResponseEntity salvaSkin(@Valid @RequestParam("nome") String nome, @Valid @RequestParam("crediti") int crediti)
    {
        try
        {
            skinService.nuovaSkin(nome, crediti);
            return new ResponseEntity<>(new ResponseMessage("Skin added!"), HttpStatus.CREATED);
        } catch (SkinAlreadyExistsException e)
        {
            return new ResponseEntity<>(new ResponseMessage("Skin already exists"), HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e)
        {
            return new ResponseEntity<>(new ResponseMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

}

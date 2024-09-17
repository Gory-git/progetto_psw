package org.progettopsw.controllers;

import org.progettopsw.models.Skin;
import org.progettopsw.models.Utente;
import org.progettopsw.models.UtenteSkin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.progettopsw.services.SkinService;
import org.progettopsw.services.UtenteService;
import org.progettopsw.services.UtenteSkinService;
import org.progettopsw.support.exceptions.NotEnoughCreditsException;
import org.progettopsw.support.exceptions.SkinAlreadyOwnedException;
import org.progettopsw.support.exceptions.UserNotFoundException;
import org.progettopsw.support.messages.ResponseMessage;

import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping(value = "/skins")
public class SkinController
{
    @Autowired
    private SkinService skinService;
    @Autowired
    private UtenteSkinService utenteSkinService;
    @Autowired
    private UtenteService utenteService;

    @GetMapping("/owned")
    public ResponseEntity getSkinPossedute(@Valid @RequestBody Utente utente)
    {
        List<Skin> ret = utenteSkinService.getUtenteSkin(utente);
        if (ret.isEmpty())
            return new ResponseEntity<>(new ResponseMessage("No results!"), HttpStatus.OK);
        return new ResponseEntity<>(ret, HttpStatus.OK);
    }

    @GetMapping("/notowned")
    public ResponseEntity getSkinNonPossedute(@Valid @RequestBody Utente utente)
    {
        List<Skin> possedute = utenteSkinService.getUtenteSkin(utente);
        List<Skin> tutte = skinService.getAll();
        List<Skin> ret = new LinkedList<>();
        for (Skin skin : tutte)
            if (!possedute.contains(skin))
                ret.add(skin);
        if (ret.isEmpty())
            return new ResponseEntity<>(new ResponseMessage("No results!"), HttpStatus.OK);
        return new ResponseEntity<>(ret, HttpStatus.OK);
    }

    @PostMapping("/acquire")
    public ResponseEntity acquireSkin(@Valid @RequestBody UtenteSkin utenteSkin)
    {
        try
        {
            utenteSkinService.aggiungiSkinUtente(utenteSkin);
            utenteService.agiornaCrediti(utenteSkin.getUtente(), -utenteSkin.getSkin().getCrediti());
        }catch (SkinAlreadyOwnedException e)
        {
            return new ResponseEntity<>(new ResponseMessage("You already own this skin!"), HttpStatus.BAD_REQUEST);
        } catch (UserNotFoundException e)
        {
            return new ResponseEntity<>(new ResponseMessage("User don't exists"), HttpStatus.BAD_REQUEST);
        } catch (NotEnoughCreditsException e)
        {
            return new ResponseEntity<>(new ResponseMessage("Not enough credits"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ResponseMessage("Skin acquired!"), HttpStatus.CREATED);
    }
}

package org.progettopsw.controllers;

import org.progettopsw.models.Skin;
import org.progettopsw.models.Utente;
import org.progettopsw.models.UtenteSkin;
import org.progettopsw.support.dto.SkinDTO;
import org.progettopsw.support.embeddables.UtenteSkinKey;
import org.progettopsw.support.exceptions.*;
import org.progettopsw.support.jwt.CustomJWT;
import org.progettopsw.support.jwt.CustomJWTConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.progettopsw.services.SkinService;
import org.progettopsw.services.UtenteService;
import org.progettopsw.services.UtenteSkinService;
import org.progettopsw.support.messages.ResponseMessage;

import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;

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
    @Autowired
    private UtenteService utenteService;

    @GetMapping("/owned")
    @PreAuthorize("hasAnyRole('ROLE_user','ROLE_admin')")
    public ResponseEntity getSkinPossedute()
    {
        try
        {
            CustomJWT cJWT = (CustomJWT) SecurityContextHolder.getContext().getAuthentication();
            if (cJWT == null)
                return new ResponseEntity<>(new ResponseMessage("JWT error!"), HttpStatus.UNAUTHORIZED);
            Utente utente = utenteService.trovaUtente(cJWT.getEmail());

            List<Skin> skins = utenteSkinService.getUtenteSkin(utente);
            if (skins.isEmpty())
                return new ResponseEntity<>(new ResponseMessage("No results!"), HttpStatus.OK);
            List<SkinDTO> ret = new LinkedList<>();
            for (Skin skin : skins)
            {
                SkinDTO skinDTO = new SkinDTO();
                skinDTO.setId(skin.getId());
                skinDTO.setNome(skin.getNome());
                skinDTO.setCrediti(skin.getCrediti());
                ret.add(skinDTO);
            }

            return new ResponseEntity<>(ret, HttpStatus.OK);
        } catch (UserNotFoundException e)
        {
            return new ResponseEntity<>(new ResponseMessage("Uer not found!"), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/notowned")
    @PreAuthorize("hasAnyRole('ROLE_user','ROLE_admin')")
    public ResponseEntity getSkinNonPossedute()
    {
        try
        {
            CustomJWT cJWT = (CustomJWT) SecurityContextHolder.getContext().getAuthentication();
            if (cJWT == null)
                return new ResponseEntity<>(new ResponseMessage("JWT error!"), HttpStatus.UNAUTHORIZED);
            Utente utente = utenteService.trovaUtente(cJWT.getEmail());

            List<Skin> possedute = utenteSkinService.getUtenteSkin(utente);
            List<Skin> tutte = skinService.getAll();
            List<SkinDTO> ret = new LinkedList<>();
            for (Skin skin : tutte)
                if (!possedute.contains(skin))
                {
                    SkinDTO skinDTO = new SkinDTO();
                    skinDTO.setId(skin.getId());
                    skinDTO.setNome(skin.getNome());
                    skinDTO.setCrediti(skin.getCrediti());
                    ret.add(skinDTO);
                }
            if (ret.isEmpty())
                return new ResponseEntity<>(new ResponseMessage("No results!"), HttpStatus.OK);
            return new ResponseEntity<>(ret, HttpStatus.OK);
        } catch (UserNotFoundException e)
        {
            return new ResponseEntity<>(new ResponseMessage("Uer not found!"), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/acquire")
    @PreAuthorize("hasAnyRole('ROLE_user','ROLE_admin')")
    public ResponseEntity acquireSkin(@Valid @RequestParam("nome") String nome)
    {
        try
        {
            CustomJWT cJWT = (CustomJWT) SecurityContextHolder.getContext().getAuthentication();
            if (cJWT == null)
                return new ResponseEntity<>(new ResponseMessage("JWT error!"), HttpStatus.UNAUTHORIZED);
            Utente utente = utenteService.trovaUtente(cJWT.getEmail());
            Skin skin = skinService.skinPerNome(nome);

            if (utente == null)
                return new ResponseEntity<>(new ResponseMessage("User not found"), HttpStatus.NOT_FOUND);
            if (skin == null)
                return new ResponseEntity<>(new ResponseMessage("Skin not found"), HttpStatus.NOT_FOUND);

            UtenteSkin utenteSkin = new UtenteSkin();
            utenteSkin.setUtente(utente);
            utenteSkin.setSkin(skin);

            UtenteSkinKey utenteSkinKey = new UtenteSkinKey();
            utenteSkinKey.setSkin(skin.getId());
            utenteSkinKey.setUtente(utente.getId_utente());

            utenteSkin.setId(utenteSkinKey);

            utenteService.agiornaCrediti(utente, -skin.getCrediti());
            utenteSkinService.aggiungiSkinUtente(utenteSkin);

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
    public ResponseEntity salvaSkin(@Valid @RequestBody Skin skin)
    {
        try
        {
            skinService.nuovaSkin(skin);

            return new ResponseEntity<>(new ResponseMessage("Skin added!"), HttpStatus.CREATED);
        } catch (SkinAlreadyExistsException e) {
            return new ResponseEntity<>(new ResponseMessage("Skin already exists"), HttpStatus.BAD_REQUEST);
        }
    }

}

package org.progettopsw.controllers;

import org.progettopsw.models.Miglioramento;
import org.progettopsw.models.Skin;
import org.progettopsw.models.Utente;
import org.progettopsw.models.UtenteMiglioramento;
import org.progettopsw.support.dto.MiglioramentoDTO;
import org.progettopsw.support.dto.SkinDTO;
import org.progettopsw.support.jwt.CustomJWT;
import org.progettopsw.support.jwt.CustomJWTConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.transaction.annotation.Transactional;
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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
    @Autowired
    private UtenteService utenteService;

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_user')")
    @Transactional
    public ResponseEntity getMiglioramentiDisponibili()
    {
        List<Miglioramento> miglioramenti = miglioramentoService.findAll();
        if (miglioramenti.isEmpty())
            return new ResponseEntity<>(new ResponseMessage("No results!"), HttpStatus.OK);
        List<MiglioramentoDTO> ret = new ArrayList<>();
        for (Miglioramento miglioramento : miglioramenti)
        {
            MiglioramentoDTO dto = new MiglioramentoDTO();
            dto.setId(miglioramento.getId());
            dto.setNome(miglioramento.getNome());
            dto.setCrediti(miglioramento.getCrediti());
            dto.setDescrizione(miglioramento.getDescrizione());
            dto.setTipologia(miglioramento.getTipologia());
            ret.add(dto);
        }
        return new ResponseEntity<>(ret, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('ROLE_user')")
    public ResponseEntity getMiglioramentoByNome(@RequestParam(value="nome") String nome)
    {
        try
        {
            Miglioramento miglioramento = miglioramentoService.miglioramentoPerNome(nome);
            if (miglioramento == null)
                return new ResponseEntity<>(new ResponseMessage("No results!"), HttpStatus.OK);
            MiglioramentoDTO dto  = new MiglioramentoDTO();
            dto.setId(miglioramento.getId());
            dto.setNome(miglioramento.getNome());
            dto.setCrediti(miglioramento.getCrediti());
            dto.setDescrizione(miglioramento.getDescrizione());
            dto.setTipologia(miglioramento.getTipologia());
            return new ResponseEntity<>(dto, HttpStatus.OK);
        }catch(Exception e)
        {
            return new ResponseEntity<>(new ResponseMessage("Errore!"), HttpStatus.OK);
        }
    }

    @GetMapping("/search/{crediti}")
    @PreAuthorize("hasAuthority('ROLE_user')")
    public ResponseEntity getMiglioramentoByCrediti(@PathVariable("crediti") int crediti)
    {
        try
        {
            List<Miglioramento> miglioramenti = miglioramentoService.miglioramentoPerCrediti(crediti);

            if (miglioramenti.isEmpty())
                return new ResponseEntity<>(new ResponseMessage("No results!"), HttpStatus.OK);

            List<MiglioramentoDTO> ret = new ArrayList<>();
            for (Miglioramento miglioramento : miglioramenti)
            {
                MiglioramentoDTO dto = new MiglioramentoDTO();
                dto.setId(miglioramento.getId());
                dto.setNome(miglioramento.getNome());
                dto.setCrediti(miglioramento.getCrediti());
                dto.setDescrizione(miglioramento.getDescrizione());
                dto.setTipologia(miglioramento.getTipologia());
                ret.add(dto);
            }
            return new ResponseEntity<>(ret, HttpStatus.OK);
        }catch(Exception e)
        {
            return new ResponseEntity<>(new ResponseMessage("Errore!"), HttpStatus.OK);
        }
    }

    @PostMapping("/acquire")
    @PreAuthorize("hasAuthority('ROLE_user')")
    public ResponseEntity acquireMiglioramento(@Valid @RequestBody String nome)
    {
        try
        {
            CustomJWT cJWT = (CustomJWT) SecurityContextHolder.getContext().getAuthentication();
            if (cJWT == null)
                return new ResponseEntity<>(new ResponseMessage("JWT error!"), HttpStatus.OK);
            Utente utente = utenteService.trovaUtente(cJWT.getEmail());
            Miglioramento miglioramento = miglioramentoService.miglioramentoPerNome(nome);

            UtenteMiglioramento utenteMiglioramento = new UtenteMiglioramento();
            utenteMiglioramento.setUtente(utente);
            utenteMiglioramento.setMiglioramento(miglioramento);
            utenteMiglioramento.setQuantita(1);

            if (!utenteMiglioramentoService.miglioramentiPerUtente(utenteMiglioramento.getUtente()).contains(utenteMiglioramento.getMiglioramento()))
                utenteMiglioramentoService.aggiungiMiglioramentoAdUtente(utenteMiglioramento);
            else
                utenteMiglioramentoService.updateQuantitaMiglioramentoAdUtente(utenteMiglioramento.getUtente(), utenteMiglioramento.getMiglioramento(), utenteMiglioramento.getQuantita());
            utenteService.agiornaCrediti(utenteMiglioramento.getUtente(), -utenteMiglioramento.getMiglioramento().getCrediti());

            return new ResponseEntity<>(new ResponseMessage("Miglioramento acquired!"), HttpStatus.CREATED);
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
}

package org.progettopsw.controllers;

import org.progettopsw.models.Miglioramento;
import org.progettopsw.models.Skin;
import org.progettopsw.models.Utente;
import org.progettopsw.models.UtenteMiglioramento;
import org.progettopsw.repositories.MiglioramentoRepository;
import org.progettopsw.support.dto.MiglioramentoDTO;
import org.progettopsw.support.dto.SkinDTO;
import org.progettopsw.support.embeddables.UtenteMiglioramentoKey;
import org.progettopsw.support.exceptions.*;
import org.progettopsw.support.jwt.CustomJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.progettopsw.services.MiglioramentoService;
import org.progettopsw.services.UtenteMiglioramentoService;
import org.progettopsw.services.UtenteService;
import org.progettopsw.support.messages.ResponseMessage;

import javax.validation.Valid;
import java.util.ArrayList;
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
    @Autowired
    private MiglioramentoRepository miglioramentoRepository;

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ROLE_user','ROLE_admin')")
    @Transactional
    public ResponseEntity getMiglioramentiDisponibili()
    {
        try
        {
            List<Miglioramento> miglioramenti = miglioramentoService.findAll();
            if (miglioramenti.isEmpty())
                return new ResponseEntity<>(new ResponseMessage("No results!"), HttpStatus.OK);
            CustomJWT cJWT = (CustomJWT) SecurityContextHolder.getContext().getAuthentication();
            if (cJWT == null)
                return new ResponseEntity<>(new ResponseMessage("JWT error!"), HttpStatus.OK);
            Utente utente = utenteService.trovaUtente(cJWT.getEmail());
            List<MiglioramentoDTO> ret = new ArrayList<>();
            for (Miglioramento miglioramento : miglioramenti)
            {
                MiglioramentoDTO dto = new MiglioramentoDTO();
                dto.setId(miglioramento.getId());
                dto.setNome(miglioramento.getNome());
                dto.setCrediti(miglioramento.getCrediti());
                dto.setDescrizione(miglioramento.getDescrizione());
                dto.setTipologia(miglioramento.getTipologia());
                dto.setQuantitaMassima(miglioramento.getQuantita_massima());
                UtenteMiglioramento utenteMiglioramento = utenteMiglioramentoService.miglioramentoUtente(utente, miglioramento);
                int posseduti = 0;
                if (utenteMiglioramento != null)
                    posseduti = utenteMiglioramento.getQuantita();
                dto.setQuantitaPosseduta(posseduti);
                ret.add(dto);
            }
            return new ResponseEntity<>(ret, HttpStatus.OK);
        } catch (UserNotFoundException e)
        {
            return new ResponseEntity<>(new ResponseMessage("User doesn't exists"), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAnyRole('ROLE_user','ROLE_admin')")
    public ResponseEntity getMiglioramentoByNome(@RequestParam(value="nome") String nome)
    {
        try
        {
            Miglioramento miglioramento = miglioramentoService.miglioramentoPerNome(nome);
            if (miglioramento == null)
                return new ResponseEntity<>(new ResponseMessage("No results!"), HttpStatus.OK);
            CustomJWT cJWT = (CustomJWT) SecurityContextHolder.getContext().getAuthentication();
            if (cJWT == null)
                return new ResponseEntity<>(new ResponseMessage("JWT error!"), HttpStatus.OK);
            Utente utente = utenteService.trovaUtente(cJWT.getEmail());
            MiglioramentoDTO dto  = new MiglioramentoDTO();
            dto.setId(miglioramento.getId());
            dto.setNome(miglioramento.getNome());
            dto.setCrediti(miglioramento.getCrediti());
            dto.setDescrizione(miglioramento.getDescrizione());
            dto.setTipologia(miglioramento.getTipologia());
            dto.setQuantitaMassima(miglioramento.getQuantita_massima());
            UtenteMiglioramento utenteMiglioramento = utenteMiglioramentoService.miglioramentoUtente(utente, miglioramento);
            int posseduti = 0;
            if (utenteMiglioramento != null)
                posseduti = utenteMiglioramento.getQuantita();
            dto.setQuantitaPosseduta(posseduti);

            return new ResponseEntity<>(dto, HttpStatus.OK);
        }catch(Exception e)
        {
            return new ResponseEntity<>(new ResponseMessage("Errore!"), HttpStatus.OK);
        }
    }

    @GetMapping("/search/{crediti}")
    @PreAuthorize("hasAnyRole('ROLE_user','ROLE_admin')")
    public ResponseEntity getMiglioramentoByCrediti(@PathVariable("crediti") int crediti)
    {
        try
        {
            List<Miglioramento> miglioramenti = miglioramentoService.miglioramentoPerCrediti(crediti);

            if (miglioramenti.isEmpty())
                return new ResponseEntity<>(new ResponseMessage("No results!"), HttpStatus.OK);

            CustomJWT cJWT = (CustomJWT) SecurityContextHolder.getContext().getAuthentication();
            if (cJWT == null)
                return new ResponseEntity<>(new ResponseMessage("JWT error!"), HttpStatus.OK);
            Utente utente = utenteService.trovaUtente(cJWT.getEmail());

            List<MiglioramentoDTO> ret = new ArrayList<>();
            for (Miglioramento miglioramento : miglioramenti)
            {
                MiglioramentoDTO dto = new MiglioramentoDTO();
                dto.setId(miglioramento.getId());
                dto.setNome(miglioramento.getNome());
                dto.setCrediti(miglioramento.getCrediti());
                dto.setDescrizione(miglioramento.getDescrizione());
                dto.setTipologia(miglioramento.getTipologia());
                dto.setQuantitaMassima(miglioramento.getQuantita_massima());
                UtenteMiglioramento utenteMiglioramento = utenteMiglioramentoService.miglioramentoUtente(utente, miglioramento);
                int posseduti = 0;
                if (utenteMiglioramento != null)
                    posseduti = utenteMiglioramento.getQuantita();
                dto.setQuantitaPosseduta(posseduti);
                ret.add(dto);
            }
            return new ResponseEntity<>(ret, HttpStatus.OK);
        }catch(Exception e)
        {
            return new ResponseEntity<>(new ResponseMessage("Errore!"), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/acquire")
    @PreAuthorize("hasAnyRole('ROLE_user','ROLE_admin')")
    public ResponseEntity acquireMiglioramento(@Valid @RequestParam("nome") String nome)
    {
        try
        {
            CustomJWT cJWT = (CustomJWT) SecurityContextHolder.getContext().getAuthentication();
            if (cJWT == null)
                return new ResponseEntity<>(new ResponseMessage("JWT error!"), HttpStatus.OK);
            Utente utente = utenteService.trovaUtente(cJWT.getEmail());
            Miglioramento miglioramento = miglioramentoService.miglioramentoPerNome(nome);



            utenteService.aggiungiCrediti(utente, -miglioramento.getCrediti());

            if (!utenteMiglioramentoService.miglioramentiPerUtente(utente).contains(miglioramento))
            {
                UtenteMiglioramento utenteMiglioramento = new UtenteMiglioramento();
                UtenteMiglioramentoKey utenteMiglioramentoKey = new UtenteMiglioramentoKey();
                utenteMiglioramentoKey.setMiglioramento(miglioramento.getId());
                utenteMiglioramentoKey.setUtente(utente.getId_utente());
                utenteMiglioramento.setId(utenteMiglioramentoKey);
                utenteMiglioramento.setUtente(utente);
                utenteMiglioramento.setMiglioramento(miglioramento);
                utenteMiglioramento.setQuantita(1);

                utenteMiglioramentoService.aggiungiMiglioramentoAdUtente(utenteMiglioramento);
            }
            else
                utenteMiglioramentoService.updateQuantitaMiglioramentoAdUtente(utenteMiglioramentoService.miglioramentoUtente(utente, miglioramento), 1);

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
            if (nome.isEmpty())
                return new ResponseEntity<>(new ResponseMessage("Nome invalido"), HttpStatus.BAD_REQUEST);
            if (descrizione.isEmpty())
                return new ResponseEntity<>(new ResponseMessage("Descrizione invalida"), HttpStatus.BAD_REQUEST);
            if (!tipologia.equals("a") && !tipologia.equals("b") && !tipologia.equals("c") && !tipologia.equals("d") && !tipologia.equals("e") )
                return new ResponseEntity<>(new ResponseMessage("Tipologia invalida "), HttpStatus.BAD_REQUEST);
            if (crediti <= 0)
                return new ResponseEntity<>(new ResponseMessage("Inserire un valore di crediti maggiore di 0"), HttpStatus.BAD_REQUEST);
            if (quantitaMassima <= 0)
                return new ResponseEntity<>(new ResponseMessage("Inserire una quantità massima maggiore di 0"), HttpStatus.BAD_REQUEST);

            Miglioramento miglioramento = new Miglioramento();
            miglioramento.setCrediti(crediti);
            miglioramento.setNome(nome);
            miglioramento.setDescrizione(descrizione);
            miglioramento.setTipologia(tipologia.toLowerCase());
            miglioramento.setQuantita_massima(quantitaMassima);

            miglioramentoService.nuovoMiglioramento(miglioramento);
            return new ResponseEntity<>(new ResponseMessage("Miglioramento added!"), HttpStatus.CREATED);
        } catch (MiglioramentoAlreadyExistsException e)
        {
            return new ResponseEntity<>(new ResponseMessage("Miglioramento already exists"), HttpStatus.BAD_REQUEST);

        }
    }
}

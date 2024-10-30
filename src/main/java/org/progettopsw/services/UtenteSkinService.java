package org.progettopsw.services;

import jakarta.persistence.LockModeType;
import org.progettopsw.models.Skin;
import org.progettopsw.models.Utente;
import org.progettopsw.models.UtenteSkin;
import org.progettopsw.repositories.SkinRepository;
import org.progettopsw.support.dto.SkinDTO;
import org.progettopsw.support.embeddables.UtenteSkinKey;
import org.progettopsw.support.exceptions.*;
import org.progettopsw.support.messages.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.progettopsw.repositories.UtenteSkinRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Service
public class UtenteSkinService
{
    @Autowired
    private UtenteSkinRepository utenteSkinRepository;
    @Autowired
    private UtenteService utenteService;
    @Autowired
    private SkinService skinService;

    @Transactional(readOnly = true)
    public List<SkinDTO> getUtenteSkin() throws IllegalArgumentException, UserNotFoundException, NoSkinsException
    {
        Utente utente = utenteService.trovaUtente();

        List<Skin> skins = utenteSkinRepository.findSkinByUtente(utente);
        if (skins.isEmpty())
            throw new NoSkinsException();
        List<SkinDTO> ret = new LinkedList<>();
        for (Skin skin : skins)
        {
            SkinDTO skinDTO = new SkinDTO();
            skinDTO.setId(skin.getId());
            skinDTO.setNome(skin.getNome());
            skinDTO.setCrediti(skin.getCrediti());
            ret.add(skinDTO);
        }

        return ret;
    }

    @Transactional(readOnly = true)
    public List<SkinDTO> getSkinNonPossedute() throws UserNotFoundException, NoSkinsException
    {
        List<SkinDTO> possedute = getUtenteSkin();
        List<SkinDTO> tutte = skinService.getAll();
        List<SkinDTO> ret = new LinkedList<>();

        for (SkinDTO skinT : tutte)
        {
            boolean add = true;
            for (SkinDTO skinP : possedute)
                    if (Objects.equals(skinT.getId(), skinP.getId()))
                    {
                        add = false;
                        break;
                    }
            if (add)
                ret.add(skinT);
        }
        if (ret.isEmpty())
            throw new NoSkinsException();
        return ret;
    }

    @Lock(LockModeType.OPTIMISTIC)
    @Transactional(readOnly = false)
    public void aggiungiSkinUtente(String nome) throws IllegalArgumentException, SkinAlreadyOwnedException, SkinDoesNotExistsException, UserNotFoundException, NotEnoughCreditsException
    {
        Utente utente = utenteService.trovaUtente();
        Skin skin = skinService.skinPerNome(nome);

        if (utenteSkinRepository.findSkinByUtente(utente).contains(skin))
            throw new SkinAlreadyOwnedException();

        UtenteSkin utenteSkin = new UtenteSkin();
        utenteSkin.setUtente(utente);
        utenteSkin.setSkin(skin);

        UtenteSkinKey utenteSkinKey = new UtenteSkinKey();
        utenteSkinKey.setSkin(skin.getId());
        utenteSkinKey.setUtente(utente.getId_utente());

        utenteSkin.setId(utenteSkinKey);

        utenteService.aggiungiCrediti(utente, -skin.getCrediti());
        utenteSkinRepository.save(utenteSkin);
    }
}

package org.progettopsw.services;

import jakarta.persistence.LockModeType;
import org.progettopsw.models.Skin;
import org.progettopsw.models.Utente;
import org.progettopsw.models.UtenteSkin;
import org.progettopsw.repositories.SkinRepository;
import org.progettopsw.support.exceptions.SkinDoesNotExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.progettopsw.repositories.UtenteSkinRepository;
import org.progettopsw.support.exceptions.SkinAlreadyOwnedException;
import org.springframework.web.context.request.FacesRequestAttributes;

import java.util.List;

@Service
public class UtenteSkinService
{
    @Autowired
    private UtenteSkinRepository utenteSkinRepository;
    @Autowired
    private SkinRepository skinRepository;

    @Transactional(readOnly = true)
    public List<Skin> getUtenteSkin(Utente utente) throws IllegalArgumentException
    {
        if (utente == null)
            throw new IllegalArgumentException();
        return utenteSkinRepository.findSkinByUtente(utente);
    }

    @Lock(LockModeType.OPTIMISTIC)
    @Transactional(readOnly = false)
    public void aggiungiSkinUtente(UtenteSkin utenteSkin) throws IllegalArgumentException, SkinAlreadyOwnedException, SkinDoesNotExistsException
    {
        if (utenteSkin == null || utenteSkin.getUtente() == null || utenteSkin.getSkin() == null)
            throw new IllegalArgumentException();
        if (!skinRepository.findAll().contains(utenteSkin.getSkin()))
            throw new SkinDoesNotExistsException();
        if (utenteSkinRepository.findSkinByUtente(utenteSkin.getUtente()).contains(utenteSkin.getSkin()))
            throw new SkinAlreadyOwnedException();
        utenteSkinRepository.save(utenteSkin);
    }
}

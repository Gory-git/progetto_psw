package org.progettopsw.services;

import org.progettopsw.models.Skin;
import org.progettopsw.models.Utente;
import org.progettopsw.models.UtenteSkin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.progettopsw.repositories.UtenteSkinRepository;
import org.progettopsw.support.exceptions.SkinAlreadyOwnedException;

import java.util.List;

@Service
public class UtenteSkinService
{
    @Autowired
    private UtenteSkinRepository utenteSkinRepository;

    @Transactional(readOnly = true)
    public List<Skin> getUtenteSkin(Utente utente) throws IllegalArgumentException
    {
        if (utente == null)
            throw new IllegalArgumentException();
        return utenteSkinRepository.findSkinByUtente(utente);
    }

    public void aggiungiSkinUtente(UtenteSkin utenteSkin) throws IllegalArgumentException, SkinAlreadyOwnedException
    {
        if (utenteSkin == null || utenteSkin.getUtente() == null || utenteSkin.getSkin() == null)
            throw new IllegalArgumentException();
        if (utenteSkinRepository.findSkinByUtente(utenteSkin.getUtente()).contains(utenteSkin.getSkin()))
            throw new SkinAlreadyOwnedException();
        utenteSkinRepository.save(utenteSkin);
    }
}
package org.progettopsw.services;

import org.progettopsw.models.Miglioramento;
import org.progettopsw.models.Skin;
import org.progettopsw.support.exceptions.MiglioramentoAlreadyExistsException;
import org.progettopsw.support.exceptions.SkinAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.progettopsw.repositories.SkinRepository;

import java.util.List;

@Service
public class SkinService
{
    @Autowired
    private SkinRepository skinRepository;

    @Transactional(readOnly = true)
    public Skin skinPerNome(String nome)
    {
        return skinRepository.findByNome(nome);
    }

    @Transactional(readOnly = true)
    public List<Skin> skinPerCrediti(int crediti)
    {
        return skinRepository.findByCrediti(crediti);
    }

    @Transactional(readOnly = true)
    public List<Skin> getAll()
    {
        return skinRepository.findAll();
    }

    @Transactional(readOnly = false)
    public void nuovaSkin(Skin skin) throws SkinAlreadyExistsException {
        if (skinRepository.findByNome(skin.getNome()) != null)
            throw new SkinAlreadyExistsException();
        skinRepository.save(skin);
    }
}

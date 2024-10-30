package org.progettopsw.services;

import org.progettopsw.models.Skin;
import org.progettopsw.support.dto.SkinDTO;
import org.progettopsw.support.exceptions.SkinAlreadyExistsException;
import org.progettopsw.support.messages.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.progettopsw.repositories.SkinRepository;

import java.util.LinkedList;
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
    public List<SkinDTO> getAll()
    {
        List<Skin> tutte = skinRepository.findAll();
        List<SkinDTO> ret = new LinkedList<>();

        for (Skin skin : tutte)
        {
                SkinDTO skinDTO = new SkinDTO();
                skinDTO.setId(skin.getId());
                skinDTO.setNome(skin.getNome());
                skinDTO.setCrediti(skin.getCrediti());
                ret.add(skinDTO);
        }
        return ret;
    }

    @Transactional(readOnly = false)
    public void nuovaSkin(String nome, int crediti) throws SkinAlreadyExistsException, IllegalArgumentException
    {
        if (nome.isEmpty())
            throw new IllegalArgumentException("Nome invalido");
        if (crediti <= 0)
            throw new IllegalArgumentException("Crediti non validi");
        if (skinRepository.findByNome(nome) != null)
            throw new SkinAlreadyExistsException();

        Skin skin = new Skin();
        skin.setCrediti(crediti);
        skin.setNome(nome);
        skinRepository.save(skin);
    }
}

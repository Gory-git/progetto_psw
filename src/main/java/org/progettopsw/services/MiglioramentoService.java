package org.progettopsw.services;

import org.progettopsw.models.Miglioramento;
import org.progettopsw.support.exceptions.MiglioramentoAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.progettopsw.repositories.MiglioramentoRepository;

import java.util.List;

@Service
public class MiglioramentoService
{
    @Autowired
    private MiglioramentoRepository miglioramentoRepository;

    @Transactional(readOnly = true)
    public List<Miglioramento> findAll()
    {
        return miglioramentoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Miglioramento miglioramentoPerNome(String nome)
    {
        return miglioramentoRepository.findByNome(nome);
    }

    @Transactional(readOnly = true)
    public List<Miglioramento> miglioramentoPerCrediti(int crediti)
    {
        return miglioramentoRepository.findByCrediti(crediti);
    }

    @Transactional(readOnly = false)
    public void nuovoMiglioramento(Miglioramento miglioramento) throws MiglioramentoAlreadyExistsException {
        if (miglioramentoRepository.findByNome(miglioramento.getNome()) != null)
            throw new MiglioramentoAlreadyExistsException();
        miglioramentoRepository.save(miglioramento);
    }
}

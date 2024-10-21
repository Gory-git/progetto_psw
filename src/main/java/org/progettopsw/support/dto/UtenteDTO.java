package org.progettopsw.support.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class UtenteDTO
{
    private long id;
    private String nome;
    private String cognome;
    private String email;
    private int crediti;
}

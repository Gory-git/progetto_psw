package org.progettopsw.support.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class SkinDTO
{
    private Long id;
    private int crediti;
    private String nome;
}

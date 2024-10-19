package org.progettopsw.support.jwt;

import com.nimbusds.jwt.JWT;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;

@Getter
@Setter
public class CustomJWT extends JwtAuthenticationToken
{
    private String username;
    private String nome;
    private String cognome;
    public CustomJWT(Jwt jwt, Collection<? extends GrantedAuthority> authorities) {
        super(jwt, authorities);
    }
}

package org.progettopsw.support.jwt;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import org.springframework.lang.NonNull;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

public class CustomJWTConverter implements Converter<Jwt, CustomJWT>
{

    @Override
    public CustomJWT convert(@NonNull Jwt jwt) {
        Collection<GrantedAuthority> authorities = extractAuthorities(jwt);

        var customJwt = new CustomJWT(jwt, authorities);
        customJwt.setUsername(jwt.getClaimAsString("username"));
        customJwt.setNome(jwt.getClaimAsString("given_name"));
        customJwt.setCognome(jwt.getClaimAsString("family_name"));
        return customJwt;
    }

    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt)
    {
        var authorities = new ArrayList<GrantedAuthority>();

        var realm_access = jwt.getClaimAsMap("realm_access");
        if (realm_access != null && realm_access.get("roles") != null) {
            var roles = realm_access.get("roles");
            if (roles instanceof List l) {
                l.forEach(role ->
                        authorities.add(new SimpleGrantedAuthority("ROLE_" + role))
                );
            }
        }

        return authorities;
    }
}

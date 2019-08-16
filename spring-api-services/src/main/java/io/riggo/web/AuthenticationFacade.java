package io.riggo.web;

import com.auth0.jwt.JWT;
import com.auth0.spring.security.api.authentication.AuthenticationJsonWebToken;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacade {

    public final static String EMAIL_CLAIM_KEY = "https://auth.riggoqa.com/email";
    public final static String SITEID_CLAIM_KEY = "https://auth.riggoqa.com/siteId";

    public String getUsername(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return JWT.decode(((AuthenticationJsonWebToken) authentication).getToken()).getClaim(EMAIL_CLAIM_KEY).asString();
    }

    public Integer getSiteId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String siteId = JWT.decode(((AuthenticationJsonWebToken) authentication).getToken()).getClaim(SITEID_CLAIM_KEY).asString();
        return NumberUtils.toInt(siteId);
    }
}

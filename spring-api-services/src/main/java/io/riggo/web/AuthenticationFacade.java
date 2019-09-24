package io.riggo.web;

import com.auth0.jwt.JWT;
import com.auth0.spring.security.api.authentication.AuthenticationJsonWebToken;
import io.riggo.data.domain.Role;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacade {

    public final static String EMAIL_CLAIM_KEY = "https://auth.riggoqa.com/email";
    public final static String SITEID_CLAIM_KEY = "https://auth.riggoqa.com/siteId";
    public final static String ROLES_CLAIM_KEY = "https://auth.riggoqa.com//roles";
    public final static String CLIENT_ID = "A0WB2oszcFXbz0IB2PhREmDgSopOhGYp@clients";


    public String getUsername(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return JWT.decode(((AuthenticationJsonWebToken) authentication).getToken()).getClaim(EMAIL_CLAIM_KEY).asString();
    }

    public Integer getSiteId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String siteId = JWT.decode(((AuthenticationJsonWebToken) authentication).getToken()).getClaim(SITEID_CLAIM_KEY).asString();
        return NumberUtils.toInt(siteId);
    }

    public String [] getRoles(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return JWT.decode(((AuthenticationJsonWebToken) authentication).getToken()).getClaim(ROLES_CLAIM_KEY).asArray(String.class);
    }

    public boolean isSuperAdmin(){
        return ArrayUtils.contains(getRoles(), Role.SUPER_ADMIN.getAuth0Role());
    }

    public boolean isSiteAdmin(){
        return ArrayUtils.contains(getRoles(), Role.SITE_ADMIN.getAuth0Role());
    }

    public boolean isShipperExecutive(){
        return ArrayUtils.contains(getRoles(), Role.SHIPPER_EXECUTIVE.getAuth0Role());
    }

    public boolean isMachine(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return StringUtils.equals(((AuthenticationJsonWebToken) authentication).getName(), CLIENT_ID);
    }
}

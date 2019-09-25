package io.riggo.web;

import com.auth0.jwt.JWT;
import com.auth0.spring.security.api.authentication.AuthenticationJsonWebToken;
import io.riggo.config.Auth0Properties;
import io.riggo.data.domain.Role;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacade {

    @Autowired
    private Auth0Properties auth0Properties;
    public final static String EMAIL_CLAIM_KEY = "https://auth.riggoqa.com/email";
    public final static String SITEID_CLAIM_KEY = "https://auth.riggoqa.com/siteId";
    public final static String ROLES_CLAIM_KEY = "https://auth.riggoqa.com//roles";
    public final static String CLIENT_ID = "A0WB2oszcFXbz0IB2PhREmDgSopOhGYp@clients";


    public String getUsername(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return JWT.decode(((AuthenticationJsonWebToken) authentication).getToken()).getClaim(auth0Properties.getEmailClaimKey()).asString();
    }

    public Integer getSiteId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String siteId = JWT.decode(((AuthenticationJsonWebToken) authentication).getToken()).getClaim(auth0Properties.getSiteIdClaimKey()).asString();
        return NumberUtils.toInt(siteId);
    }

    public String [] getRoles(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return JWT.decode(((AuthenticationJsonWebToken) authentication).getToken()).getClaim(auth0Properties.getRolesClaimKey()).asArray(String.class);
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
        return StringUtils.equals(((AuthenticationJsonWebToken) authentication).getName(), auth0Properties.getMachineClientId());
    }
}

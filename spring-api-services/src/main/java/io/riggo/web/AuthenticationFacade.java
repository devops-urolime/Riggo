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

    public String getUsername(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return JWT.decode(((AuthenticationJsonWebToken) authentication).getToken()).getClaim(auth0Properties.getEmailClaimKey()).asString();
    }

    //TODO: Auth0 has hardcoded the siteId - we should be able to add it to MetaData in Auth0 and pull it for the rule.
    public Integer getSiteId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String siteId = JWT.decode(((AuthenticationJsonWebToken) authentication).getToken()).getClaim(auth0Properties.getSiteIdClaimKey()).asString();
        if(siteId == null && isMachine()){
            return 100;
        }
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

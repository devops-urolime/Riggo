package io.riggo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("auth0")
public class Auth0Properties {

    private String issuer;
    private String apiAudience;
    private String machineClientId;
    private String namespace;

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getApiAudience() {
        return apiAudience;
    }

    public void setApiAudience(String apiAudience) {
        this.apiAudience = apiAudience;
    }

    public String getMachineClientId() {
        return machineClientId;
    }

    public void setMachineClientId(String machineClientId) {
        this.machineClientId = machineClientId;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getEmailClaimKey(){
        return this.namespace + "email";
    }

    public String getSiteIdClaimKey(){
        return this.namespace + "siteId";
    }

    public String getRolesClaimKey(){
        return this.namespace + "roles";
    }
}

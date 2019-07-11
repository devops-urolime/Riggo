package io.riggo.utilities.idgen;

import java.util.UUID;

public class ClientKeyGenerator {

    public String generateClientId(){
        return UUID.randomUUID().toString();
    }
}


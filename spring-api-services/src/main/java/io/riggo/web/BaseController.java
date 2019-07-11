package io.riggo.web;

import java.util.HashMap;
import java.util.Map;


/*import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.SignatureGenerationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.RSAKeyProvider;


import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import static com.auth0.jwt
//import static com.auth0.jwt.PemUtils.readPublicKeyFromFile;
*/

// TODO: Remove this controller if this is not used.
public class BaseController {

    Map<String, Object> getUserMetaData(Map<String, Object> headers) {

        Map<String, Object> meta = new HashMap();
        String token = (String) headers.get("authorization");


        return meta;
    }
}

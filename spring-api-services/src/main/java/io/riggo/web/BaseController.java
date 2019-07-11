package io.riggo.web;


import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * This controller is to be the base class to all controllers. for a point of implementing common services
 * that will be invoked such as Extraction and Validation of JWT tokens And reaching our the header data. M
 * More of the c
 */
public class BaseController {

    public HttpServletRequest getReq() {
        return req;
    }

    @Autowired
    HttpServletRequest req;

    public Logger getLogger() {
        return logger;
    }

    Logger logger = LoggerFactory.getLogger(this.getClass());


    public void dumpHeaders() {

        Map<String, String> heads = getHeaders();

        heads.forEach((key, value) -> {
            logger.error(String.format("Header '%s' = %s", key, value));

        });
    }

    public String getToken() {

        String token = req.getHeader("Authorization");
        if (!Strings.isNullOrEmpty(token)) {
            return token.substring(7);
        }
        return null;

    }


    Map<String, String> getHeaders() {

        return Collections.list(req.getHeaderNames())
                .stream()
                .collect(Collectors.toMap(h -> h, req::getHeader));

    }


    public Map getTokenClaims(String token) {

        Map claims = null;
        try {
            DecodedJWT jwt = JWT.decode(token);
            claims = jwt.getClaims();
        } catch (JWTDecodeException e) {

            logger.error(Arrays.toString(e.getStackTrace()));
        }
        return claims;
    }

    public void dumpTokenData(String token) {
        Map claims;
        try {
            DecodedJWT jwt = JWT.decode(token);
            claims = jwt.getClaims();


            if (claims != null) {
                claims.forEach((key, value) -> {
                    logger.error(String.format(" ** JWT Claims '%s' = %s", key, value));

                });
            }


            // claims.containsKey("read")
        } catch (JWTDecodeException exception) {
            logger.error(Arrays.toString(exception.getStackTrace()));
        }
    }

}

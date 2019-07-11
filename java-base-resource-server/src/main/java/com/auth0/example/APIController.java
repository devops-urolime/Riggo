package com.auth0.example;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import org.json.JSONObject;

@Controller
@Component
public class APIController {

    @RequestMapping(value = "/v1/public", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String publicEndpoint() {
        return new JSONObject()
                .put("message", "All good. You DO NOT need to be authenticated to call /api/public.")
                .toString();
    }

    @RequestMapping(value = "/v1/private", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String privateEndpoint() {
        return new JSONObject()
                .put("message", "All good. You can see this because you are Authenticated.")
                .toString();
    }

    @RequestMapping(value = "/v1/private-scoped", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String privateScopedEndpoint() {
        return new JSONObject()
                .put("message", "All good. You can see this because you are Authenticated with a Token granted the 'read:messages' scope")
                .toString();
    }

    @RequestMapping(value = "/v1/coffee", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String privateCoffeeScopedEndpoint() {
        return new JSONObject()
                .put("coffee", "Here is a perfect coffee for you!. You can see this because you are Authenticated with a Token granted the 'read:coffee' scope")
                .toString();
    }
}

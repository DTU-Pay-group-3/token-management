package org.acme;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import models.ResponseMessage;


@Path("/token")
public class TokenService {

    public TokenService() {
    }

    @POST
    public ResponseMessage GenerateToken(String customerId) {

        String token = "tokenn" + customerId;
        return new ResponseMessage(true, token);

    }
}

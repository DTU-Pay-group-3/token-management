package org.acme;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import models.ResponseMessage;
import org.jboss.resteasy.core.interception.jaxrs.ResponseContainerRequestContext;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;



@Path("/token")
public class TokenService {

    private HashMap<String, String[]> tokenMap;

    public TokenService() {
        tokenMap = new HashMap<>();
    }

    @POST
    public String[] generateToken(String customerId) {
        String[] tokens = new String[6];
        for (int i = 0; i < 6; i++) {
            tokens[i] = newToken();
        }
        tokenMap.put(customerId, tokens);
        return tokens;

    }

    @POST
    @Path("/use")
    public ResponseMessage findToken(String token) {
        for (String customerID : tokenMap.keySet()) {
            String[] tokens = tokenMap.get(customerID);
            for (int i = 0; i < tokens.length; i++) {
                if (tokens[i].equals(token)) {
                    String[] updatedTokens = new String[tokens.length - 1];
                    int index = 0;
                    for (int j = 0; j < tokens.length; j++) {
                        if (j != i) {
                            updatedTokens[index++] = tokens[j];
                        }
                    }
                    tokenMap.put(customerID, updatedTokens);
                    System.out.println(Arrays.toString(tokenMap.get("cid1")));
                    return new ResponseMessage(true,customerID);
                }
            }
        }

        return new ResponseMessage(false, "Token not found"); // Token not found
    }

    private String newToken() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] tokenBytes = new byte[10];
        secureRandom.nextBytes(tokenBytes);
        String randomToken = Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
        randomToken = randomToken.substring(0, 10);
        return randomToken;
    }

}

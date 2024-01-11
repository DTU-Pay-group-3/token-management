package org.acme;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
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

    //holds all tokens from customer (customerId, tokens)
    //customerId1:[token1,token2,token3]
    public TokenService() {
        tokenMap = new HashMap<>();

    }

    @POST
    public String[] generateToken(String customerId) {
        //gets all tokens given a customer
        String[] tokensCustomerId = tokenMap.get(customerId);
        if(tokensCustomerId == null)
        {
            String[] tokens = new String[6];
            for (int i = 0; i < 6; i++) {
                tokens[i] = newToken();
            }
            tokenMap.put(customerId, tokens);
            return tokens;
        }
        //number of tokens per customer
        int numberOfTokens = tokensCustomerId.length;
        if(numberOfTokens < 2) {
            String[] tokens = new String[4];
            for (int i = 0; i < 4; i++) {
                tokens[i] = newToken();
            }
            tokenMap.put(customerId, tokens);
            return tokens;
        }
        else return null;
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

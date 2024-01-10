package dtu.example;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import models.ResponseMessage;

import java.util.ArrayList;

public class TokenService {

    private Client client;
    WebTarget baseUrl;

    public TokenService() {
        client = ClientBuilder.newClient();
        baseUrl = client.target("http://localhost:8080/");
    }

    public ResponseMessage generateToken(String cid)
    {
        Response response = baseUrl.path("token")
                .request()
                .post(Entity.entity(cid, MediaType.APPLICATION_JSON));
        return response.readEntity(ResponseMessage.class);
    }



}

package main;

import messaging.Event;
import messaging.MessageQueue;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;

public class TokenService {

    MessageQueue queue;
    private HashMap<String, String[]> tokenMap = new HashMap<>();

    //holds all tokens from customer (customerId, tokens)
    //customerId1:[token1,token2,token3]
    public TokenService() {

    }

    public TokenService(MessageQueue q) {
        this.queue = q;
        this.queue.addHandler("GenerateToken", this::generateToken);
        this.queue.addHandler("ValidateToken", this::FindToken);

    }

    public String[] getAllTokens(String customerid) {
        return tokenMap.get(customerid);
    }


    public void generateToken(Event ev) {
        System.out.println("Event GenerateToken found");
        //Pass customer id during event creation
        String customerId = ev.getArgument(0, String.class);
        var correlationId = ev.getArgument(1, CorrelationId.class);
        //gets all tokens given a customer
        String[] tokensCustomerId = tokenMap.get(customerId);
        if (tokensCustomerId == null) {
            String[] tokens = new String[6];
            for (int i = 0; i < 6; i++) {
                tokens[i] = newToken();
            }
            tokenMap.put(customerId, tokens);
            Event event = new Event("GenerateTokenCompleted", new Object[]{tokens, correlationId});
            System.out.println("Publishing GenerateTokenCompleted event");
            queue.publish(event);

        } else {
            //number of tokens per customer
            int numberOfTokens = tokensCustomerId.length;
            if (numberOfTokens < 2) {
                String[] tokens = new String[4];
                for (int i = 0; i < 4; i++) {
                    tokens[i] = newToken();
                }
                tokenMap.put(customerId, tokens);
                Event event = new Event("GenerateTokenCompleted", new Object[]{tokens, correlationId});
                System.out.println("Publishing GenerateTokenCompleted event");
                queue.publish(event);

            } else {
                Event event = new Event("GenerateTokenFailed", new Object[]{"FailMessage", correlationId});
                System.out.println("Publishing GenerateTokenFailed event");
                queue.publish(event);

            }
        }
    }


    public void FindToken(Event ev) {
        //Find token and remove it (validate)
        System.out.println("Event ValidateToken found");
        String token = ev.getArgument(0, String.class);
        var correlationId = ev.getArgument(1, CorrelationId.class);

        for (String customerID : tokenMap.keySet()) {
            String[] tokens = tokenMap.get(customerID);
            if (tokens == null) {
                Event event = new Event("ValidateTokenFailed", new Object[]{"Fail", correlationId});
                System.out.println("Publishing ValidateTokenFailed event");
                queue.publish(event);
                return;
            }
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
                    Event event = new Event("ValidateTokenCompleted", new Object[]{updatedTokens, correlationId});
                    System.out.println("Publishing ValidateTokenCompleted event");
                    System.out.println(String.join(",", updatedTokens));
                    queue.publish(event);
                    return;
                }
            }
            Event event = new Event("ValidateTokenFailed", new Object[]{"Fail", correlationId});
            System.out.println("Publishing ValidateTokenFailed event");
            queue.publish(event);
        }

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

package models;

public class RequestMessage {

    String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public RequestMessage(String token) {
        this.token = token;
    }

    public RequestMessage(){

    }
}

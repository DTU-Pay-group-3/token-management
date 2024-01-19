package main;


import messaging.implementations.RabbitMqQueue;

import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;

public class StartUp {
    public static void main(String[] args) throws Exception {
        new StartUp().startUp();
    }

    private void startUp() throws Exception {
        var mq = new RabbitMqQueue("rabbitMq");
        new TokenService(mq);
    }
}

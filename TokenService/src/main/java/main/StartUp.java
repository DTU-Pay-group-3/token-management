package main;


import messaging.implementations.RabbitMqQueue;

import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;

public class StartUp {
	public static void main(String[] args) throws Exception {

	}

	private void startUp() throws Exception {
var mq=new RabbitMqQueue("rabbitMq");
		System.out.println("connected to Mq");
		//System.out.println(mq.toString());
		//new PaymentService(mq);
	}
}

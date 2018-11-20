package com.chatapplicationdemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.chatapplicationdemo.model.ChatMessage;
@RestController  
public class SendToUserController {
	
	private static String username = "";
	

	@Autowired
	private SimpMessagingTemplate webSocket;
	
	@MessageMapping("/single-chat")
//	 @SendToUser(value = "/queue/messages", broadcast = false)
	public ChatMessage sendMessage1( @Payload ChatMessage chatMessage) throws Exception {
		System.out.println("3333333333333333");
		System.err.println("hi i am here");
		System.out.println("hai my username is "+" "+username);
	   

//	    webSocket.convertAndSendToUser(username, "/queue/messages", new ChatMessage(chatMessage.getContent()));
	   webSocket.convertAndSendToUser(username, "/queue/messages", null);
	   return chatMessage;
	}
	
	@PostMapping("/getselecteduser")
	public String getSelectedUser(@RequestBody String x ) {
		System.out.println("useraname is ready to take"+x);
		username = x;
		return null;
		
	}
}

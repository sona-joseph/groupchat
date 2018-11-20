package com.chatapplicationdemo.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.chatapplicationdemo.model.ChatMessage;

@RestController                             //we changed controller to rest controler.only then we get json data.
public class ChatController {
	
	

	JSONArray array = new JSONArray();
	
//	private static String username = "";
	
	
	
	
	
	@MessageMapping("/chat.addUser")  //to print who is joined 
	@SendTo("/topic/public")
	public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {

		headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
		
		System.out.println(">...............he i am  joined" +" " +chatMessage);
		return chatMessage;
	}
	
	@MessageMapping("/group-chat")   // The @MessageMapping annotation ensures that if a message is sent to
										   // destination "/chat.sendMessage", then the sendMessage() method is called.
	@SendTo("/topic/public")
	public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        
		System.out.println("$$$$$$$$$$$$$$$$$" + chatMessage);
		
		
		System.err.println("hi i am " + chatMessage.getSender());
		String s = chatMessage.getSender();
		array.add(s);
        return chatMessage; // The return value is broadcast to all subscribers to "/topic/public" as
							// specified in the @SendTo annotation
	}
	
	

	
	
	/*
	@Autowired
	private SimpMessagingTemplate webSocket;

	@MessageMapping("/single-chat")
	public ChatMessage sendMessage1( @Payload ChatMessage chatMessage) throws Exception {
		System.out.println("3333333333333333");
		System.err.println("hi i am here");
	   

//	    webSocket.convertAndSendToUser(username, "/queue/messages", new ChatMessage(chatMessage.getContent()));
	   webSocket.convertAndSendToUser(username, "/queue/messages", null);
	   return chatMessage;
	}
	*/
	
	
	
	@GetMapping("/getonlineusers")
	public JSONArray getOnlineUsers() {
	
		System.out.println(";;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;"+array);
		return array;
		
		
	}
	
	/*
	@PostMapping("/getselecteduser")
	public String getSelectedUser(@RequestBody String x ) {
		System.out.println("-----------------------"+x);
		username = x;
		return null;
		
		
	}*/
	
	
	
	
	
	
	
	
	
	
	
	public void removeNotActiveUsers(String username) {
		System.out.println("?????????????????????"+username); //when tab closes.it passes its username and that username we get here is removed by this function
		array.remove(username);
	}
	
	

}

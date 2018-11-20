package com.chatapplicationdemo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.chatapplicationdemo.model.ChatMessage;


@Component
public class WebSocketEventListener {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);
    @Autowired
    private ChatController chatController; //this autowiring is for getting username in chatcontroller when remove function is called
    @Autowired
    private SendToUserController controller;
    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        logger.info("Received a new web socket connection"); //when someone enters  the username.this will printed
       
    
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        
    
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        System.out.println();
        if(username != null) {
        	logger.info("User left : " + username);
            chatController.removeNotActiveUsers(username);
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setType(ChatMessage.MessageType.LEAVE);
            chatMessage.setSender(username);


            messagingTemplate.convertAndSend("/topic/public", chatMessage); //here the lefting message get to all clients which are subscribed to topics/public
           /* messagingTemplate.convertAndSendToUser(username, "/queue/reply", chatMessage)  (user, destination, payload, headers);*/
           
 
            
            
        }
    }
    
    
}


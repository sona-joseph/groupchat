package com.chatapplicationdemo.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;


@Configuration //@Configuration to indicate that it is a Spring configuration class. 
	@EnableWebSocketMessageBroker //enables WebSocket message handling, backed by a message broker
	public class ChatApplicationConfig implements WebSocketMessageBrokerConfigurer {
        @Override
	    public void registerStompEndpoints(StompEndpointRegistry registry) {
	        registry.addEndpoint("/group-chat").withSockJS(); //anything we an give in place of ws
	        registry.addEndpoint("/single-chat").withSockJS();
	    }
        @Override
      
	    public void configureMessageBroker(MessageBrokerRegistry registry) {
	        registry.setApplicationDestinationPrefixes("/app"); //This prefix will be used to define all the message mappings; for example, "/app/chat.sendmessage
	        registry.enableSimpleBroker("/topic","/queue","/user"); 
//	        registry.enableSimpleBroker("/topic"); ///  enable a simple memory-based message broker to carry the  messages back to the client on destinations prefixed with "/topic".


	        
	    }
}


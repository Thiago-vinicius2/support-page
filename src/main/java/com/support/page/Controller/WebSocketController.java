package com.support.page.Controller;

import com.support.page.Dto.Message.ChatMessageDto;
import com.support.page.Dto.Message.CreateMessageDto;
import com.support.page.Security.TokenService;
import com.support.page.Service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private MessageService messageService;

    @Autowired
    private TokenService tokenService;

    @MessageMapping("/ticket/send")
    public void sendMessage(CreateMessageDto dto, Message<?> message) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String token = accessor.getFirstNativeHeader("Authorization");

        if (token == null || !token.startsWith("Bearer ")) {
            throw new RuntimeException("Token ausente ou inválido no header");
        }

        token = token.replace("Bearer ", "");
        System.out.println("Token recebido no WebSocket: " + token);
        String email = tokenService.validateTokenAndGetSubject(token);

        if (email == null) {
            throw new RuntimeException("Token inválido");
        }

        ChatMessageDto savedMessage = messageService.saveMessage(dto, email);
        messagingTemplate.convertAndSend("/topic/ticket/" + dto.ticketId(), savedMessage);
    }
}

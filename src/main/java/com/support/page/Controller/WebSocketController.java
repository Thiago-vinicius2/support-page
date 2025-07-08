package com.support.page.Controller;

import com.support.page.Dto.Message.ChatMessageDto;
import com.support.page.Dto.Message.CreateMessageDto;
import com.support.page.Service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;


@Controller
public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private MessageService messageService;

    @MessageMapping("/ticket/send")
    public void sendMessage(CreateMessageDto dto, Principal principal) {
        System.out.println("Principal recebido: " + principal);

        if (principal == null) {
            throw new RuntimeException("Usuário não autenticado");
        }

        String email = principal.getName();
        ChatMessageDto savedMessage = messageService.saveMessage(dto, email);
        String destination = "/topic/ticket/" + dto.ticketId();
        messagingTemplate.convertAndSend(destination, savedMessage);
    }
}

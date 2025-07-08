package com.support.page.Controller;

import com.support.page.Dto.Chat.ResponseChatDto;
import com.support.page.Service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ChatController {

    @Autowired
    private ChatService chatService;

    @GetMapping("/chats-tecnico")
    public List<ResponseChatDto> getChatsTecnico() {
        return chatService.listChatsTecnico();
    }

    @GetMapping("/chats-cliente")
    public List<ResponseChatDto> getChatsCliente() {
        return chatService.listChatsCliente();
    }



}

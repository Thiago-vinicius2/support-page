package com.support.page.Controller;

import com.support.page.Dto.Message.ChatMessageDto;
import com.support.page.Dto.Message.CreateMessageDto;
import com.support.page.Service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MessageController {

    @Autowired
    private MessageService messageService;

    @GetMapping("/ticket/{ticketId}/messages")
    public ResponseEntity<List<ChatMessageDto>> allMessages(@PathVariable Long ticketId){
        return ResponseEntity.ok(messageService.getMessagesByTicket(ticketId));
    }

    @PostMapping("/ticket/save-message")
    public ResponseEntity<ChatMessageDto> saveMessage(@RequestBody CreateMessageDto dto, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        ChatMessageDto saveMessage = messageService.saveMessage(dto, token);
        return ResponseEntity.ok(saveMessage);
    }
}

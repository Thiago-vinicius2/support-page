package com.support.page.Service;

import com.support.page.Dto.Message.ChatMessageDto;
import com.support.page.Dto.Message.CreateMessageDto;
import com.support.page.Entity.Message.Message;
import com.support.page.Entity.Ticket.Ticket;
import com.support.page.Entity.User.User;
import com.support.page.Repository.MessageRepository;
import com.support.page.Repository.TicketRepository;
import com.support.page.Repository.UserRepository;
import com.support.page.Security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    public ChatMessageDto toDto(Message message) {
        return new ChatMessageDto(
                message.getTicket().getId(),
                message.getRemetente().getRole().name(),
                message.getRemetente().getNome(),
                message.getConteudo(),
                message.getDataHora()
        );
    }

    public ChatMessageDto saveMessage(CreateMessageDto dto, String token) {

        String email = tokenService.validateTokenAndGetSubject(token);
        if (email == null) {
            throw new RuntimeException("Token inválido ou usuário não autenticado");
        }

        Ticket ticket = ticketRepository.findById(dto.ticketId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket não encontrado"));

        User remetente = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        Message message = new Message();
        message.setTicket(ticket);
        message.setRemetente(remetente);
        message.setConteudo(dto.conteudo());
        message.setDataHora(LocalDateTime.now());

        messageRepository.save(message);

        return new ChatMessageDto(
                ticket.getId(),
                remetente.getRole().name(),
                remetente.getNome(),
                message.getConteudo(),
                message.getDataHora());
    }

    public List<ChatMessageDto> getMessagesByTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket não encontrado"));

        return messageRepository.findByTicket(ticket).stream()
                .map(this::toDto)
                .toList();
    }
}




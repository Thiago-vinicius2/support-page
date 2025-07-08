package com.support.page.Service;

import com.support.page.Dto.Chat.ResponseChatDto;
import com.support.page.Entity.Message.Message;
import com.support.page.Entity.Ticket.StatusTicket;
import com.support.page.Entity.Ticket.Ticket;
import com.support.page.Entity.User.User;
import com.support.page.Repository.MessageRepository;
import com.support.page.Repository.TicketRepository;
import com.support.page.Repository.UserRepository;
import com.support.page.Utils.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class ChatService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    public List<ResponseChatDto> listChatsTecnico(){
        String emailUsuario = AuthUtils.getLoggedUserEmail();
        User usuario = userRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário técnico não encontrado"));

        List<Ticket> tickets = ticketRepository.findByUserResponsavelAndStatusTicket(usuario, StatusTicket.ASSUMIDO);

        List<ResponseChatDto> conversas = new ArrayList<>();

        for (Ticket ticket : tickets) {
            Message ultimaMensagem = messageRepository.findTopByTicketOrderByDataHoraDesc(ticket).orElse(null);

            String textLastMessage = (ultimaMensagem != null) ? ultimaMensagem.getConteudo() : "";
            LocalDateTime dataUltimaMensagem = (ultimaMensagem != null) ? ultimaMensagem.getDataHora() : LocalDateTime.MIN;

            conversas.add(new ResponseChatDto(
                    ticket.getId(),
                    ticket.getUserSolicitante().getNome(),
                    textLastMessage,
                    dataUltimaMensagem
            ));
        }
        conversas.sort(Comparator.comparing(ResponseChatDto::dataUltimaMensagem).reversed());
        return conversas;
    }

    public List<ResponseChatDto> listChatsCliente() {
        String emailUsuario = AuthUtils.getLoggedUserEmail();
        User usuario = userRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        List<Ticket> tickets = ticketRepository.findByUserSolicitante(usuario);

        List<ResponseChatDto> conversas = new ArrayList<>();

        for (Ticket ticket : tickets) {
            if (ticket.getStatusTicket() != StatusTicket.ASSUMIDO || ticket.getUserResponsavel() == null) {
                continue;
            }

            Message ultimaMensagem = messageRepository.findTopByTicketOrderByDataHoraDesc(ticket).orElse(null);

            String textLastMessage = (ultimaMensagem != null) ? ultimaMensagem.getConteudo() : "";
            LocalDateTime dataUltimaMensagem = (ultimaMensagem != null) ? ultimaMensagem.getDataHora() : LocalDateTime.MIN;

            conversas.add(new ResponseChatDto(
                    ticket.getId(),
                    ticket.getUserResponsavel().getNome(),
                    textLastMessage,
                    dataUltimaMensagem
            ));
        }
        conversas.sort(Comparator.comparing(ResponseChatDto::dataUltimaMensagem).reversed());
        return conversas;
    }

}

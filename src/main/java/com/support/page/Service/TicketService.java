package com.support.page.Service;

import com.support.page.Dto.Ticket.AssumeTicketDto;
import com.support.page.Dto.Ticket.CloseTicketDto;
import com.support.page.Dto.Ticket.CreateTicketDto;
import com.support.page.Dto.Ticket.TicketPendenteDto;
import com.support.page.Entity.Ticket.StatusTicket;
import com.support.page.Entity.Ticket.Ticket;
import com.support.page.Entity.User.User;
import com.support.page.Repository.TicketRepository;
import com.support.page.Repository.UserRepository;
import com.support.page.Utils.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    public List<TicketPendenteDto> allTicket(){
        List<Ticket> tickets = ticketRepository.findAll();

        return tickets.stream()
                .map(ticket -> new TicketPendenteDto(
                        ticket.getId(),
                        ticket.getTitulo(),
                        ticket.getDescricao(),
                        ticket.getDataAbertura(),
                        ticket.getUserSolicitante().getNome(),
                        ticket.getUserResponsavel().getNome(),
                        ticket.getUserEncerramento().getNome(),
                        ticket.getStatusTicket()))
                .toList();
    }

    public ResponseEntity<Map<String, String>> createTicket(CreateTicketDto dto) {

        String emailUsuario = AuthUtils.getLoggedUserEmail();

        User solicitante = userRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        Ticket ticket = new Ticket();
        ticket.setTitulo(dto.titulo());
        ticket.setDescricao(dto.descricao());
        ticket.setStatusTicket(StatusTicket.PENDENTE);
        ticket.setDataAbertura(LocalDate.now());
        ticket.setUserSolicitante(solicitante);
        ticket.setDataAssumido(null);
        ticket.setDataEncerramento(null);
        ticket.setUserEncerramento(null);

        ticketRepository.save(ticket);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Ticket Criado. Aguarde o retorno do suporte!"));
    }

    public ResponseEntity<String> assumeTicket(AssumeTicketDto dto) {

        Ticket ticket = ticketRepository.findById(dto.id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket não encontrado"));

        if (ticket.getStatusTicket() != StatusTicket.PENDENTE){
            return ResponseEntity.badRequest().body("Não foi possivel assumir o ticket");
        }

        String emailUsuario = AuthUtils.getLoggedUserEmail();
        User responsavel =  userRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        ticket.setDataAssumido(LocalDate.now());
        ticket.setUserResponsavel(responsavel);
        ticket.setStatusTicket(StatusTicket.ASSUMIDO);

        ticketRepository.save(ticket);

        return ResponseEntity.ok("Ticket assumido com sucesso");
    }

    public ResponseEntity<String> closeTicket(CloseTicketDto dto){
        Ticket ticket = ticketRepository.findById(dto.id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket não encontrado"));

        if (ticket.getStatusTicket() != StatusTicket.ASSUMIDO) {
            return ResponseEntity.badRequest().body("Não foi possivel encerrar o ticket");
        }

        String emailUsuario = AuthUtils.getLoggedUserEmail();
        User userEncerramento = userRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        ticket.setUserEncerramento(userEncerramento);
        ticket.setStatusTicket(StatusTicket.ENCERRADO);
        ticket.setDataEncerramento(LocalDate.now());

        ticketRepository.save(ticket);

        return ResponseEntity.ok("Ticket encerrado com sucesso");
    }
}

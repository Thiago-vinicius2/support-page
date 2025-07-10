package com.support.page.Controller;

import com.support.page.Dto.Ticket.*;
import com.support.page.Service.TicketService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @GetMapping("/all-ticket")
    public List<AllTicketDto> allTicket(){
        return ticketService.allTicket();
    }

    @GetMapping("/info-ticket/{id}")
    public ResponseEntity<InfoTicketDto> infoTicketDto(@PathVariable Long id){
        return ticketService.infoTicket(id);
    }

    @PostMapping("/create-ticket")
    public ResponseEntity<Map<String, String>> createTicket(@Valid @RequestBody CreateTicketDto dto){
        return ticketService.createTicket(dto);
    }

    @PutMapping("/assume-ticket")
    public ResponseEntity<String> assumeTicket(@RequestBody AssumeTicketDto dto){
        return ticketService.assumeTicket(dto);
    }

    @PutMapping("/close-ticket")
    public ResponseEntity<String> closeTicket(@RequestBody CloseTicketDto dto){
        return ticketService.closeTicket(dto);
    }


}

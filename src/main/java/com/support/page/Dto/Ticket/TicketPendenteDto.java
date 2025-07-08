package com.support.page.Dto.Ticket;

import com.support.page.Entity.Ticket.StatusTicket;

import java.time.LocalDate;

public record TicketPendenteDto(
        Long id,
        String titulo,
        String descricao,
        LocalDate dataAbertura,
        String userSolicitante,
        String userResponsavel,
        String userEncerramento,
        StatusTicket statusTicket) {
}

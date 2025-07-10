package com.support.page.Dto.Ticket;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record InfoTicketDto(
        Long id,
        String nomeSolicitante,
        String titulo,
        String descricao,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate dataAbertura
) {
}

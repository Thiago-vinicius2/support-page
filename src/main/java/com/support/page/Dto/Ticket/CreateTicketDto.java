package com.support.page.Dto.Ticket;

import jakarta.validation.constraints.NotNull;

public record CreateTicketDto(
        @NotNull(message = "O titulo é obrigatório")
        String titulo,

        @NotNull(message = "A descricao é obrigatória")
        String descricao) {
}


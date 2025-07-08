package com.support.page.Dto.Chat;

import java.time.LocalDateTime;

public record ResponseChatDto(
        Long ticketId,
        String nomeUserChat,
        String ultimaMensagem,
        LocalDateTime dataUltimaMensagem) {
}

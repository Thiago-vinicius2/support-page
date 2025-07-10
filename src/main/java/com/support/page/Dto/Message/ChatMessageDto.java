package com.support.page.Dto.Message;

import java.time.LocalDateTime;

public record ChatMessageDto(
    Long ticketId,
    String senderType,
    String senderName,
    String senderEmail,
    String content,
    LocalDateTime timestamp) {
}




package com.support.page.Repository;

import com.support.page.Entity.Message.Message;
import com.support.page.Entity.Ticket.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByTicket(Ticket ticket);

    Optional<Message> findTopByTicketOrderByDataHoraDesc(Ticket ticket);
}

package com.support.page.Repository;

import com.support.page.Entity.Ticket.StatusTicket;
import com.support.page.Entity.Ticket.Ticket;
import com.support.page.Entity.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByUserResponsavelAndStatusTicket(User responsavel, StatusTicket statusTicket);

    List<Ticket> findByUserSolicitante(User usuario);
}

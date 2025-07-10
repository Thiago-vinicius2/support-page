package com.support.page.Entity.Ticket;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.support.page.Entity.Message.Message;
import com.support.page.Entity.User.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "ticket")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "O titulo é obrigatório")
    @Column(name = "titulo", nullable = false)
    private String titulo;

    @NotNull(message = "A descrição é obrigatória")
    @Column(name = "descricao", nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @NotNull(message = "A data de abertura é obrigatória")
    @Column(name = "data_abertura", nullable = false)
    private LocalDate dataAbertura;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(name = "data_encerramento")
    private LocalDate dataEncerramento;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(name = "data_assumido")
    private LocalDate dataAssumido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsavel_id")
    @JsonBackReference
    private User userResponsavel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solicitante_id")
    @JsonBackReference
    private User userSolicitante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "encerramento_id")
    @JsonBackReference
    private User userEncerramento;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusTicket statusTicket = StatusTicket.PENDENTE;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    private List<Message> message = new ArrayList<>();
}

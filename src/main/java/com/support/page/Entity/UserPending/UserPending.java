package com.support.page.Entity.UserPending;

import com.support.page.Entity.User.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user_pending")
public class UserPending {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @NotNull(message = "O nome é obrigatório")
    @Column(name = "nome", nullable = false)
    private String nome;

    @Email
    @NotNull(message = "O email é obrigatório")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotNull(message = "A senha é obrigatória")
    @Column(name = "senha", nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "O tipo de usuario é obrigatório")
    @Column(name = "tipo_user", nullable = false)
    private Role roleSelected;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status = Status.PENDENTE;

    @NotNull
    @Column(name = "data_cadastro")
    private LocalDate dataCadastro = LocalDate.now();

    @NotNull
    @Column(name = "tentativas_cadastro")
    private Integer tentativasCadastro;
}

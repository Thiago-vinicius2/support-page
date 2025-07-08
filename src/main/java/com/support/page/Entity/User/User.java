package com.support.page.Entity.User;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.support.page.Entity.Message.Message;
import com.support.page.Entity.Ticket.Ticket;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "usuario")
public class User implements UserDetails {

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
    private Role role;

    @OneToMany(mappedBy = "userResponsavel", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Ticket> ticketsAssumidos = new ArrayList<>();

    @OneToMany(mappedBy = "userSolicitante", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Ticket> ticketsCriados = new ArrayList<>();

    @OneToMany(mappedBy = "userEncerramento", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Ticket> ticketsEncerrados = new ArrayList<>();

    @OneToMany(mappedBy = "remetente", fetch = FetchType.LAZY)
    private List<Message> messages = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

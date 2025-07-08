package com.support.page.Dto.UserPending;

import com.support.page.Entity.User.Role;
import com.support.page.Entity.UserPending.Status;

import java.time.LocalDate;

public record ResponseUserPendingDto(
        Long id,
        String nome,
        String email,
        Role roleSelected,
        Status status,
        LocalDate dataCadastro,
        Integer tentativasCadastro) {
}

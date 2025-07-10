package com.support.page.Dto.UserPending;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.support.page.Entity.User.Role;
import com.support.page.Entity.UserPending.Status;

import java.time.LocalDate;

public record ResponseUserPendingDto(
        Long id,
        String nome,
        String email,
        Role roleSelected,
        Status status,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate dataCadastro,
        Integer tentativasCadastro) {
}

package com.support.page.Dto.User;

import com.support.page.Entity.User.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ApproveUserDto(
        @NotBlank(message = "O email é obrigatório")
        @Email(message = "Informe um email válido")
        String email,

        Role novaRole) {
}

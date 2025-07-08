package com.support.page.Dto.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


public record LoginUserDto(
        @NotBlank(message = "O email é obrigatório")
        @Email(message = "Informe um email válido")
        String email,

        @NotBlank(message = "A senha é obrigatória")
        String senha) {
}

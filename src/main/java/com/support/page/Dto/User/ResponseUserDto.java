package com.support.page.Dto.User;

import com.support.page.Entity.User.Role;

public record ResponseUserDto(
        Long id,
        String nome,
        String email,
        Role role) {
}

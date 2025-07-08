package com.support.page.Utils;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class AuthUtils {

    public static String getLoggedUserEmail() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal() == null) {
            throw new RuntimeException("Usuário não autenticado");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof String email) {
            return email;
        }

        if (principal instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        }

        throw new RuntimeException("Tipo de usuário não suportado");
    }
}

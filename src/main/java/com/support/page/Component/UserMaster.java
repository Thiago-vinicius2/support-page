package com.support.page.Component;

import com.support.page.Entity.User.Role;
import com.support.page.Entity.User.User;
import com.support.page.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMaster implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        String email = "master@admin.com";

        if (userRepository.findByEmail(email).isEmpty()) {
            User master = new User();
            master.setNome("Master");
            master.setEmail(email);
            master.setSenha(passwordEncoder.encode("admin123"));
            master.setRole(Role.valueOf("ADMIN"));

            userRepository.save(master);
            System.out.println("Usuário master criado com sucesso.");
        }
    }
}
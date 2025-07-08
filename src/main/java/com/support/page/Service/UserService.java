package com.support.page.Service;

import com.support.page.Dto.User.ApproveUserDto;
import com.support.page.Dto.User.LoginUserDto;
import com.support.page.Dto.User.RefuseUserDto;
import com.support.page.Dto.User.ResponseUserDto;
import com.support.page.Entity.UserPending.Status;
import com.support.page.Entity.UserPending.UserPending;
import com.support.page.Repository.UserPendingRepository;
import com.support.page.Security.TokenResponse;
import com.support.page.Entity.User.User;
import com.support.page.Repository.UserRepository;
import com.support.page.Security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

   @Autowired
   private UserRepository userRepository;

   @Autowired
   private UserPendingRepository userPendingRepository;

   @Autowired
   private PasswordEncoder passwordEncoder;

   @Autowired
   private TokenService tokenService;

   public List<ResponseUserDto> allUser() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(user -> new ResponseUserDto(user.getId(), user.getNome(), user.getEmail(), user.getRole()))
                .collect(Collectors.toList());
    }

   public ResponseEntity<TokenResponse> login(LoginUserDto dto) {

       Optional<User> usuario = userRepository.findByEmail(dto.email());
       if (usuario.isEmpty() || !passwordEncoder.matches(dto.senha(), usuario.get().getSenha()))
           throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "E-mail ou senha inválido");

       String token = tokenService.generateToken(usuario.get());
       return ResponseEntity.ok(new TokenResponse(token));
   }

   public ResponseEntity<String> approveUser(ApproveUserDto dto) {

        UserPending userPending = userPendingRepository.findByEmail(dto.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhum cadastro foi encontrado!"));

        if (userRepository.findByEmail(userPending.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cadastro já foi aprovado");
        }

       User user = new User();
       user.setNome(userPending.getNome());
       user.setEmail(userPending.getEmail());
       user.setSenha(userPending.getSenha());
       user.setRole(dto.novaRole() != null ? dto.novaRole() : userPending.getRoleSelected());

       userRepository.save(user);
       userPendingRepository.delete(userPending);

        return ResponseEntity.ok("Usuário aprovado e cadastrado com sucesso!");
   }

    public ResponseEntity<String> refuseUser(RefuseUserDto dto) {
        UserPending userPending = userPendingRepository.findByEmail(dto.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cadastro não localizado!"));

        if (userRepository.findByEmail(userPending.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cadastro já foi aprovado");
        }

        userPending.setStatus(Status.RECUSADO);

        userPendingRepository.save(userPending);

        return ResponseEntity.ok("Usuário recusado!");
    }
}

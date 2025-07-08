package com.support.page.Service;

import com.support.page.Dto.UserPending.CreateUserPendingDto;
import com.support.page.Dto.UserPending.ResponseUserPendingDto;
import com.support.page.Entity.UserPending.Status;
import com.support.page.Entity.UserPending.UserPending;
import com.support.page.Repository.UserPendingRepository;
import com.support.page.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Map;


@Service
public class UserPendingService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserPendingRepository userPendingRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<ResponseUserPendingDto> allUserPending(){
        List<UserPending> users = userPendingRepository.findAll();

        return users.stream()
                .map(userPending -> new ResponseUserPendingDto(
                        userPending.getId(),
                        userPending.getNome(),
                        userPending.getEmail(),
                        userPending.getRoleSelected(),
                        userPending.getStatus(),
                        userPending.getDataCadastro(),
                        userPending.getTentativasCadastro()))
                .collect(Collectors.toList());
    }

    public ResponseEntity<Map<String, String>> createUserPending(CreateUserPendingDto dto) {

        if (userRepository.findByEmail(dto.email()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O email já existe");
        }

        Optional<UserPending> userOpt = userPendingRepository.findByEmail(dto.email());

        if (userOpt.isPresent()){

            UserPending usuarioExistente = userOpt.get();

            if (usuarioExistente.getStatus() == Status.PENDENTE){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cadastro está em analise");
            }

            if (usuarioExistente.getStatus() == Status.RECUSADO) {
                if (usuarioExistente.getTentativasCadastro() >= 3) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT,
                            "Este e-mail atingiu o limite de tentativas de cadastro, por favor, tente novamente com outro e-mail.");
                }

                usuarioExistente.setTentativasCadastro(usuarioExistente.getTentativasCadastro() + 1);
                usuarioExistente.setNome(dto.nome());
                usuarioExistente.setEmail(dto.email());
                usuarioExistente.setSenha(passwordEncoder.encode(dto.senha()));
                usuarioExistente.setRoleSelected(dto.roleSelected());
                usuarioExistente.setStatus(Status.PENDENTE);
                usuarioExistente.setDataCadastro(LocalDate.now());

                userPendingRepository.save(usuarioExistente);

                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(Map.of("message", "Nova tentativa de cadastro realizada com sucesso. Aguardando nova análise."));
            }
        }
        UserPending userPending = new UserPending();
        userPending.setNome(dto.nome());
        userPending.setEmail(dto.email());
        userPending.setSenha(passwordEncoder.encode(dto.senha()));
        userPending.setRoleSelected(dto.roleSelected());
        userPending.setStatus(Status.PENDENTE);
        userPending.setDataCadastro(LocalDate.now());
        userPending.setTentativasCadastro(1);

        userPendingRepository.save(userPending);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Cadastro realizado e está pendente de aprovação!"));
    }
}

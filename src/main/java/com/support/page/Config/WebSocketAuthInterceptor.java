package com.support.page.Config;

import com.support.page.Security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


@Component
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    @Autowired
    private TokenService tokenService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.SEND.equals(accessor.getCommand()) || StompCommand.CONNECT.equals(accessor.getCommand())) {
            List<String> authHeaders = accessor.getNativeHeader("Authorization");

            if (authHeaders != null && !authHeaders.isEmpty()) {
                String token = authHeaders.get(0).replace("Bearer ", "");
                String email = tokenService.validateTokenAndGetSubject(token);
                String role = tokenService.getRoleFromToken(token);

                if (email != null) {
                    var authorities = List.of(new SimpleGrantedAuthority(role));
                    var auth = new UsernamePasswordAuthenticationToken(email, null, authorities);

                    accessor.setUser(auth);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    System.out.println("Usuário autenticado: " + email);
                }
            }
        }
        return message;
    }
}


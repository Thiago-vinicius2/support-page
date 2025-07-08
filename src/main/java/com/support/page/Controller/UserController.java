package com.support.page.Controller;

import com.support.page.Dto.User.ApproveUserDto;
import com.support.page.Dto.User.LoginUserDto;
import com.support.page.Dto.User.RefuseUserDto;
import com.support.page.Dto.User.ResponseUserDto;
import com.support.page.Security.TokenResponse;
import com.support.page.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/all-user")
    public List<ResponseUserDto> allUser() {
        return userService.allUser();
    }

    @PostMapping("/auth/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginUserDto dto) {
        return userService.login(dto);
    }

    @PostMapping("/approve-user")
    public ResponseEntity<String> approveUser(@Valid @RequestBody ApproveUserDto dto) {
        return userService.approveUser(dto);
    }

    @PutMapping("/refuse-user")
    public ResponseEntity<String> refuseUser(@RequestBody RefuseUserDto dto){
        return userService.refuseUser(dto);
    }
}

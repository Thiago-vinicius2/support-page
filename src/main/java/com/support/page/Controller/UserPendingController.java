package com.support.page.Controller;

import com.support.page.Dto.UserPending.CreateUserPendingDto;
import com.support.page.Dto.UserPending.ResponseUserPendingDto;
import com.support.page.Service.UserPendingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class UserPendingController {

    @Autowired
    private UserPendingService userPendingService;

    @GetMapping("/all-user-pending")
    public List<ResponseUserPendingDto> allUserPending(){
        return userPendingService.allUserPending();
    }

    @PostMapping("/create-user")
    public ResponseEntity<Map<String, String>> createUser(@Valid @RequestBody CreateUserPendingDto dto) {
        return userPendingService.createUserPending(dto);
    }
}

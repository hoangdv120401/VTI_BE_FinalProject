package com.vti.controller;

import com.vti.dto.AccountDTO;
import com.vti.dto.LoginResponse;
import com.vti.form.LoginRequest;
import com.vti.form.RegisterRequest;
import com.vti.service.ILoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin("*")
public class AuthenticationController {
    @Autowired
    private ILoginService loginService;
    @PostMapping("/auth/register")
    public ResponseEntity<AccountDTO> register(@RequestBody RegisterRequest registerRequest){
        AccountDTO accountDTO = loginService.register(registerRequest);
        return ResponseEntity
                .ok()
                .body(accountDTO);
    }
    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){
        LoginResponse loginResponse = loginService.login(loginRequest);
        return  ResponseEntity
                .ok()
                .body(loginResponse);
    }
}

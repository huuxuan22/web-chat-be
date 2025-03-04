package com.example.webchat.controller;

import com.example.webchat.component.JwtTokenUtils;
import com.example.webchat.dto.LoginDTO;
import com.example.webchat.model.Users;
import com.example.webchat.respone.errors.LoginErrorsDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/")
public class LoginController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtils tokenUtils;

    @PostMapping("login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDTO,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            LoginErrorsDTO loginErrorsDTO = new LoginErrorsDTO();
            bindingResult.getFieldErrors().stream()
                    .forEach(fieldError -> {
                        String field = fieldError.getField();
                        String message = fieldError.getDefaultMessage();
                        switch (field) {
                            case "username":
                                loginErrorsDTO.setUsername(
                                        loginErrorsDTO.getUsername() == null ? message :
                                                loginErrorsDTO.getUsername() + "; " + message
                                );
                                break;
                            case "password":
                                loginDTO.setPassword(
                                        loginDTO.getPassword() == null ? message :
                                                loginErrorsDTO.getPassword() + "; " + message
                                );
                                break;
                        }
                    });
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(loginErrorsDTO);
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getUsername(),loginDTO.getPassword())
            );
            Users user = (Users) authentication.getPrincipal();
            String jwt = tokenUtils.generateToken(user);
            return ResponseEntity.ok(jwt);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Đã có lỗi xảy ra. Vui lòng thử lại sau.: "+ e.getMessage());
        }
    }

}

package com.example.webchat.controller;

import com.example.webchat.dto.UserDTO;
import com.example.webchat.model.Users;
import com.example.webchat.respone.errors.UserErrorsDTO;
import com.example.webchat.service.impl.IUserService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/")
public class UserController {
    @Autowired
    private IUserService userService;
    @Autowired
    private ModelMapper modelMapper;
    @GetMapping("infor")
    public ResponseEntity<?> getInfor(@AuthenticationPrincipal Users users) {
        return ResponseEntity.ok(users);
    }

    @PostMapping("get-list-user")
    public ResponseEntity<?> getListUser(@AuthenticationPrincipal Users users) {
        if (users == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Không có thông tin người dùng");
        }
        List<Users> usersList = userService.searchUsers(users.getUsername());
        return ResponseEntity.ok(usersList);
    }

    @PutMapping("update")
    public ResponseEntity<?> updateUser(@AuthenticationPrincipal Users users,
                                        @Valid @RequestBody UserDTO userDTO,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            UserErrorsDTO userErrorsDTO = new UserErrorsDTO();
            bindingResult.getFieldErrors().stream()
                    .forEach(fieldError -> {
                        String fieldName = fieldError.getField();
                        String errorMessage = fieldError.getDefaultMessage();
                        switch (fieldName) {
                            case "username":
                                userErrorsDTO.setUsername(
                                        userErrorsDTO.getUsername() == null ? errorMessage :
                                                userErrorsDTO.getUsername() + "; " + errorMessage
                                );
                                break;
                            case "password":
                                userErrorsDTO.setPassword(
                                        userErrorsDTO.getPassword() == null ? errorMessage :
                                                userErrorsDTO.getPassword() + "; " + errorMessage
                                );
                                break;
                            case "fullName":
                                userErrorsDTO.setFullName(
                                        userErrorsDTO.getFullName() == null ? errorMessage :
                                                userErrorsDTO.getFullName() + "; " + errorMessage
                                );
                                break;
                        }
                    });
            return ResponseEntity.ok(userErrorsDTO);
        }
        return ResponseEntity.ok(userService.updateUser(modelMapper.map(userDTO, Users.class)));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserDTO userDTO,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            UserErrorsDTO userErrorsDTO = new UserErrorsDTO();
            bindingResult.getFieldErrors().stream()
                    .forEach(fieldError -> {
                        String fieldName = fieldError.getField();
                        String errorMessage = fieldError.getDefaultMessage();
                        switch (fieldName) {
                            case "username":
                                userErrorsDTO.setUsername(
                                        userErrorsDTO.getUsername() == null ? errorMessage :
                                                userErrorsDTO.getUsername() + "; " + errorMessage
                                );
                                break;
                            case "password":
                                userErrorsDTO.setPassword(
                                        userErrorsDTO.getPassword() == null ? errorMessage :
                                                userErrorsDTO.getPassword() + "; " + errorMessage
                                );
                                break;
                            case "fullName":
                                userErrorsDTO.setFullName(
                                        userErrorsDTO.getFullName() == null ? errorMessage :
                                                userErrorsDTO.getFullName() + "; " + errorMessage
                                );
                                break;
                        }
                    });
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userErrorsDTO);
        }
        userService.createUser(userDTO);
        return ResponseEntity.ok().build();
    }
}

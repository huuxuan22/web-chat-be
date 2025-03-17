package com.example.webchat.controller;

import com.example.webchat.component.JwtTokenUtils;
import com.example.webchat.dto.UserDTO;
import com.example.webchat.model.Users;
import com.example.webchat.respone.errors.UserErrorsDTO;
import com.example.webchat.service.impl.IUserService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtils tokenUtils;
    @GetMapping("infor")
    public ResponseEntity<?> getInfor(@AuthenticationPrincipal Users users) {
        return ResponseEntity.ok(users);
    }

    @GetMapping("search")
    public ResponseEntity<?> getListUser(@AuthenticationPrincipal Users users,
                                         @RequestParam("name") String name) {
        if (users == null || name == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Không có thông tin người dùng");
        }
        List<Users> usersList = userService.searchUsers(name.trim());
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
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDTO.getUsername(),userDTO.getPassword())
        );
        Users user = (Users) authentication.getPrincipal();
        String jwt = tokenUtils.generateToken(user);
        return ResponseEntity.ok(jwt);
    }

    /**
     * tìm kiếm người dung để kết bạn
     * @param users
     * @return
     */
    @GetMapping("/search-user")
    public ResponseEntity<?> searchUserForAddFriend(@AuthenticationPrincipal Users users,
                                                    @RequestParam("name") String name) {
        if (users == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("người dùng không tồn tại");
        }
        if (name == null || name.isEmpty()) {
            name = "";
        }
        return ResponseEntity.ok().body(userService.searchUserAddFiends(name.trim(), users.getUserId()));
    }

}

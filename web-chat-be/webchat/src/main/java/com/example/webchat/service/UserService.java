package com.example.webchat.service;

import com.example.webchat.dto.UserDTO;
import com.example.webchat.exception.DataNotFoundException;
import com.example.webchat.model.Roles;
import com.example.webchat.model.Users;
import com.example.webchat.repository.IUserRepository;
import com.example.webchat.service.impl.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {
    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Users findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new DataNotFoundException("Không tìm thấy người dùng này "));
    }

    @Override
    public Users updateUser(Users user) {
        return userRepository.save(user);
    }

    @Override
    public List<Users> searchUsers(String value) {
        return null;
    }

    @Override
    public Users createUser(UserDTO userDTO) {
        Users users = Users.builder()
                .fullName(userDTO.getFullName())
                .role(Roles.builder().roleId(1).roleName(Roles.USER).build())
                .username(userDTO.getUsername())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .thumbnail(null)
                .build();
        return userRepository.save(users);
    }
}

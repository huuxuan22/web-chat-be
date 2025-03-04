package com.example.webchat.service.impl;


import com.example.webchat.dto.UserDTO;
import com.example.webchat.model.Users;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    Users findByUsername(String username);
    Users updateUser(Users user);
    List<Users> searchUsers(String value);
    Users createUser(UserDTO userDTO);
}

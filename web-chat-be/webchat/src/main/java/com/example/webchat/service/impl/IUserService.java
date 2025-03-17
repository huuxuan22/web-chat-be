package com.example.webchat.service.impl;


import com.example.webchat.dto.UserDTO;
import com.example.webchat.model.Users;
import com.example.webchat.request.ListMessageRequest;
import com.example.webchat.respone.UserAddFiend;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface IUserService {
    Users findByUsername(String username);
    Users updateUser(Users user);
    List<Users> searchUsers(String value);
    Users createUser(UserDTO userDTO);
    List<UserAddFiend> searchUserAddFiends(String value,Integer userLogin);
    Users findUserById(Integer id);
}

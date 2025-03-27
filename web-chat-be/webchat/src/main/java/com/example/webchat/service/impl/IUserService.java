package com.example.webchat.service.impl;


import com.example.webchat.dto.UserDTO;
import com.example.webchat.model.Users;
import com.example.webchat.request.LoginGoogleFacebook;
import com.example.webchat.respone.UserAddFiend;

import java.util.List;

public interface IUserService {
    Users findByUsername(String username);
    void updateUser(Users user);
    List<Users> searchUsers(String value);
    Users createUser(UserDTO userDTO);
    List<UserAddFiend> searchUserAddFiends(String value,Integer userLogin);
    Users findUserById(Integer id);
    Users updateThumbnail(Users user);
    String LoginFacebookGoogle(LoginGoogleFacebook loginGoogleFacebook);
}

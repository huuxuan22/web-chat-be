package com.example.webchat.service;

import com.example.webchat.dto.UserDTO;
import com.example.webchat.exception.DataNotFoundException;
import com.example.webchat.model.Roles;
import com.example.webchat.model.Users;
import com.example.webchat.repository.IUserRepository;
import com.example.webchat.request.ListMessageRequest;
import com.example.webchat.respone.UserAddFiend;
import com.example.webchat.service.impl.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
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
        return userRepository.searchByFullname(value);
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

    /**
     *
     * @param value
     * @return
     */
    @Override
    public List<UserAddFiend> searchUserAddFiends(String value,Integer userLogin) {
        List<Object[]> results = userRepository.searchUsersByFullname(value,userLogin);
        List<UserAddFiend> users = new ArrayList<>();

        for (Object[] row : results) {
            users.add(new UserAddFiend(
                    ((Number) row[0]).longValue(),  // Chuyển thành Long
                    (String) row[1],
                    (String) row[2],
                    (String) row[3],
                    ((Number) row[4]).longValue()   // Chuyển thành Long
            ));
        }
        return users;
    }

    @Override
    public Users findUserById(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Không tìm thấy người dùng"));
    }


}

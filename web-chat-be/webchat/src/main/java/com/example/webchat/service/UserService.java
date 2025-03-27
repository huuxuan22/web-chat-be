package com.example.webchat.service;

import com.example.webchat.component.JwtTokenUtils;
import com.example.webchat.dto.UserDTO;
import com.example.webchat.exception.DataNotFoundException;
import com.example.webchat.model.Roles;
import com.example.webchat.model.Users;
import com.example.webchat.repository.IUserRepository;
import com.example.webchat.request.LoginGoogleFacebook;
import com.example.webchat.respone.UserAddFiend;
import com.example.webchat.service.impl.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements IUserService {
    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtils tokenUtils;

    @Override
    public Users findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new DataNotFoundException("Không tìm thấy người dùng này "));
    }

    @Override
    public void updateUser(Users user) {
         userRepository.updateUser(user.getFullName(),user.getPassword(),user.getUserId());
    }

    @Override
    public List<Users> searchUsers(String value) {
        List<Users> userSearch = userRepository.searchByFullname(value);
        for (Users user : userSearch) {
            if (user.getThumbnail() != null) {
                user.setThumbnail("http://localhost:8080/api/user/image/" + user.getThumbnail());
            }
        }
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
                    (String) row[2] == null ? null : "http://localhost:8080/api/user/image/" + row[2],
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

    @Override
    public Users updateThumbnail(Users user) {
        return userRepository.save(user);
    }

    @Override
    public String LoginFacebookGoogle(LoginGoogleFacebook loginGoogleFacebook) {
        Optional<Users> userOptional = userRepository.findByFullnameGoogleFaceBook(loginGoogleFacebook.getFullName());
        Users user;

        if (userOptional.isPresent()) {
            user = userOptional.get();
        } else {
            String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
            String username = (loginGoogleFacebook.getUsername() == null)
                    ? uuid + loginGoogleFacebook.getFullName()
                    : loginGoogleFacebook.getUsername();

            String randomPassword = UUID.randomUUID().toString(); // Không dùng để login, chỉ để mã hóa
            user = Users.builder()
                    .fullName(loginGoogleFacebook.getFullName())
                    .role(Roles.builder().roleId(2).roleName(Roles.USER_FACEBOOK_GOOGLE).build())
                    .username(username)
                    .password(passwordEncoder.encode(randomPassword)) // chỉ mã hóa cho đủ field
                    .thumbnail(null)
                    .build();

            userRepository.save(user);
        }

        // Bỏ authenticate vì user đã đăng nhập từ Google/Facebook
        String jwt = tokenUtils.generateToken(user);
        return jwt;
    }




}

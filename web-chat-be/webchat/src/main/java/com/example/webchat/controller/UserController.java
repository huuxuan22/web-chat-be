package com.example.webchat.controller;

import com.example.webchat.component.JwtTokenUtils;
import com.example.webchat.dto.UpdateUserDTO;
import com.example.webchat.dto.UserDTO;
import com.example.webchat.model.Users;
import com.example.webchat.request.LoginGoogleFacebook;
import com.example.webchat.respone.errors.UserErrorUpdateDTO;
import com.example.webchat.respone.errors.UserErrorsDTO;
import com.example.webchat.service.RedisService;
import com.example.webchat.service.impl.IUserService;
import com.example.webchat.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    private Utils utils;

    @Autowired
    private JwtTokenUtils tokenUtils;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private RedisService redisService;
    @GetMapping("infor")
    public ResponseEntity<?> getInfor(@AuthenticationPrincipal Users users) {
        String name = users.getThumbnail();
        if (name != null) {
            users.setThumbnail("http://localhost:8080/api/user/image/" + name);
        }
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
                                        @Valid @RequestBody UpdateUserDTO updateUserDTO,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            UserErrorUpdateDTO userErrorsDTO = new UserErrorUpdateDTO();
            bindingResult.getFieldErrors().stream()
                    .forEach(fieldError -> {
                        String fieldName = fieldError.getField();
                        String errorMessage = fieldError.getDefaultMessage();
                        switch (fieldName) {
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
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        users.setFullName(updateUserDTO.getFullName());
        userService.updateUser(users);
        return ResponseEntity.ok("đã cập nhật thành công");
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

    @PostMapping( value = "upload-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> upload(@AuthenticationPrincipal Users users,
                                    @RequestParam("file") MultipartFile file) throws IOException {
        if (file.getSize() == 0) {
            return ResponseEntity.ok("không có file ảnh");
        }
        if (file.getSize() > 1024 * 1024) {
            return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("anh qua lon, lon hon 10Byte");
        }
        String contentType = file.getContentType();
        if (contentType == null || !contentType.contains("image/")) {
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("unsupported image type");
        }
        String fileName = utils.storeFile(file,users.getThumbnail());
        if (file.equals(users.getThumbnail())) {
            return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("ảnh đại diện cũ");
        }
        users.setThumbnail(fileName);
        userService.updateThumbnail(users);
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }
    @GetMapping("/image/{imageName}")
    public ResponseEntity<?> getImage(@PathVariable String imageName) {
        try {
            Path imagePath = Paths.get("uploads/" + imageName);
            UrlResource resource = new UrlResource(imagePath.toUri());
            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(resource);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy hình ảnh");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy hình ảnh");
        }
    }

    @PostMapping("/login-google-facebook")
    public ResponseEntity<?> loginGoogle (@RequestBody LoginGoogleFacebook loginGoogleFacebook) {
        String token = userService.LoginFacebookGoogle(loginGoogleFacebook);
        return ResponseEntity.ok().body(token);
    }

    @PostMapping("logout")
    public ResponseEntity<?> logout(@AuthenticationPrincipal Users user,
                                    HttpServletRequest request) {
        String token = GetTokenFromRequest.getTokenFromRequest(request);
        if (token != null) {
            String username = tokenUtils.extractUserName(token);
            redisService.addTokenList(username,token);
            return ResponseEntity.ok("Đăng xuất thành công");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Không tìm thấy token hợp lệ");
        }
    }

    public class GetTokenFromRequest {
        public static String getTokenFromRequest(HttpServletRequest request) {
            String bearerToken = request.getHeader("Authorization");

            if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
                return bearerToken.substring(7); // Cắt bỏ "Bearer " để lấy token
            }
            return null;
        }
    }
}

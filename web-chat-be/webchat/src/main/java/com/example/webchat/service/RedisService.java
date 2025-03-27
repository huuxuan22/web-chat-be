package com.example.webchat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RedisService {
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    /**
     * them token vao trong redis
     * @param username ten nguoi dung
     * @param token token cua nguoi dung
     */
    public void addTokenList(String username,String token) {
        ListOperations<String, String> ops = redisTemplate.opsForList();
        ops.rightPush(username,token);
    }

    /**
     * lay toan bo token cua nguoi dung
     * @param username tai khoan cua nguoi dung
     * @return danh sach token
     */
    public List<String> getTokenList(String username) {
        ListOperations<String,String> ops = redisTemplate.opsForList();
        return ops.range(username,0,-1);
    }

    /**
     * xoa khoi token ra redis
     * @param username tai khoan nguoi dung
     * @param token token cua nguoi dung
     */
    public void removeTokenList(String username,String token) {
        ListOperations<String,String> ops = redisTemplate.opsForList();
        ops.leftPush(username,token);
    }

    /**
     * xoa het token cua nguoi dung theo username
     * @param username tai khoan nguoi dung
     */
    public void deleteTokens(String username) {
        redisTemplate.delete(username);
    }
}

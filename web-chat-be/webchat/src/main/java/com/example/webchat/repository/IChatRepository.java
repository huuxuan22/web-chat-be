package com.example.webchat.repository;

import com.example.webchat.model.Chat;
import com.example.webchat.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IChatRepository extends JpaRepository<Chat, Integer> {

}

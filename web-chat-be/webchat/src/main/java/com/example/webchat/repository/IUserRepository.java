package com.example.webchat.repository;

import com.example.webchat.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<Users, Integer> {
    @Query("select u from  Users u where u.username = :username")
    Optional<Users> findByUsername(@Param("username") String username);

    @Query("select u from Users u where u.username = :username")
    Optional<Users> findByEmail(@Param("username") String username);

    @Query("select u from Users u where u.fullName like concat('%', :param, '%')")
    List<Users> findListByFullName(@Param("param") String param);

    @Query("select u from Users  u where u.fullName = :fullname")
    Optional<Users> findByFullname(@Param("fullname") String fullname);

    @Query("select u from Users  u where u.fullName = concat('%',:fulName, '%' ) ")
    List<Users> searchByFullname(@Param("fullname") String fullname);
}

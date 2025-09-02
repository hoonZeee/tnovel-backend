package com.example.tnovel_backend.repository.user;

import com.example.tnovel_backend.repository.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {


    Optional<User> findByUsername(String username);
    Optional<User> findByName(String name);
}

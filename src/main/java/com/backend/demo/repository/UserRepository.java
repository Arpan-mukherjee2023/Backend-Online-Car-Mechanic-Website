package com.backend.demo.repository;

import com.backend.demo.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, String>{
    boolean existsByUserId(String userId);

    User findByEmailAndPassword(String email, String password);
    Optional<User> findByUserIdAndPassword(String userId, String Password);
    Optional<User> findByUserId(String userId);
}



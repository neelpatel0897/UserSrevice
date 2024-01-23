package dev.neel.userSrevice.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.neel.userSrevice.models.User;


public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);
    
}
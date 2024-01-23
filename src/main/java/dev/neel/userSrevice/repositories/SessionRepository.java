package dev.neel.userSrevice.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.neel.userSrevice.models.Session;

public interface SessionRepository extends JpaRepository<SessionRepository,Long> {

    void save(Session session);

    Optional<Session> findByTokenAndUser_Id(String token, Long userId);
    
}
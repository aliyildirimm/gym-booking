package com.gym.authservice.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gym.authservice.domain.User;


@Repository
public interface UserJpaRepository extends JpaRepository<User, Long> {
}

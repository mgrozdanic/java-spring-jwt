package com.example.demo.repositories;

import com.example.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UsersRepository extends JpaRepository<User, Long> {
    @Query("SELECT COUNT(u) from User u WHERE u.email = :email")
    int emailUnique(@Param("email") String email);
    User findByEmail(String email);
}

package com.taller_4.taller_4.Repositories;

import com.taller_4.taller_4.Models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<UserModel, Long> {
    UserModel findByEmail(String email);
    UserModel findByUsername(String username);

    @Query
    (value = "SELECT * FROM users WHERE email = ?1 AND password = ?2", nativeQuery = true)
    UserModel findByEmailAndPassword(String email, String password);

    @Query
    (value = "SELECT * FROM users WHERE username = ?1 AND password = ?2", nativeQuery = true)
    UserModel findByUsernameAndPassword(String username, String password);
    @Query
    (value = "SELECT * FROM users WHERE email = ?1", nativeQuery = true)
    Optional<UserModel> findByEmailOptional(String email);

}

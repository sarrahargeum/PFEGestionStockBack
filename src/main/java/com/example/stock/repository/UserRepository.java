package com.example.stock.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.stock.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {


    Optional<User> findByEmail(String email);

    Optional<Object> findOneByEmailIgnoreCase(String email);
    


}

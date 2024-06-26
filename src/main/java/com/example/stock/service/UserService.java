package com.example.stock.service;

import com.example.stock.model.User;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface UserService {

    User editProfil(User u);
    
    User retrieveUser (Integer userId);
    
    List<User> findAll();
    
    void deleteUser(Integer id);
    
	
	 User updateUser(Integer id, User User) ;
	  
}

package com.example.stock.service;

import com.example.stock.model.User;

import java.util.List;


public interface UserService {

    User editProfil(User u);
    
    User retrieveUser (Integer userId);
    
    List<User> findAll();
    
    void deleteUser(Integer id);
    
	
	 User updateUser(Integer id, User User) ;
	  
}

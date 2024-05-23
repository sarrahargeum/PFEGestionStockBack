package com.example.stock.controller;

import com.example.stock.config.ApplicationConfig;
import com.example.stock.model.Article;
import com.example.stock.model.User;
import com.example.stock.service.UserService;
import com.example.stock.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    ApplicationConfig applicationConfig;
    @Autowired
     UserService userServices;

    @Autowired
    UserRepository userRepository;

    /*@PutMapping("/{id}")
    public ResponseEntity<User> editProfile(@PathVariable Integer id, @RequestBody User u) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            existingUser.setFirstname(u.getFirstname());
            existingUser.setLastname(u.getLastname());
            existingUser.setEmail(u.getEmail());

            User editedUser = userServices.editProfil(existingUser);

            return ResponseEntity.ok(editedUser);
        } else {

            return ResponseEntity.notFound().build();
        }
    }*/
    @PutMapping("/updateUser")
    public User updateUser(@RequestBody User u) {
    	 return userServices.updateUser(u);
    }


    @GetMapping("/retrieve-user/{id}")
    public User retrieveUser(@PathVariable("id") Integer userId) {
        return userServices.retrieveUser(userId);
    }

    @GetMapping("/all")
    @ResponseBody
    public List<User> findAll() {
        return userServices.findAll();
    }
    
    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable Integer id) {
        userServices.deleteUser(id);
    }

    @GetMapping("/searchUserBynomEtprenom/{firstname}/{lastname}")
    public int searchEtudiantBynomEtprenom(@PathVariable("firstname") String firstname, @PathVariable("lastname") String lastname) {
        return userServices.searchUserBynomEtprenom(firstname, lastname);
    }

}

package com.example.stock.auth;

import com.example.stock.config.JwtAuthenticationFilter;
import com.example.stock.config.JwtService;
import com.example.stock.exception.ErrorMessage;
import com.example.stock.model.User;
import com.example.stock.repository.UserRepository;
import com.example.stock.service.UserService;
import com.example.stock.service.metiers.MailServiceImpl;
import jakarta.mail.MessagingException;


import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;



@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationConrtroller {

     JwtService jwtService;
      JwtAuthenticationFilter jwtAuthenticationFilter;
     AuthenticationManagerBuilder authenticationManagerBuilder;

    private  Logger log = LoggerFactory.getLogger(User.class);
    @Autowired
    AuthenticationService service;

    @Autowired
    MailServiceImpl mailSenderService;
    
    @PostMapping("/register")
    public void register(@RequestBody RegisterRequest request) {
    	 
        User user = service.register(request);
        	String email = user.getEmail();
    	    String password = user.getPassword();
    	    String text = "Votre compte est prêt, mais encore désactivé. \n" +
                    "Email: " + email + "\n" +
                    "Mot de passe: " + password;
        String subject=" votre compte est préte";
      //  User user = service.register(request);
     
        try {
            mailSenderService.sendSimpleMessageToNewUser(user.getEmail(),subject,text);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }


    @PutMapping("/admin/activated")
    public ResponseEntity<User> updateUserActivated(@Valid @RequestBody User userDTO) throws MessagingException {
        String text = "Votre compte est Activé, merci de cliquer sur le lien ci-dessous pour y accéder :\n" +
                "\n" + "http://localhost:4200/login";
        String subject = " Activation de compte";
        Optional<User> updatedUserAcivated = service.updateUserActivated(userDTO);
        User us = updatedUserAcivated.get();
        mailSenderService.sendSimpleMessageToNewUser(us.getEmail(), subject, text);
        return new ResponseEntity(updatedUserAcivated, HttpStatus.OK);

    }

}

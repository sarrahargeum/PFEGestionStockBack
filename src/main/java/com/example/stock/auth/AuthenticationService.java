package com.example.stock.auth;

import com.example.stock.config.JwtService;
import com.example.stock.model.Magasin;
import com.example.stock.model.Roles;
import com.example.stock.model.User;
import com.example.stock.repository.MagasinRepository;
import com.example.stock.repository.RolesRepository;
import com.example.stock.service.metiers.MailServiceImpl;
import com.example.stock.repository.UserRepository;

import lombok.RequiredArgsConstructor;


import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

import java.util.Optional;



@Service
@RequiredArgsConstructor

public class AuthenticationService {
    private final Logger log = LoggerFactory.getLogger(AuthenticationService.class);
     @Autowired
     UserRepository repository;
    
      @Autowired
      PasswordEncoder passwordEncoder;
    
      @Autowired
      JwtService jwtService;
     
      @Autowired
      AuthenticationManager authenticationManager;
  
      private static  SecureRandom SECURE_RANDOM = new SecureRandom();

      @Autowired
        MailServiceImpl mailService;
  
      @Autowired
      UserRepository userRepository;
    
      @Autowired
      RolesRepository rolesRepository;
      
      @Autowired
      MagasinRepository magasinRepositrory;

  
   
   
    public User register(RegisterRequest userDTO) {

        /*userRepository
                .findOneByEmailIgnoreCase(userDTO.getEmail())
                .ifPresent(existingUser -> {
                    boolean removed = removeNonActivatedUser((User) existingUser);
                    if (!removed) {
                       // throw new EmailAlreadyUsedException();
                    }
                });*/
        User newUser = new User();
        String encryptedPassword = passwordEncoder.encode(userDTO.getPassword());
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setFirstname(userDTO.getFirstname());
        newUser.setLastname(userDTO.getLastname());
        if (userDTO.getEmail() != null) {
            newUser.setEmail(userDTO.getEmail().toLowerCase());
        }
       //get Roles
      
        if (userDTO.getRoles() != null && userDTO.getRoles().getId() != null) {
            Roles userRoles = rolesRepository.findById(userDTO.getRoles().getId()).orElse(null);
            if (userRoles != null) {
                // Assuming a user has only one role (modify as needed)
                newUser.setRole(userRoles);
            }
            }
        
        //get magasin
  
        if (userDTO.getMagasins() != null && userDTO.getMagasins().getId() != null) {
            Magasin magasin = magasinRepositrory.findById(userDTO.getMagasins().getId()).orElse(null);
            if (magasin != null) {
                newUser.setMagasin(magasin);
            }}
    
        // new user is active
        newUser.setActivated(false);
        // new user gets registration key
        newUser.setActivationKey(RandomStringUtils.random(20, 0, 0, true, true, (char[])null, SECURE_RANDOM));
       newUser.setRole(userDTO.getRoles());
        userRepository.save(newUser);

        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }



    public Optional<User> updateUserActivated(User userDTO) {
        return Optional
                .of(userRepository.findById(userDTO.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(user -> {
                    user.setActivated(userDTO.isActivated());
                    userRepository.save(user);
                    log.debug("Changed Information for User: {}", user);
                    return user;
                }) ;
   }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
    	 authenticationManager.authenticate(
    		        new UsernamePasswordAuthenticationToken(
    		            request.getEmail(),
    		            request.getPassword()
    		        )
    		    );
        User user = repository.findByEmail(request.getEmail())
                .orElseThrow();
        if(!user.isActivated() ){
            return AuthenticationResponse.builder()
                    .message("Votre compte est desactivé")
                    .build();
        }
        else{
        String jwtToken = jwtService.generateToken(user);
		Roles role = user.getRole();
		
		
        return AuthenticationResponse.builder()
                .accesstoken(jwtToken)
                .roles(role)
                .user(user)
                .build();
    }
        }


}



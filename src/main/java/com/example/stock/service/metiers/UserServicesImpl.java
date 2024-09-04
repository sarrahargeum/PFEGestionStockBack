package com.example.stock.service.metiers;


import com.example.stock.model.User;
import com.example.stock.repository.UserRepository;
import com.example.stock.service.UserService;

import lombok.extern.slf4j.Slf4j;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class UserServicesImpl implements UserService {
    private final Logger log = LoggerFactory.getLogger(User.class);

    @Autowired
    UserRepository userRepository;

    public User editProfil(User u) {

        return userRepository.save(u);
    }
    
    
    public User retrieveUser (Integer userId){
        User u = userRepository.findById(userId).get();
        return  u;
    }

    
	@Override
	public List<User> findAll() {
		if(userRepository.findAll()==null) {
		 log.error("error fetching users " );
		}
		return userRepository.findAll();
	}

  
	@Override
	public void deleteUser(Integer id) {
		userRepository.deleteById(id);
		
	}


	
	

	
	@Override
	public User updateUser(Integer id, User user) {
	    Optional<User> userInfo = userRepository.findById(id);
	    User u;
	    
	    if (userInfo.isPresent()) {
	        User existingUser = userInfo.get();
	        existingUser.setFirstname(user.getFirstname());
	        existingUser.setLastname(user.getLastname());
	        existingUser.setEmail(user.getEmail());
	   
	        u = userRepository.save(existingUser);
	    } else {
	        u = userRepository.save(user);
	    }
	    
	    return u;
	}

}

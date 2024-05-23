package com.example.stock.service.metiers;

import com.example.stock.model.Article;
import com.example.stock.model.User;
import com.example.stock.repository.UserRepository;
import com.example.stock.service.UserService;

import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


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
	public int searchUserBynomEtprenom(String firstname, String lastname) {
		return userRepository.searchUserBynomEtprenom(firstname, lastname);
	}
	
	
	@Override
	public User updateUser(User u) {
		   
		  log.info(""+u);
	     
	     return  userRepository .save(u);
	}


}

package com.example.stock.service.metiers;

import com.example.stock.model.Roles;
import com.example.stock.repository.RolesRepository;
import com.example.stock.service.RolesService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RolesServiceImpl implements RolesService {
	
	   
		@Autowired
		RolesRepository roleRepository;
		
		  @Override
		    public List<Roles> findAll() {
		        return roleRepository.findAll().stream().collect(Collectors.toList());
		    }
		  
		 
		    
		    
		    public List<Roles> getAllRoleNames() {
		        List<Roles> roles = roleRepository.findAll();
		        return roles.stream()
		                    .map(role -> {
		                    	Roles dto = new Roles();
		                        dto.setId(role.getId());
		                        dto.setNomRole(role.getNomRole());
		                        return dto;
		                    })
		                    .collect(Collectors.toList());
		    }
}

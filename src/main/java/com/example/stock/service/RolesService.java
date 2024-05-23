package com.example.stock.service;

import java.util.List;

import com.example.stock.model.Roles;

public interface RolesService {
	
	  List<Roles> findAll();
	  List<Roles> getAllRoleNames() ;
}

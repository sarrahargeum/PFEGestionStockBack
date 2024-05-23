package com.example.stock.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


import com.example.stock.model.Roles;

import com.example.stock.service.RolesService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/roles")
public class RolesController {
	
    @Autowired
    RolesService rolesService;

  

    @GetMapping("/all")
    @ResponseBody
    public List<Roles> findAll() {

        return rolesService.findAll();
    }
    
    @GetMapping("/names")
    public List<Roles> getAllRoleNames() {
        return rolesService.getAllRoleNames();
    }
}

package com.example.stock.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.stock.dto.ClientDto;
import com.example.stock.model.Client;
import com.example.stock.service.ClientService;@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/client")
public class ClientController {
	
	@Autowired
    ClientService clientService;

    @PostMapping("/addCli")
    public  ClientDto ajouterClient(@RequestBody ClientDto dto){
        return clientService.ajouterClient(dto);
        }


    @GetMapping("/all")
    public List<ClientDto> findAll() {
      return clientService.findAll();
    }

    @PutMapping("/update/{id}")
    public void updateClient(@PathVariable("id") Integer id,@RequestBody Client client) {
        clientService.updateClient(id, client);
    }
    
    @GetMapping("/retrieve-client/{id}")
    public Client retrieveClient(@PathVariable("id") Integer clientId) {
        return clientService.retrieveClient(clientId);
    }
    
    @DeleteMapping("/delete/{id}")
    public void deleteClient(@PathVariable Integer id) {
        clientService.deleteClient(id);
    }

}

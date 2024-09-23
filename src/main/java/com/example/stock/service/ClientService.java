package com.example.stock.service;

import java.util.List;

import com.example.stock.dto.ClientDto;
import com.example.stock.model.Client;



public interface ClientService {
	
	ClientDto ajouterClient(ClientDto dto);
	
	// public ClientDto findById(Integer id);
	 void  updateClient(Integer id, Client cli);
	Client deleteClient(Integer id);
   List<ClientDto> findAll();
   ClientDto retrieveClient (Integer id);

long countClients();

}

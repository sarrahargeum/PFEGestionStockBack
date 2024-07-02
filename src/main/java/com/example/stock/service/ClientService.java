package com.example.stock.service;

import java.util.List;

import com.example.stock.model.Client;



public interface ClientService {
	
	Client ajouterClient(Client cli);
	 void  updateClient(Integer id, Client cli);
	Client deleteClient(Integer id);
   List<Client> findAll();
   Client retrieveClient (Integer clientId);

}

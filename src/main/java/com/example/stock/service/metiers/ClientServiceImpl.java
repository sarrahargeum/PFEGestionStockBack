package com.example.stock.service.metiers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.stock.model.Client;
import com.example.stock.repository.ClientRepository;
import com.example.stock.service.ClientService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ClientServiceImpl implements ClientService {
	
	@Autowired
	ClientRepository clientRepository;
	
	
	
	@Override
	public Client ajouterClient(Client cli ) {
		 return clientRepository.save(cli);
	}


	
	public void updateClient(Integer id, Client client) {
	  
	    Optional<Client> cliInfo = clientRepository.findById(id);

	    if (cliInfo.isPresent()) {
	    	Client cl = cliInfo.get();
	        cl.setNom(client.getNom()); 
	        cl.setPrenom(client.getPrenom());
	        cl.setAdresse(client.getAdresse());
	        cl.setNumTel(client.getNumTel());

	        clientRepository.save(cl); 
	    } else {
	        System.out.println("client with ID = " + id + " not found.");
	    }
	
	  }
	
	

	@Override
	public Client deleteClient(Integer id) {
		clientRepository.deleteById(id);
        return null;
	}

	@Override
	public List<Client> findAll() {
        return clientRepository.findAll().stream().collect(Collectors.toList());
	}
	
    public Client retrieveClient (Integer clientId){
    	Client cl = clientRepository.findById(clientId).get();
        return  cl;
    }
	

}

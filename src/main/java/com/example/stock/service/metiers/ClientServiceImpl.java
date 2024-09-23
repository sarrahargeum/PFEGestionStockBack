package com.example.stock.service.metiers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.stock.dto.ClientDto;
import com.example.stock.dto.FournisseurDto;
import com.example.stock.exception.EntityNotFoundException;
import com.example.stock.model.Client;
import com.example.stock.repository.ClientRepository;
import com.example.stock.service.ClientService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ClientServiceImpl implements ClientService {
	
	@Autowired
	ClientRepository clientRepository;
	
	
	
	  public ClientDto retrieveClient(ClientDto dto) {
		 

		    return ClientDto.fromEntity(
		        clientRepository.save(
		            ClientDto.toEntity(dto)
		        )
		    );
		  }

		  public ClientDto retrieveClient( Integer id) {
		    if (id == null) {
		      log.error("Client ID is null");
		      return null;
		    }
		    return clientRepository.findById(id)
		        .map(ClientDto::fromEntity)
		        .orElseThrow(() -> new EntityNotFoundException(
		            "Aucun Client avec l'ID = " + id + " n' ete trouve dans la BDD")
		        );
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
	public List<ClientDto> findAll() {
		return clientRepository.findAll().stream()
		        .map(ClientDto::fromEntity)
		        .collect(Collectors.toList());
	}
	
	
  

    
    

    public long countClients() {
        return clientRepository.count();
    }

	@Override
	public ClientDto ajouterClient(ClientDto dto) {
		// TODO Auto-generated method stub
		return null;
	}


	

}

package com.example.stock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.stock.model.Client;
@Repository
public interface ClientRepository  extends JpaRepository<Client, Integer>  {

}

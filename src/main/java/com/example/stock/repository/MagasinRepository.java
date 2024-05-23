package com.example.stock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.example.stock.model.Magasin;

@Repository
public interface MagasinRepository extends JpaRepository<Magasin, Integer>{

}

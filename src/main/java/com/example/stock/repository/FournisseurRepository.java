package com.example.stock.repository;

import com.example.stock.model.Fournisseur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface FournisseurRepository extends JpaRepository<Fournisseur, Integer> {

    long count();

}


package com.example.stock.repository;

import com.example.stock.model.BonEntreFournisseur;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BonEntreFournisseurRepository extends JpaRepository<BonEntreFournisseur, Integer> {
	
    Optional<BonEntreFournisseur> findBonEntreFournisseurByCode(String code);

}

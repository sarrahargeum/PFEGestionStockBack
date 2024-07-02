package com.example.stock.repository;

import com.example.stock.model.BonEntree;
import com.example.stock.model.BonSortie;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BonSortieRepository extends JpaRepository<BonSortie, Integer> {
  
	Optional<BonSortie> findBonSortieByCode(String code);

}

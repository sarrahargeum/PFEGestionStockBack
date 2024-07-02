package com.example.stock.repository;

import com.example.stock.model.BonEntree;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BonEntreeRepository extends JpaRepository<BonEntree, Integer> {
	
    Optional<BonEntree> findBonEntreeByCode(String code);

}

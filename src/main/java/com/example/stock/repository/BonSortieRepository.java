package com.example.stock.repository;

import com.example.stock.model.BonSortie;
import com.example.stock.model.Client;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BonSortieRepository extends JpaRepository<BonSortie, Integer> {
  
	Optional<BonSortie> findBonSortieByCode(String code);
	
	
    List<BonSortie> findAllByClientId(Integer id);
    long count();


    @Query("SELECT b FROM BonSortie b LEFT JOIN FETCH b.ligneSorties WHERE b.id = :id")
    Optional<BonSortie> findByIdWithLigneSorties(@Param("id") Integer id);
}

package com.example.stock.repository;

import com.example.stock.model.BonSortie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BonSortieRepository extends JpaRepository<BonSortie, Integer> {
}

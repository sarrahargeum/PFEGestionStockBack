package com.example.stock.repository;

import com.example.stock.model.LigneSortie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface LigneSortieRepository extends JpaRepository<LigneSortie, Integer> {
}

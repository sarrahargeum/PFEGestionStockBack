package com.example.stock.repository;

import com.example.stock.model.BonEntree;


import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BonEntreeRepository extends JpaRepository<BonEntree, Integer> {
	
    Optional<BonEntree> findBonEntreeByCode(String code);
    List<BonEntree> findAllByFournisseurId(Integer id);

    BonEntree findFirstByOrderByIdDesc();

    
    @Query("SELECT MONTH(b.dateCommande), COUNT(b) FROM BonEntree b GROUP BY MONTH(b.dateCommande)")
    List<Object[]> countBonEntreeByMonth();

}

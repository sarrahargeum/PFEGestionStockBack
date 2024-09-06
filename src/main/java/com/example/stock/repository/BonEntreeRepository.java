package com.example.stock.repository;

import com.example.stock.model.BonEntree;

import net.bytebuddy.asm.Advice.OffsetMapping.Sort;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BonEntreeRepository extends JpaRepository<BonEntree, Integer> {
	
    Optional<BonEntree> findBonEntreeByCode(String code);
    List<BonEntree> findAllByFournisseurId(Integer id);

    BonEntree findFirstByOrderByIdDesc();


}

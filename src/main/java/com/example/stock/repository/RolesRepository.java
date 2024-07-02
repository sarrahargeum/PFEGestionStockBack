package com.example.stock.repository;

import com.example.stock.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface RolesRepository extends JpaRepository<Roles, Integer> {
}

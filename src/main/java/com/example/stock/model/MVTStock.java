package com.example.stock.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Date;

@Data
@Entity
@Builder
@Table(name = "Mvtstock")

public class MVTStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer quantite;
    private Instant dateMvt;
    
    @Column(name = "typestock")
    @Enumerated(EnumType.STRING)
    private TypeStock typestock;

    @Column(name = "idmagasin")
    private Integer idMagasin;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "idarticle")
    private Article article;


    
}

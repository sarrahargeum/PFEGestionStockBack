package com.example.stock.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ligneSortieclient")
public class LigneSortieClient {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer quantite;

    private Float prixUnitaire;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "idarticle")
    private Article article;

    @Column(name = "idmagasin")
    private Integer idMagasin;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "bon_sortie_client_id")
    private BonSortieClient bonSortieClient;
}

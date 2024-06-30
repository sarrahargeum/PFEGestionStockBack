package com.example.stock.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bonEntreClient")
public class BonSortieClient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String code;
    private Date dateCommande;

    @ManyToOne
    @JoinColumn(name = "idUser")
    private  User user;
    
    @Column(name = "etatcommande")
    @Enumerated(EnumType.STRING)
    private EtatCommande etatCommande;

    @Column(name = "idmagasin")
    private Integer idMagasin;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "bonSortieClient")
    private List<LigneSortieClient> ligneSortieClientsClients;
}

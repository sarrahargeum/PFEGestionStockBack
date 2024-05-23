package com.example.stock.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;

import jakarta.persistence.CascadeType;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "article")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String code;
    private String designation;
    private Float prix;
    private Float tauxTva;
 
    private String image;
 //   @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "idcategory")
    private Category category;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "idmagasin")
    private Magasin magasin;


    @OneToMany(mappedBy = "article")
    private List<Stock>  stocks;

    @OneToMany(mappedBy = "article")
    private List<LigneEntreeFournisseur> ligneEntreeFournisseur;

    @OneToMany(mappedBy = "article")
    private List<LigneSortieClient> ligneSortieClients;

    @OneToMany(mappedBy = "article")
    private List<LigneEntreeFournisseur> ligneEntreeFournisseurs;

	




}

package com.example.stock.model;


import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoryId")
    private Category category;

  
    @ManyToOne
    @JoinColumn(name = "magasinId")
    private Magasin magasin;


    @OneToMany( mappedBy = "article")
    private Set<MVTStock>  stocks;

    @OneToMany( mappedBy = "article")
    private Set<LigneEntree> ligneEntrees;
   
    @OneToMany( mappedBy = "article")
    private Set<LigneSortie> ligneSorties;



}

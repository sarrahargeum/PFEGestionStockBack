package com.example.stock.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;

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

    @ManyToOne( fetch = FetchType.EAGER)
    @JoinColumn(name = "categoryId")
    private Category category;

  
  @ManyToOne
    @JoinColumn(name = "magasinId")
   private Magasin magasin;


    @OneToMany( fetch = FetchType.LAZY,mappedBy = "article")
    private List<MVTStock>  stocks;

   @OneToMany( fetch = FetchType.LAZY,mappedBy = "article")
    private List<LigneEntreeFournisseur> ligneEntreeFournisseur;
   
    @OneToMany( fetch = FetchType.LAZY,mappedBy = "article")
    private List<LigneSortieClient> ligneSortieClients;



	




}

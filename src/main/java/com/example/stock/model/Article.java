package com.example.stock.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "article")
public class Article {

    public Article(String string, String string2) {
		// TODO Auto-generated constructor stub
	}

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


    //@OneToMany( mappedBy = "article")
    @OneToMany(mappedBy = "article", cascade = CascadeType.REMOVE, orphanRemoval = true )
    private Set<MVTStock>  stocks;

    @OneToMany( mappedBy = "article", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<LigneEntree> ligneEntrees;
   
  //  @OneToMany( mappedBy = "article")
    @OneToMany(mappedBy = "article", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<LigneSortie> ligneSorties;



}

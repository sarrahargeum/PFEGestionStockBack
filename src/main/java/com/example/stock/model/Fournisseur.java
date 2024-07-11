package com.example.stock.model;



import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "fournisseur")
public class Fournisseur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nom;

    private String prenom;
    private String adresse;
    private String mail;
    private String numTel;

  
   private Integer idMagasin;

   
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY, mappedBy = "fournisseur")
    private List<BonEntree> bonEntrees;

}

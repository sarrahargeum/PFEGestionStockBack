package com.example.stock.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

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

    @ManyToOne
    @JoinColumn(name = "magasinId")
   private Magasin magasin;

   
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY, mappedBy = "fournisseur")
    private List<BonEntree> bonEntrees;

}

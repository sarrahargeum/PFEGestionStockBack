package com.example.stock.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
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
    private String numTel;

    @ManyToOne
    @JoinColumn(name = "idmagasin")
    private Magasin magasin;

    @JsonIgnore
    @OneToMany(mappedBy = "fournisseur")
    private List<BonEntreFournisseur> bonEntreFournisseurs;

}

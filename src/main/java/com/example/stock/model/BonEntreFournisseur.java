package com.example.stock.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "bonEntreFournisseur")
public class BonEntreFournisseur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String code;


    private Instant dateCommande;
 

    @Column(name = "etatcommande")
    @Enumerated(EnumType.STRING)
    private EtatCommande etatCommande;

    @Column(name = "idmagasin")
    private Integer idMagasin;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "idfournisseur")
    private Fournisseur fournisseur;

    @JsonIgnore
    @OneToMany(mappedBy = "ligneEntreeFournisseur")
    private List<LigneEntreeFournisseur> ligneEntreeFournisseurs;

    
    public boolean isBonFournisseurLivree() {
        return EtatCommande.LIVREE.equals(this.etatCommande);
      }
    
}

package com.example.stock.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.Instant;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "bonEntree")
public class BonEntree {
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

  
    @ManyToOne
    @JoinColumn(name = "idfournisseur")
    private Fournisseur fournisseur;

 
    @OneToMany(mappedBy =  "bonEntree")
    private Set<LigneEntree> ligneEntrees;

    
    public boolean isBonFournisseurLivree() {
        return EtatCommande.LIVREE.equals(this.etatCommande);
      }
    
}

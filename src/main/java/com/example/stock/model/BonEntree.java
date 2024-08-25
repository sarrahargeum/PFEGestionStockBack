package com.example.stock.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.Instant;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
  
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "idfournisseur")
    private Fournisseur fournisseur;

 
    @OneToMany(mappedBy = "bonEntree", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LigneEntree> ligneEntrees;

    
}

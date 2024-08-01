package com.example.stock.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.Instant;
import java.util.Set;

import com.example.stock.dto.FournisseurDto;

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
  
    @ManyToOne 
    @JoinColumn(name = "idfournisseur")
    private Fournisseur fournisseur;

 
    @OneToMany(mappedBy =  "bonEntree")
    private Set<LigneEntree> ligneEntrees;

    

 
    
}

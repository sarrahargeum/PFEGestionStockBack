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
  
    @ManyToOne 
    @JoinColumn(name = "idfournisseur")
    private Fournisseur fournisseur;

 
    @OneToMany(mappedBy =  "bonEntree")
    private Set<LigneEntree> ligneEntrees;

    
    public static BonEntree toEntity(BonEntree dto) {
        if (dto == null) {
          return null;
        }
        BonEntree bonEntree = new BonEntree();
        bonEntree.setId(dto.getId());
        bonEntree.setCode(dto.getCode());
        bonEntree.setDateCommande(dto.getDateCommande());
        bonEntree.setFournisseur(dto.getFournisseur());
        bonEntree.setIdMagasin(dto.getIdMagasin());
        bonEntree.setEtatCommande(dto.getEtatCommande());
        return bonEntree;
      }
    
    public static BonEntree fromEntity(BonEntree bonEntree) {
        if (bonEntree == null) {
          return null;
        }
        return BonEntree.builder()
            .id(bonEntree.getId())
            .code(bonEntree.getCode())
            .dateCommande(bonEntree.getDateCommande())
            .fournisseur(bonEntree.getFournisseur())
            .etatCommande(bonEntree.getEtatCommande())
            .idMagasin(bonEntree.getIdMagasin())
            .build();
      }
    public boolean isBonFournisseurLivree() {
        return EtatCommande.LIVREE.equals(this.etatCommande);
      }
    
}

package com.example.stock.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "bonSortie")
public class BonSortie {
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
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn( name = "idclient")
    private Client client;
    
    @OneToMany(mappedBy = "bonSortie", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LigneSortie> ligneSorties;
    
    
    public static BonSortie toEntity(BonSortie dto) {
        if (dto == null) {
          return null;
        }
        BonSortie bonSortie = new BonSortie();
        bonSortie.setId(dto.getId());
        bonSortie.setCode(dto.getCode());
        bonSortie.setDateCommande(dto.getDateCommande());
        bonSortie.setClient(dto.getClient());
        bonSortie.setIdMagasin(dto.getIdMagasin());
        bonSortie.setEtatCommande(dto.getEtatCommande());
        return bonSortie;
      }
    
    public static BonSortie fromEntity(BonSortie bonSortie) {
        if (bonSortie == null) {
          return null;
        }
        return BonSortie.builder()
            .id(bonSortie.getId())
            .code(bonSortie.getCode())
            .dateCommande(bonSortie.getDateCommande())
            .client(bonSortie.getClient())
            .etatCommande(bonSortie.getEtatCommande())
            .idMagasin(bonSortie.getIdMagasin())
            .build();
      } 
    
    
    public boolean isBonSortieLivree() {
        return EtatCommande.LIVREE.equals(this.etatCommande);
      }
    
    
  

}

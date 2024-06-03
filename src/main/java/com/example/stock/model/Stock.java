package com.example.stock.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Date;

@Data
@Entity
@Table(name = "stock")
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer quantite;
    private Date dateMvt;
    
    @Column(name = "typestock")
    @Enumerated(EnumType.STRING)
    private TypeStock typestock;

    @Column(name = "idmagasin")
    private Integer idMagasin;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "idarticle")
    private Article article;

	public Integer getQuantite() {
		return quantite;
	}

	public void setQuantite(Integer quantite) {
		this.quantite = quantite;
	}

	public Date getDateMvt() {
		return dateMvt;
	}

	public void setDateMvt(Date dateMvt) {
		this.dateMvt = dateMvt;
	}

	public TypeStock getTypestock() {
		return typestock;
	}

	public void setTypestock(TypeStock typestock) {
		this.typestock = typestock;
	}

	public Integer getIdMagasin() {
		return idMagasin;
	}

	public void setIdMagasin(Integer idMagasin) {
		this.idMagasin = idMagasin;
	}

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}
    
}

package com.example.stock.dto;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import com.example.stock.model.Magasin;
import com.example.stock.model.Roles;
import com.example.stock.model.User;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {

	 private Integer id;
	    private String firstname;
	    private String lastname;
	    private String email;
	    private String password;
	    
	    
	    @NotNull
	    @Column(nullable = true)
	    private boolean activated ;

	 
	   
	    private String activationKey;
	 
	    
	    
private MagasinDto magasin;

  private RolesDto roles;

  public static UserDto fromEntity(User utilisateur) {
    if (utilisateur == null) {
      return null;
    }

    return UserDto.builder()
        .id(utilisateur.getId())
        .firstname(utilisateur.getFirstname())
        .lastname(utilisateur.getLastname())
        .email(utilisateur.getEmail())
        .password(utilisateur.getPassword())
        .email(utilisateur.getEmail())
        .magasin(MagasinDto.fromEntity(utilisateur.getMagasin()))
      
        .build();
  }

  public static User toEntity(UserDto dto) {
    if (dto == null) {
      return null;
    }

    User utilisateur = new User();
    utilisateur.setId(dto.getId());
    utilisateur.setFirstname(dto.getFirstname());
    utilisateur.setLastname(dto.getLastname());
    utilisateur.setEmail(dto.getEmail());
    utilisateur.setPassword(dto.getPassword());
    
    utilisateur.setMagasin(MagasinDto.toEntity(dto.getMagasin()));

    return utilisateur;
  }
}

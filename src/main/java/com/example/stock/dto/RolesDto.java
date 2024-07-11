package com.example.stock.dto;

import com.example.stock.model.Roles;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RolesDto {

	 private Integer id;
	    private String nomRole;

  @JsonIgnore
  private UserDto user;

  public static RolesDto fromEntity(Roles roles) {
    if (roles == null) {
      return null;
    }
    return RolesDto.builder()
        .id(roles.getId())
        .nomRole(roles.getNomRole())
        .build();
  }

  public static Roles toEntity(RolesDto dto) {
    if (dto == null) {
      return null;
    }
    Roles roles = new Roles();
    roles.setId(dto.getId());
    roles.setNomRole(dto.getNomRole());
  //  roles.setUser(UserDto.toEntity(dto.getUser()));
    return roles;
  }

}

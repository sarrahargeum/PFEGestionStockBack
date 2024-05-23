package com.example.stock.auth;


import com.example.stock.model.Magasin;
import com.example.stock.model.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    public String firstname;
    private String lastname;
    private String email;
    public String password;
    
    private Magasin magasins;
    private  Roles roles ;
	

}

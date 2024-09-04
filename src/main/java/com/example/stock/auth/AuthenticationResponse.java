package com.example.stock.auth;


import com.example.stock.model.Roles;
import com.example.stock.model.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    private String accesstoken;
    private String message;
    private Roles roles;
    private User user;
	
}

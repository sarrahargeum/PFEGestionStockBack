package com.example.stock.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.validation.constraints.NotNull;

@Data

@AllArgsConstructor
@Entity
@Table(name = "user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    
    
    @NotNull
    @Column(nullable = true)
   // private String activated = "activer";
    private boolean activated ;

    
    
    @Column(name = "activation_key", length = 20)
    @JsonIgnore
    private String activationKey;
 
    
    @ManyToOne
    @JoinColumn(name = "role_id") // Name of the foreign key column in the users table
    private Roles role;


    @ManyToOne
    @JoinColumn(name = "idmagasin")
    private Magasin magasin;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
   private List<BonSortieClient> bonSortieClients;

    public User(User user) {
    }


    public User() {
		// TODO Auto-generated constructor stub
	}


	@Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public boolean isActivated() {
        return activated;
    }

    public String getActivationKey() {
        return activationKey;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public void setActivationKey(String activationKey) {
        this.activationKey = activationKey;
    }


	

	public void setPassword(String encryptedPassword) {
		this.password = encryptedPassword;
		
	}



}


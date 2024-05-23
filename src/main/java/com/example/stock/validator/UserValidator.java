package com.example.stock.validator;

import com.example.stock.model.User;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class UserValidator {

  public static List<String> validate(User user) {
    List<String> errors = new ArrayList<>();

    if (user == null) {
      errors.add("Veuillez renseigner le nom d'utilisateur");
      errors.add("Veuillez renseigner le prenom d'utilisateur");
      errors.add("Veuillez renseigner le mot de passe d'utilisateur");


      return errors;
    }

    if (!StringUtils.hasLength(user.getUsername())) {
      errors.add("Veuillez renseigner le nom d'utilisateur");
    }
    if (!StringUtils.hasLength(user.getLastname())) {
      errors.add("Veuillez renseigner le prenom d'utilisateur");
    }
    if (!StringUtils.hasLength(user.getEmail())) {
      errors.add("Veuillez renseigner l'email d'utilisateur");
    }
    if (!StringUtils.hasLength(user.getEmail())) {
      errors.add("Veuillez renseigner le mot de passe d'utilisateur");
    }


    return errors;
  }

}

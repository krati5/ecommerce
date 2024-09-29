package org.example.productservice.clients.authenticationclient.dtos;

import lombok.Getter;
import lombok.Setter;
//import org.example.userservice.models.Role;
//import org.example.userservice.models.User;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class UserResponseDto {
    private String email;
    private String firstName;
    private String lastName;
    private Set<Role> roles = new HashSet<>();

}

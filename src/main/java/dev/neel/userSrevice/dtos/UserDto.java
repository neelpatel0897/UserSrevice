package dev.neel.userSrevice.dtos;

import java.util.HashSet;
import java.util.Set;

import dev.neel.userSrevice.models.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UserDto {
    private String email;
    private Set<Role> roles=new HashSet<Role>();

   
}


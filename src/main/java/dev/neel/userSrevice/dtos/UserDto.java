package dev.neel.userSrevice.dtos;

import java.util.HashSet;
import java.util.Set;
import dev.neel.userSrevice.models.User;

import dev.neel.userSrevice.models.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UserDto {
    private String email;
    private Set<Role> roles=new HashSet<Role>();

   public static UserDto from(User user){
         UserDto userDto=new UserDto();
         userDto.setEmail(user.getEmail());
         userDto.setRoles(user.getRoles());
         return userDto;
   }
}


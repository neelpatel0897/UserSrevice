package dev.neel.userSrevice.models;

import lombok.Getter;
import lombok.Setter;
import java.util.Set;
import java.util.HashSet;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;

@Getter
@Setter
@Entity(name = "users")
public class User extends BaseModel {
    

    private String email;
    private String password;
    @ManyToMany
    private Set<Role> roles=new HashSet<Role>();

    
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
    

}
package dev.neel.userSrevice.services;



import org.springframework.stereotype.Service;

import dev.neel.userSrevice.models.Role;
import dev.neel.userSrevice.repositories.RoleRepository;

@Service
public class RoleService {

    private RoleRepository  roleRepository;
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role createRole(String role) {
        Role newRole = new Role();
        newRole.setRole(role);
        Role saveRole =roleRepository.save(newRole);
        return saveRole;
    }
    
}
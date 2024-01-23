package dev.neel.userSrevice.services;

import org.springframework.stereotype.Service;

import dev.neel.userSrevice.models.User;
import java.util.Optional;
import java.util.List;
import dev.neel.userSrevice.models.Role;
import java.util.Set;
import dev.neel.userSrevice.dtos.UserDto;
import dev.neel.userSrevice.repositories.RoleRepository;
import dev.neel.userSrevice.repositories.UserRepository;

@Service
public class UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public UserDto getUserDetails(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isEmpty()) {
            return null;
        }

        UserDto userDto = new UserDto();
        userDto.setEmail(userOptional.get().getEmail());
        userDto.setRoles(userOptional.get().getRoles());


        return userDto;
    }

    public UserDto setUserRoles(Long userId, List<Long> roleIds) {
        Optional<User> userOptional = userRepository.findById(userId);
        List<Role> roles = roleRepository.findAllByIdIn(roleIds);

        if (userOptional.isEmpty()) {
            return null;
        }

        User user = userOptional.get();
        user.setRoles(Set.copyOf(roles));

        User savedUser = userRepository.save(user);

        UserDto userDto = new UserDto();
        userDto.setEmail(savedUser.getEmail());
        userDto.setRoles(savedUser.getRoles());

        return userDto;
    }
}
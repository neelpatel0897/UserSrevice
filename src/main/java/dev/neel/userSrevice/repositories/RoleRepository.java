package dev.neel.userSrevice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.neel.userSrevice.models.Role;
import java.util.List;

public interface RoleRepository extends JpaRepository<Role,Long> {

    List<Role> findAllByIdIn(List<Long> roleIds);
}
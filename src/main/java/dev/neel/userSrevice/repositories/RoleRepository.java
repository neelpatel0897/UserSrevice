package dev.neel.userSrevice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.neel.userSrevice.models.Role;
import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {

    List<Role> findAllByIdIn(List<Long> roleIds);
}
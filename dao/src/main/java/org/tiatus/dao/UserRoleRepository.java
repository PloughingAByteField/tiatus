package org.tiatus.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tiatus.entity.Role;
import org.tiatus.entity.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    List<UserRole> findByRole(Role role);
}

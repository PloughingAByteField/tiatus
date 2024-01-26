package org.tiatus.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import org.tiatus.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRoleName(String roleName);
}

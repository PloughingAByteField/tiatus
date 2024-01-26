package org.tiatus.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import org.tiatus.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserNameAndPassword(String userName, String password);
}

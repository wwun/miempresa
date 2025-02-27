package com.miempresa.wwun.demo.modules.users.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.miempresa.wwun.demo.modules.users.entities.User;

public interface UserRepository extends CrudRepository<User, Long>{
    List<User> findAll();
    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);
}

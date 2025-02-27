package com.miempresa.wwun.demo.modules.users.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import org.springframework.stereotype.Repository;

import com.miempresa.wwun.demo.modules.users.entities.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long>{
    Optional<Role> findByName(String name);
}

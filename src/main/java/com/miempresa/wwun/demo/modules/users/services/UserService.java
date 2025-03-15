package com.miempresa.wwun.demo.modules.users.services;

import java.util.List;

import com.miempresa.wwun.demo.modules.users.dtos.UserCreateDTO;
import com.miempresa.wwun.demo.modules.users.dtos.UserDTO;

public interface UserService {
    List<UserDTO> findAll();
    UserDTO save(UserCreateDTO userCreateDTO);
    boolean existsByUsername(String username);
}

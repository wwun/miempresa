package com.miempresa.wwun.demo.modules.users.services;

import java.util.List;

import com.miempresa.wwun.demo.modules.users.dtos.UserChangePasswordDTO;
import com.miempresa.wwun.demo.modules.users.dtos.UserCreateDTO;
import com.miempresa.wwun.demo.modules.users.dtos.UserDTO;
import com.miempresa.wwun.demo.modules.users.dtos.UserUpdateDTO;

public interface UserService {
    List<UserDTO> findAll();
    UserDTO save(UserCreateDTO userCreateDTO);
    UserDTO saveAdmin(UserCreateDTO userCreateDTO);
    boolean existsByUsername(String username);
    UserDTO update(Long id, UserUpdateDTO userUpdateDTO);
    boolean delete(Long id);
    UserDTO changePassword(Long id, UserChangePasswordDTO userChangePasswordDTO);
}

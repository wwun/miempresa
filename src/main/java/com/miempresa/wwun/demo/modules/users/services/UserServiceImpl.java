package com.miempresa.wwun.demo.modules.users.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.transaction.annotation.Transactional;

import com.miempresa.wwun.demo.modules.users.dtos.UserDTO;
import com.miempresa.wwun.demo.modules.users.entities.Role;
import com.miempresa.wwun.demo.modules.users.entities.User;
import com.miempresa.wwun.demo.modules.users.repositories.RoleRepository;
import com.miempresa.wwun.demo.modules.users.repositories.UserRepository;

public class UserServiceImpl implements UserService{

    private UserRepository userRepository;
    private RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> findAll() {
        return userRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());  //(role) -> role.getName()
        //Collectors.toList() es un método que devuelve un Collector que acumula los elementos de un Stream en una lista
    }

    @Override
    @Transactional
    public UserDTO save(UserDTO userDTO) {

        List<Role> roles = new ArrayList<>();
        
        roleRepository.findByName("ROLE_USER").ifPresent(roles::add);

        roleRepository.findByName("ROLE_ADMIN").ifPresent(roles::add);

        user.setRoles(roles);
        
        //pending to add password encoded

        return userRepository.save(user);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    private UserDTO convertToDTO(User user){
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setRoles(user.getRoles().stream().map(Role::getName).collect(Collectors.toList()));
        return userDTO;
    }
}

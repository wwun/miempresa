package com.miempresa.wwun.demo.modules.users.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.miempresa.wwun.demo.mappers.UserMapper;
import com.miempresa.wwun.demo.modules.users.dtos.UserChangePasswordDTO;
import com.miempresa.wwun.demo.modules.users.dtos.UserCreateDTO;
import com.miempresa.wwun.demo.modules.users.dtos.UserDTO;
import com.miempresa.wwun.demo.modules.users.dtos.UserUpdateDTO;
import com.miempresa.wwun.demo.modules.users.entities.Role;
import com.miempresa.wwun.demo.modules.users.entities.User;
import com.miempresa.wwun.demo.modules.users.repositories.RoleRepository;
import com.miempresa.wwun.demo.modules.users.repositories.UserRepository;

public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, UserMapper userMapper, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> findAll() {
        return userRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());  //(role) -> role.getName()
        //Collectors.toList() es un método que devuelve un Collector que acumula los elementos de un Stream en una lista
    }

    @Override
    @Transactional
    public UserDTO save(UserCreateDTO userCreateDTO) {

        User user = userMapper.toEntity(userCreateDTO);

        List<Role> roles = new ArrayList<>();
        
        roleRepository.findByName("ROLE_USER").ifPresent(roles::add);

        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        return userMapper.toDTO(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserDTO saveAdmin(UserCreateDTO userCreateDTO){
        User user = userMapper.toEntity(userCreateDTO);

        List<Role> roles = new ArrayList<>();

        roleRepository.findByName("ROLE_USER").ifPresent(roles::add);

        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(userCreateDTO.getPassword()));

        return userMapper.toDTO(userRepository.save(user));
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    @Transactional
    public UserDTO update(Long id, UserUpdateDTO userUpdateDTO){
        Optional<User> userFound = userRepository.findById(id);
        if(userFound.isPresent()){
            User user = userFound.get();

            user.setEmail(userUpdateDTO.getEmail());
            user.setUsername(userUpdateDTO.getUsername());

            User userUpdated = userRepository.save(user);
            
            return userMapper.toDTO(userUpdated);
        }else{
            return null;    //error exception
        }
    }

    @Override
    @Transactional
    public boolean delete(Long id){
        if(userRepository.findById(id).isPresent()){
            userRepository.deleteById(id);
            return true;
        }
        return false;   //error exception need to be managed
    }

    @Override
    @Transactional
    public UserDTO changePassword(Long id, UserChangePasswordDTO userChangePasswordDTO){
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            if(passwordEncoder.matches(user.get().getPassword(),userChangePasswordDTO.getOldPassword())){
                user.get().setPassword(passwordEncoder.encode(userChangePasswordDTO.getNewPassword()));
                User updatedUser = userRepository.save(user.get());
                return userMapper.toDTO(updatedUser);
            } else {
                throw new RuntimeException("Error al guardar la contrasena");
            }
        }else {
            throw new RuntimeException("Usuario no encontrado");
        }
    }

    private UserDTO convertToDTO(User user){
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setRoles(user.getRoles().stream().map(Role::getName).collect(Collectors.toList()));
        return userDTO;
    }
}
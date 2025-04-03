package com.miempresa.wwun.demo.modules.users.controllers;

import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.miempresa.wwun.demo.modules.users.dtos.UserChangePasswordDTO;
import com.miempresa.wwun.demo.modules.users.dtos.UserCreateDTO;
import com.miempresa.wwun.demo.modules.users.dtos.UserDTO;
import com.miempresa.wwun.demo.modules.users.dtos.UserUpdateDTO;
import com.miempresa.wwun.demo.modules.users.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping
    public List<UserDTO> listAll(){
        return userService.findAll();
    }

    //create
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<?> create(@Valid @RequestBody UserCreateDTO userCreateDTO, BindingResult result){
        if(result.hasErrors()){
            return validation(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(userCreateDTO));
    }

    //create
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createAdmin(@Valid @RequestBody UserCreateDTO userCreateDTO, BindingResult result){
        if(result.hasErrors()){
            return validation(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(userCreateDTO));
    }

    //update
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody UserUpdateDTO userUpdateDTO, BindingResult result){
        if(result.hasErrors()){
            return validation(result);
        }
        return ResponseEntity.status(HttpStatus.OK).body(userService.update(id, userUpdateDTO));
    }

    //updatePassword
    @PatchMapping("/{id}/password")
    public ResponseEntity<?> updatePassword(@PathVariable Long id, @Valid @RequestBody UserChangePasswordDTO userChangePasswordDTO, BindingResult result){
        if(result.hasErrors()){
            return validation(result);
        }
        return ResponseEntity.status(HttpStatus.OK).body(userService.changePassword(id, userChangePasswordDTO));
    }

    public ResponseEntity<?> validation(BindingResult result){

        Map<String, String> errors = new HashMap<>();
        
        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);

    }
    
}

// POST se usa para crear recursos, no para actualizarlos.
// PUT se usa para actualizar completamente un recurso.
// PATCH se usa para actualizar parcialmente un recurso
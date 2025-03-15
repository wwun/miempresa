package com.miempresa.wwun.demo.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import com.miempresa.wwun.demo.modules.users.dtos.UserCreateDTO;
import com.miempresa.wwun.demo.modules.users.dtos.UserDTO;
import com.miempresa.wwun.demo.modules.users.dtos.UserUpdateDTO;
import com.miempresa.wwun.demo.modules.users.entities.Role;
import com.miempresa.wwun.demo.modules.users.entities.User;

@Mapper(componentModel = "spring")  //Le dice a MapStruct que esta interfaz se usará para convertir objetos entre clases, componentModel = "spring": Hace que MapStruct registre este mapper como un bean de Spring, así puedes inyectarlo con @Autowired
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);  //INSTANCE: Te da una instancia del mapper sin usar Spring. No la necesitas si usas Spring, pero puede ser útil para pruebas

    @Mapping(source = "roles", target = "roles", qualifiedByName = "mapRolesToStrings") //@Mapping: Define cómo se transforman los campos, source: Campo de origen (roles en la entidad User, target: Campo de destino (roles en UserDTO, qualifiedByName: Le dice a MapStruct que use el método mapRolesToStrings para convertir List<Role> a List<String>
    UserDTO toDTO(User user);

    @Mapping(source = "roles", target = "roles", qualifiedByName = "mapStringsToRoles") //source y target: Mapea roles de List<String> a List<Role> usando mapStringsToRoles
    @Mapping(target = "id", ignore = true) // No mapear id en creación porqe se genera automáticamente en la base d
    @Mapping(target = "password", ignore = true) // No mapear password (por seguridad)
    @Mapping(target = "admin", ignore = true) // No mapear admin porque es @Transient, Tu entidad User tiene un campo llamado admin, que es @Transient (es decir, no se almacena en la base de datos).  UserCreateDTO no tiene ese campo admin, porque al crear un usuario normalmente no decides directamente si es admin desde el DTO, sino que eso lo decides en la lógica de negocio o desde la base de datos. ¿Por qué @Mapping(target = "admin", ignore = true)? MapStruct intenta mapear todos los campos de UserCreateDTO a User. Como en la entidad User existe admin, pero en el DTO no, MapStruct se quejaría de que no sabe de dónde sacar ese valor
    User toEntity(UserCreateDTO dto);

    @Mapping(source = "roles", target = "roles", qualifiedByName = "mapStringsToRoles")
    @Mapping(target = "password", ignore = true) // Ignorar password en actualización
    @Mapping(target = "admin", ignore = true) // Ignorar admin
    User toEntity(UserUpdateDTO dto);

    // Método de mapeo personalizado para la lista de roles
    @Named("mapRolesToStrings") //los métodos de transformación tienen nombres explícitos (mapRolesToStrings, mapStringsToRoles) para que MapStruct los encuentre
    default List<String> mapRolesToString(List<Role> roles) {
        if (roles == null) {
            return null;
        }
        return roles.stream()
                    .map(Role::getName) // Suponiendo que Role tiene un método getName(), stream y map: Itera la lista de roles y extrae el name de cada rol, collect: Convierte el stream en una lista
                    .collect(Collectors.toList());
    }
    //En Java, las interfaces pueden tener métodos con implementación usando la palabra clave default. Estos métodos no necesitan una clase que implemente la interfaz, porque ya tienen una implementación por defecto
    //En lugar de crear una clase separada solo para hacer la conversión de roles, puedes definir esos métodos directamente en la interfaz. Luego, con la anotación @Named, le dices a MapStruct que use esos métodos cuando haga los mapeos

    // Mapea String -> Role
    @Named("mapStringsToRoles")
    default List<Role> mapStringsToRoles(List<String> roleNames) {  //rea un nuevo objeto Role con solo el nombre. Si necesitas más lógica, la ajustamos
        if (roleNames == null) return null;
        return roleNames.stream()
                        .map(name -> {
                            Role role = new Role();
                            role.setName(name); // Asegúrate de tener setName en Role
                            return role;
                        })
                        .collect(Collectors.toList());
    }
}

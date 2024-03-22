package com.taller_4.taller_4.Data;

import com.taller_4.taller_4.Dtos.UserDTO;
import com.taller_4.taller_4.Mappers.UserMapper;
import com.taller_4.taller_4.Models.PermissionsModel;
import com.taller_4.taller_4.Models.UserModel;
import com.taller_4.taller_4.Repositories.IUserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    private final IUserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder bCrypt = new BCryptPasswordEncoder();

    @Autowired
    public DataInitializer(IUserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @PostConstruct
    public void init() {
        // Aquí puedes inicializar tus datos, incluido el usuario administrador
        initializeAdminUser();
        // Otros métodos de inicialización si los necesitas
    }

    private void initializeAdminUser() {
        // Verificar si el usuario administrador ya existe
        if (userRepository.findByUsername("admin") == null) {
            // Crear el usuario administrador
            UserDTO adminUser = new UserDTO();
            adminUser.setUsername("admin");
            adminUser.setEmail("admin@email.com");
            adminUser.setPassword(bCrypt.encode("admin"));

            // Crear una instancia de UserModel
            UserModel userModel = userMapper.toUserModel(adminUser);

            // Crear una instancia de PermissionsModel y asignar el rol
            PermissionsModel permissions = new PermissionsModel();
            permissions.setRole("ADMIN_ROLE"); // Asignar el rol apropiado

            // Asignar permisos al usuario y viceversa
            userModel.setPermissions(permissions);
            permissions.setUser(userModel);

            // Guardar el usuario en la base de datos
            userRepository.save(userModel);

            // Imprimir un mensaje en la consola
            System.out.println("Usuario administrador creado exitosamente");
        }
    }

    // Otros métodos de inicialización de datos si los necesitas
}

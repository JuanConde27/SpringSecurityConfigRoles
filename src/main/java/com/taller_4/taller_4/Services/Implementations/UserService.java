package com.taller_4.taller_4.Services.Implementations;

import com.taller_4.taller_4.Dtos.ResponseDTO;
import com.taller_4.taller_4.Dtos.UserDTO;
import com.taller_4.taller_4.Mappers.UserMapper;
import com.taller_4.taller_4.Models.PermissionsModel;
import com.taller_4.taller_4.Models.UserModel;
import com.taller_4.taller_4.Repositories.IUserRepository;
import com.taller_4.taller_4.Security.JsonWebToken;
import com.taller_4.taller_4.Services.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserService implements IUserService {

    private final IUserRepository userRepository;
    private final UserMapper userMapper;
    private final JsonWebToken jsonWebToken;
    private final PasswordEncoder bCrypt = new BCryptPasswordEncoder();

    public UserService(IUserRepository userRepository, UserMapper userMapper, JsonWebToken jsonWebToken) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.jsonWebToken = jsonWebToken;
    }

    @Override
    public ResponseEntity<ResponseDTO> register(UserDTO userDTO) {
        try {
            UserModel existingUser = userRepository.findByEmail(userDTO.getEmail());
            if (existingUser != null) {
                return ResponseEntity.badRequest().body(ResponseDTO.builder().message("El correo ya se encuentra registrado").build());
            }

            String password = userDTO.getPassword();
            String hashedPassword = bCrypt.encode(password);
            userDTO.setPassword(hashedPassword);

            // Crear una instancia de UserModel
            UserModel userModel = userMapper.toUserModel(userDTO);

            // Crear una instancia de PermissionsModel y asignar el rol
            PermissionsModel permissions = new PermissionsModel();
            permissions.setRole("USER_ROLE"); // Asignar el rol apropiado

            // Asignar permisos al usuario y viceversa
            userModel.setPermissions(permissions);
            permissions.setUser(userModel);

            // Guardar el usuario en la base de datos
            UserModel savedUser = userRepository.save(userModel);

            // Construir y retornar la respuesta
            ResponseDTO responseDTO = ResponseDTO.builder().message("Usuario registrado exitosamente").response(Collections.singletonMap("user", userMapper.toUserDTO(savedUser))).build();
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
        } catch (Exception e) {
            // Manejo de excepciones
            return ResponseEntity.badRequest().body(ResponseDTO.builder().message(e.getMessage()).build());
        }
    }


    @Override
    public ResponseEntity<ResponseDTO> login(UserDTO userDTO) {
        try {
            UserModel userModel = userRepository.findByEmail(userDTO.getEmail());
            if (userModel == null) {
                return ResponseEntity.badRequest().body(ResponseDTO.builder().message("El correo no se encuentra registrado").build());
            }

            if (!bCrypt.matches(userDTO.getPassword(), userModel.getPassword())) {
                return ResponseEntity.badRequest().body(ResponseDTO.builder().message("Contraseña incorrecta").build());
            }

            String token = jsonWebToken.generateJwtToken(userModel.getId(), userModel.getPermissions().getRole());
            System.out.println(userModel.getPermissions().getRole());

            String successMessage = "Usuario autenticado exitosamente";
            ResponseDTO responseDTO = ResponseDTO.builder().message(successMessage).response(Collections.singletonMap("token", token)).build();

            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseDTO.builder().message(e.getMessage()).build());
        }
    }

    @Override
    public ResponseEntity<ResponseDTO> update(String token, UserDTO userDTO) {
        try {

            Long id = jsonWebToken.getIdFromJwtToken(token);

            UserModel userModel = userMapper.toUserModel(userDTO);

            UserModel userModelToUpdate = userRepository.findById(id).orElse(null);
            if (userModelToUpdate == null) {
                return ResponseEntity.badRequest().body(ResponseDTO.builder().message("El usuario no se encuentra registrado").build());
            }

            UserModel existingUser = userRepository.findByEmail(userModel.getEmail());
            if (existingUser != null && !existingUser.getId().equals(userModelToUpdate.getId())) {
                return ResponseEntity.badRequest().body(ResponseDTO.builder().message("El correo ya se encuentra registrado").build());
            }

            userModelToUpdate.setEmail(userModel.getEmail());
            userModelToUpdate.setPassword(userModel.getPassword());
            userModelToUpdate.setUsername(userModel.getUsername());
            UserModel updatedUser = userRepository.save(userModelToUpdate);
            UserDTO updatedUserDTO = userMapper.toUserDTO(updatedUser);

            String successMessage = "Usuario actualizado exitosamente";
            ResponseDTO responseDTO = ResponseDTO.builder().message(successMessage).response(Collections.singletonMap("user", updatedUserDTO)).build();

            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseDTO.builder().message(e.getMessage()).build());
        }
    }
}
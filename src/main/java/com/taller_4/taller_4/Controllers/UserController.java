package com.taller_4.taller_4.Controllers;

import com.taller_4.taller_4.Dtos.ResponseDTO;
import com.taller_4.taller_4.Dtos.UserDTO;
import com.taller_4.taller_4.Repositories.IUserRepository;
import com.taller_4.taller_4.Security.JsonWebToken;
import com.taller_4.taller_4.Services.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class UserController {

    private final IUserService userService;
    private final IUserRepository userRepository;
    private final JsonWebToken jsonWebToken;

    public UserController(IUserService userService, IUserRepository userRepository, JsonWebToken jsonWebToken) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.jsonWebToken = jsonWebToken;
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> register(@RequestBody UserDTO userDTO) {
        try {
            return userService.register(userDTO);
        } catch (Exception e) {
            ResponseDTO errorResponse = ResponseDTO.builder().message(e.getMessage()).build();
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> login(@RequestBody UserDTO userDTO) {
        try {
            return userService.login(userDTO);
        } catch (Exception e) {
            ResponseDTO errorResponse = ResponseDTO.builder().message(e.getMessage()).build();
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<ResponseDTO> findById(@PathVariable Long id) {
        try {
            String message = "Usuario encontrado exitosamente";
            return new ResponseEntity<>(ResponseDTO.builder().message(message).response(userRepository.findById(id)).build(), HttpStatus.OK);
        } catch (Exception e) {
            ResponseDTO errorResponse = ResponseDTO.builder().message(e.getMessage()).build();
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/findAll")
    public ResponseEntity<ResponseDTO> findAll() {
        try {
            String message = "Usuarios encontrados exitosamente";
            return new ResponseEntity<>(ResponseDTO.builder().message(message).response(userRepository.findAll()).build(), HttpStatus.OK);
        } catch (Exception e) {
            ResponseDTO errorResponse = ResponseDTO.builder().message(e.getMessage()).build();
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDTO> updateUser(@RequestHeader("Authorization") String token, @RequestBody UserDTO userDTO) {
        try {
            return userService.update(token, userDTO);
        } catch (Exception e) {
            ResponseDTO errorResponse = ResponseDTO.builder().message(e.getMessage()).build();
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseDTO> deleteUser(@PathVariable Long id) {
        try {
            String message = "Usuario eliminado exitosamente con el id " + id;
            userRepository.deleteById(id);
            return new ResponseEntity<>(ResponseDTO.builder().message(message).build(), HttpStatus.OK);
        } catch (Exception e) {
            ResponseDTO errorResponse = ResponseDTO.builder().message(e.getMessage()).build();
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }
}

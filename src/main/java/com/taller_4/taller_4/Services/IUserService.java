package com.taller_4.taller_4.Services;

import com.taller_4.taller_4.Dtos.ResponseDTO;
import com.taller_4.taller_4.Dtos.UserDTO;
import org.springframework.http.ResponseEntity;


public interface IUserService {
    ResponseEntity<ResponseDTO> register(UserDTO userDTO);
    ResponseEntity<ResponseDTO> login(UserDTO userDTO);
    ResponseEntity<ResponseDTO> update(String token, UserDTO userDTO);
}

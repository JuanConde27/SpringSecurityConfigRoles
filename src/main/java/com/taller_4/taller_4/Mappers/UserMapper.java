package com.taller_4.taller_4.Mappers;

import com.taller_4.taller_4.Dtos.UserDTO;
import com.taller_4.taller_4.Models.UserModel;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toUserDTO(UserModel userModel);
    List<UserDTO> toUserDTOs(List<UserModel> userModels);
    @InheritInverseConfiguration
    UserModel toUserModel(UserDTO userDTO);
}

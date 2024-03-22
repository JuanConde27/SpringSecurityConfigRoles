package com.taller_4.taller_4.Dtos;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String password;
}

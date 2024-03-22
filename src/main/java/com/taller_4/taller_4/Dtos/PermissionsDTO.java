package com.taller_4.taller_4.Dtos;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PermissionsDTO {
    private Long id;
    private String role;
}

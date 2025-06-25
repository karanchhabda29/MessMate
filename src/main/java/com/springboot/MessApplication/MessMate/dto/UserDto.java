package com.springboot.MessApplication.MessMate.dto;

import com.springboot.MessApplication.MessMate.entities.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private String email;
    private String name;
    private Role role;
}

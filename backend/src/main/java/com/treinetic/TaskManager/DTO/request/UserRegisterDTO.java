package com.treinetic.TaskManager.DTO.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegisterDTO {
    private String name;
    private String email;
    private String password;
}

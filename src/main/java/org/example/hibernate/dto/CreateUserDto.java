package org.example.hibernate.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.example.hibernate.entity.PersonalInfo;
import org.example.hibernate.validation.CreateUser;

public record CreateUserDto(@Valid PersonalInfo personalInfo,
                           @NotNull
                           String username,
                           @NotNull(groups = CreateUser.class)
                           String info,
                           Integer companyId) {
}

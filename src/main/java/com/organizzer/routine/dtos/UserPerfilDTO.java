package com.organizzer.routine.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserPerfilDTO(
    @NotBlank String userName,
    @NotBlank @Email String userEmail,
    @NotBlank String userCurrentPassword,
    String newUserPassword
) {}

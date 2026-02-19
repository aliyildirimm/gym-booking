package com.gym.authservice.application.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class SignUpDto {

    @NotNull
    @Size(max = 12)
    public String username;

    @NotNull
    @Size(min = 8, max = 12)
    @Pattern(
        regexp = "^(?=.*[^a-zA-Z0-9]).*$",
        message = "Password must contain at least one special character"
    )
    public String password;
}

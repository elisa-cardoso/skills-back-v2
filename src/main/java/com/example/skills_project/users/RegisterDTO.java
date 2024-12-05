package com.example.skills_project.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterDTO(
        @NotNull(message = "O login é obrigatório.")
        @Email(message = "O login deve ser um endereço de email válido.")
        String login,

        @NotNull(message = "A senha é obrigatória.")
        @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres.")
        String password,

        UserRole role
) {}

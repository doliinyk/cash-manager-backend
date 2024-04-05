package com.cashmanagerbackend.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserPasswordUpdateDTO(@NotBlank(message = "Old password is missing")
                                    @Size(min = 8,
                                            max = 40,
                                            message = "Old password must be between 8 and 40 characters long")
                                    String oldPassword,
                                    @NotBlank(message = "New password is missing")
                                    @Size(min = 8,
                                            max = 40,
                                            message = "Password must be between 8 and 40 characters long")
                                    String newPassword) {
}

package com.cashmanagerbackend.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserPasswordUpdateDTO(@Size(min = 8, max = 40, message = "Password must be between 8 and 40")
                                    @NotBlank(message = "Old password is missing")
                                    String oldPassword,
                                    @Size(min = 8, max = 40, message = "Password must be between 8 and 40")
                                    @NotBlank(message = "New password is missing")
                                    String newPassword) {
}

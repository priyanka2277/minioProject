package com.example.minioproject.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    @NotBlank(message = "Name is required")
    private  String name;
    @NotBlank(message = "Email is required")
    @Email(message="Please provide a valid email address")
    private String email;

}

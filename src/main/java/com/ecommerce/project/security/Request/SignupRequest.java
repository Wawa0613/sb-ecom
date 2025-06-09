package com.ecommerce.project.security.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class SignupRequest {
    @NotBlank
    @Size(min=3,max=20)
    private String username;
    @NotBlank
    @Size(max=50)
    private String email;
    private Set<String> role;
    @NotBlank
    @Size(min=6,max=30)
    private String password;
}

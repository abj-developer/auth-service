package com.abj.auth_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginRequest {

    @Schema(example = "admin")
    private String username;

    @Schema(example = "abj123")
    private String password;
}

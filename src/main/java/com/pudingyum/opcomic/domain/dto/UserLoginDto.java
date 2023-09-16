package com.pudingyum.opcomic.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserLoginDto {
    @NotBlank(message = "username required!")
    @Size(min = 6, message = "username must be at least 6 characters.")
    private String username;

    @NotBlank(message = "password is required!")
    @Size(min = 8, message = "password must be at least 8 characters.")
    private String password;
}

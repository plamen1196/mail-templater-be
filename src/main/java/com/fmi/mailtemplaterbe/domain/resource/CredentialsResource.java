package com.fmi.mailtemplaterbe.domain.resource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CredentialsResource {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String smtpServerName;
}

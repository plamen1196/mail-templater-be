package com.fmi.mailtemplaterbe.domain.resource;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipientResource {

    private Long id;

    @NotBlank(message = "Email must not be blank")
    private String email;
}

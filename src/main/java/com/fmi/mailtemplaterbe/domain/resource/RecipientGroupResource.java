package com.fmi.mailtemplaterbe.domain.resource;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipientGroupResource {

    private Long id;

    @NotBlank(message = "Title must not be blank")
    private String title;

    @NotNull(message = "recipientIds must not be null")
    private String recipientIds;
}

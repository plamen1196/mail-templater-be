package com.fmi.mailtemplaterbe.domain.resource;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailTemplateResource {

    private Long id;

    @NotBlank(message = "Title must not be blank")
    private String title;

    @NotBlank(message = "Message must not be blank")
    private String message;

    @NotNull(message = "Placeholders must not be null")
    private List<String> placeholders;
}

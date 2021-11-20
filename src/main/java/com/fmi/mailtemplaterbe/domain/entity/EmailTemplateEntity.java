package com.fmi.mailtemplaterbe.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "mail_templates")
@Data
@IdClass(EmailTemplateId.class)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailTemplateEntity {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "placeholders", nullable = false)
    private String placeholders;
}

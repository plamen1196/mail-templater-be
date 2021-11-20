package com.fmi.mailtemplaterbe.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "mail_templates")
@Data
@IdClass(MailTemplateId.class)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MailTemplateEntity {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "template", nullable = false)
    private String template;
}

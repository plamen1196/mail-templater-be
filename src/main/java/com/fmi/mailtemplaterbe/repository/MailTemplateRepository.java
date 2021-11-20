package com.fmi.mailtemplaterbe.repository;

import com.fmi.mailtemplaterbe.domain.entity.MailTemplateEntity;
import com.fmi.mailtemplaterbe.domain.entity.MailTemplateId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MailTemplateRepository extends JpaRepository<MailTemplateEntity, MailTemplateId> {

}

package com.fmi.mailtemplaterbe.repository;

import com.fmi.mailtemplaterbe.domain.entity.EmailTemplateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailTemplateRepository extends JpaRepository<EmailTemplateEntity, Long> {

}

package com.fmi.mailtemplaterbe.repository;

import com.fmi.mailtemplaterbe.domain.entity.RecipientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecipientEntityRepository extends JpaRepository<RecipientEntity, Long> {

    Optional<RecipientEntity> findById(Long id);
}

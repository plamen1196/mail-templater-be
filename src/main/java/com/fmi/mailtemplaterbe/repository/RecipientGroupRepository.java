package com.fmi.mailtemplaterbe.repository;

import com.fmi.mailtemplaterbe.domain.entity.RecipientGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecipientGroupRepository extends JpaRepository<RecipientGroupEntity, Long> {

    Optional<RecipientGroupEntity> findById(Long id);
}

package com.fmi.mailtemplaterbe.repository;

import com.fmi.mailtemplaterbe.domain.entity.SentEmailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SentEmailEntityRepository extends JpaRepository<SentEmailEntity, Long> {

    Optional<SentEmailEntity> findById(Long id);

    Optional<List<SentEmailEntity>> findAllByTimestampBetween(LocalDateTime startDate, LocalDateTime endDate);
}

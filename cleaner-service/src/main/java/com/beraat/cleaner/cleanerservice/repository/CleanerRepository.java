package com.beraat.cleaner.cleanerservice.repository;

import com.beraat.cleaner.cleanerservice.entity.Cleaner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CleanerRepository extends JpaRepository<Cleaner, Long> {
}

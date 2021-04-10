package com.beraat.cleaner.cleanerservice.repository;

import com.beraat.cleaner.cleanerservice.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}

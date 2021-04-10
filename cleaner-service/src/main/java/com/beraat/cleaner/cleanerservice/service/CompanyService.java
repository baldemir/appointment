package com.beraat.cleaner.cleanerservice.service;

import com.beraat.cleaner.cleanerservice.entity.Company;
import com.beraat.cleaner.cleanerservice.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.List;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;

    @Autowired
    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company addNewCompany(Company company) {
        return companyRepository.save(company);
    }

    public Company getCompany(Long id) {
        return companyRepository.findById(id).orElseThrow(() -> new InvalidParameterException());
    }
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }
}

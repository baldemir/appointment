package com.beraat.cleaner.cleanerservice.service;

import com.beraat.cleaner.cleanerservice.entity.Cleaner;
import com.beraat.cleaner.cleanerservice.entity.Company;
import com.beraat.cleaner.cleanerservice.repository.CleanerRepository;
import com.beraat.cleaner.cleanerservice.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CleanerService {
    private final CleanerRepository cleanerRepository;

    @Autowired
    public CleanerService(CleanerRepository cleanerRepository) {
        this.cleanerRepository = cleanerRepository;
    }

    public Cleaner addNewCleaner(Cleaner cleaner) {
        return cleanerRepository.save(cleaner);
    }

    public List<Cleaner> getCleaners(){
        return cleanerRepository.findAll();
    }
    public Cleaner getCleaner(Long id){
        return cleanerRepository.findById(id).orElseThrow(()->new IllegalArgumentException());
    }
}

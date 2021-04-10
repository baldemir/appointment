package com.beraat.cleaner.cleanerservice.controller;

import com.beraat.cleaner.cleanerservice.entity.Company;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.beraat.cleaner.cleanerservice.service.CompanyService;

import java.util.List;

@RestController
@RequestMapping("/company")
@Api(value = "Company API documentation")
public class CompanyController {
    private final CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping
    @ApiOperation(value = "Create a new company")
    public Company addNewProduct(@RequestBody Company company) {
        return companyService.addNewCompany(company);
    }

    @GetMapping
    @ApiOperation(value = "Get companies")
    public List<Company> getProducts() {
        return companyService.getAllCompanies();
    }
}

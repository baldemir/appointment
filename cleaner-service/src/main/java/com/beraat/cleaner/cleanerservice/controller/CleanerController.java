package com.beraat.cleaner.cleanerservice.controller;

import com.beraat.cleaner.cleanerservice.entity.Cleaner;
import com.beraat.cleaner.cleanerservice.entity.Company;
import com.beraat.cleaner.cleanerservice.service.CleanerService;
import com.beraat.cleaner.cleanerservice.service.CompanyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cleaner")
@Api(value = "Cleaner API documentation")
public class CleanerController {
    private final CleanerService cleanerService;
    private final CompanyService companyService;

    public CleanerController(CleanerService cleanerService, CompanyService companyService) {
        this.cleanerService = cleanerService;
        this.companyService = companyService;
    }

    @GetMapping
    @ApiOperation(value = "Get cleaners")
    public List<Cleaner> getCleaners() {
        return cleanerService.getCleaners();

    }

    @GetMapping(path = "/{cleanerId}")
    @ApiOperation(value = "Get cleaners")
    public Cleaner getCleaner(@PathVariable Long cleanerId) {
        return cleanerService.getCleaner(cleanerId);
    }

    @PostMapping
    @ApiOperation(value = "Create a new cleaner")
    public Cleaner addNewCleaner(@RequestBody Cleaner cleaner) {
        return cleanerService.addNewCleaner(cleaner);
    }

}

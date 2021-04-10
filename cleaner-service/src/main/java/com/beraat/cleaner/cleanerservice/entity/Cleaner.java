package com.beraat.cleaner.cleanerservice.entity;

import javax.persistence.*;

@Entity
@Table
public class Cleaner {
    @Id
    @SequenceGenerator(name = "cleaner_sequence",
            sequenceName = "cleaner_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "cleaner_sequence")
    Long id;
    String fullName;

    @ManyToOne(optional = false)
    Company company;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}

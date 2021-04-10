package com.beraat.cleaner.cleanerservice.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table
@EntityListeners(AuditingEntityListener.class)
@ApiModel(value = "Company Entity Documentation", description = "Entity")
public class Company {
    @Id
    @SequenceGenerator(name = "company_sequence",
            sequenceName = "company_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "company_sequence")
    @ApiModelProperty(value = "Unique id field of company object")
    Long id;
    @ApiModelProperty(value = "Name of company")
    String name;

    @OneToMany
    Set<Cleaner> cleaners = new HashSet<>();

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Date createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Company() {
    }

    public Company(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

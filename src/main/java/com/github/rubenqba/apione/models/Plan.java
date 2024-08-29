package com.github.rubenqba.apione.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Plan {
    @Id
    private String id;
    @Indexed(unique = true)
    private String name;
    private String description;
    @CreatedDate
    private Instant createdAt;
    @CreatedBy
    private String createdBy;
    @LastModifiedDate
    private Instant updatedAt;
    @LastModifiedBy
    private String updatedBy;
    @Version
    private Long version;
}

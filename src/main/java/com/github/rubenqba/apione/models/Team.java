package com.github.rubenqba.apione.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.Instant;

/**
 * Team summary here...
 *
 * @author rbresler
 **/
@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Team {
    @Id
    private String id;
    @Indexed(unique = true)
    private String name;
    @DocumentReference(collection = "plans")
    private Plan plan;
    @CreatedDate
    private Instant createdAt;
    @CreatedBy
    private String createdBy;
    @LastModifiedDate
    private Instant updatedAt;
    @LastModifiedBy
    private String updatedBy;
}

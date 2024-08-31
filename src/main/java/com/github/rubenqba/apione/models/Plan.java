package com.github.rubenqba.apione.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Plan extends AuditMetadata {
    @Id
    private String id;
    @Indexed(unique = true)
    private String name;
    private String description;
    @Version
    private Long version;
}

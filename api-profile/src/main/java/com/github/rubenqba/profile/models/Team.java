package com.github.rubenqba.profile.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Team extends AuditMetadata {
    @Id
    private String id;
    @Indexed(unique = true)
    private String name;
    @DocumentReference(collection = "plan")
    private Summary plan;
}

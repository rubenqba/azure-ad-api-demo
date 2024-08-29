package com.github.rubenqba.apione.service;

import com.github.rubenqba.apione.models.Plan;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Update;

import java.util.Optional;

public interface PlanRepository extends MongoRepository<Plan, String> {
    @Update("{name: ?1, description: ?2}")
    long findAndUpdateById(String id, @NotEmpty String name, String description);
}

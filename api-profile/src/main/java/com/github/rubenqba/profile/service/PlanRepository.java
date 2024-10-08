package com.github.rubenqba.profile.service;

import com.github.rubenqba.profile.models.Plan;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Update;

public interface PlanRepository extends MongoRepository<Plan, String> {
    @Update("{name: ?1, description: ?2}")
    long findAndUpdateById(String id, @NotEmpty String name, String description);
}

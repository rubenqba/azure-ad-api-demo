package com.github.rubenqba.profile.service;

import com.github.rubenqba.profile.models.Plan;
import jakarta.validation.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlanService {
    private static final Logger log = LoggerFactory.getLogger(PlanService.class);

    private final PlanRepository plans;

    public PlanService(PlanRepository plans) {
        this.plans = plans;
    }

    // TODO: implementar un metodo para normalizar el nombre de un plan que sirva como ID
    private String normalizeName(String name) {
        return name.toLowerCase().replace(" ", "_");
    }

    public Plan createPlan(String name, String description) {
        log.info("Creating a new plan with name {}", name);
        return plans.save(Plan.builder()
                .name(name)
                .description(description)
                .id(normalizeName(name))
                .build());
    }

    public Optional<Plan> getPlan(String id) {
        log.info("get plan '{}'", id);
        return plans.findById(id);
    }

    public List<Plan> getAllPlans() {
        log.info("getting all plans");
        return plans.findAll();
    }

    public void removePlan(String id) {
        log.info("removing plan '{}'", id);
        plans.deleteById(id);
    }

    public void updatePlan(String id, @NotEmpty String name, String description) {
        log.info("updating plan '{}'", id);
        final var plan = getPlan(id);
        plan.ifPresent(p -> {
            p.setName(name);
            p.setDescription(description);
            plans.save(p);
        });
    }
}

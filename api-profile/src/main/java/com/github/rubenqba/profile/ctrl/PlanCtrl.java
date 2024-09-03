package com.github.rubenqba.profile.ctrl;

import com.github.rubenqba.profile.ctrl.dto.CreatePlanDto;
import com.github.rubenqba.profile.models.Plan;
import com.github.rubenqba.profile.service.PlanService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@Validated
@PreAuthorize("hasAuthority('AZ_Admin')")
public class PlanCtrl {
    private static final Logger log = LoggerFactory.getLogger(PlanCtrl.class);

    private final PlanService plans;

    public PlanCtrl(PlanService plans) {
        this.plans = plans;
    }

    @GetMapping("/plans")
    public Collection<Plan> getPlans() {
        return plans.getAllPlans();
    }

    @PostMapping("/plans")
    public Plan createPlan(@RequestBody @Valid CreatePlanDto plan) {
        return plans.createPlan(plan.name(), plan.description());
    }

    @GetMapping("/plans/{id}")
    public ResponseEntity<Plan> findPlan(@PathVariable String id, @RequestBody @Valid CreatePlanDto dto) {
        return ResponseEntity.of(plans.getPlan(id));
    }

    @PutMapping("/plans/{id}")
    public ResponseEntity<Plan> updatePlan(@PathVariable String id, @RequestBody @Valid CreatePlanDto dto) {
        plans.updatePlan(id, dto.name(), dto.description());
        return ResponseEntity.of(plans.getPlan(id));
    }

    @DeleteMapping("/plans/{plan}")
    public void deletePlan(@PathVariable String plan) {
        plans.removePlan(plan);
    }
}

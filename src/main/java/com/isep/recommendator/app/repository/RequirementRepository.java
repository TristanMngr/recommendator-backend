package com.isep.recommendator.app.repository;

import com.isep.recommendator.app.model.Module;
import com.isep.recommendator.app.model.Requirement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequirementRepository extends JpaRepository<Requirement, Long> {

}

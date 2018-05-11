package com.isep.recommendator.app.repository;

import com.isep.recommendator.app.model.SpecialityModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecialityModuleRepository extends JpaRepository<SpecialityModule, Long> {
}

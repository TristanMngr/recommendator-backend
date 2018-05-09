package com.isep.recommendator.app.repository;

import com.isep.recommendator.app.model.Concept;
import com.isep.recommendator.app.model.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConceptRepository extends JpaRepository<Concept, Long> {

    Concept findByName(String name);

    @Query("SELECT c.modules FROM Concept c WHERE c.concept_id in (:conceptsids)")
    List<Module> getModulesByConcepts(@Param("conceptsids")List<Long> conceptsids);

}
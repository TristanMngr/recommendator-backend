package com.isep.recommendator.app.repository;

import com.isep.recommendator.app.model.Module;
import com.isep.recommendator.app.model.Requirement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RequirementRepository extends JpaRepository<Requirement, Long> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Requirement r WHERE r.concept.concept_id = :concept_id")
    Integer deleteRequirementByConceptId(@Param("concept_id") Long concept_id);
}

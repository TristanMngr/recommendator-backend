package com.isep.recommendator.app.repository;

import com.isep.recommendator.app.model.Concept;
import com.isep.recommendator.app.model.Module;
import com.isep.recommendator.app.model.Speciality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {
    List<Module> findByName(String name);

    @Query("SELECT spe FROM Module m join m.specialityModules s join m.concepts c join s.speciality spe WHERE c.concept_id in (:concept_ids) GROUP BY spe ORDER BY COUNT(c.id)")
    List<Speciality> getSpecialitiesByConceptsIds(@Param("concept_ids")List<Long> concept_ids);

    @Query("SELECT c FROM Module m join m.specialityModules s join m.concepts c join s.speciality spe WHERE c.concept_id in (:concept_ids) AND spe.id = :spe_id")
    List<Concept> getConceptBySpeIdAndConceptsIds(@Param("spe_id") Long id , @Param("concept_ids")List<Long> concept_ids);
}
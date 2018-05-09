package com.isep.recommendator.app.repository;

import com.isep.recommendator.app.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {
    List<Module> findByName(String name);

    @Query("SELECT spe FROM Module m join m.specialityModules s join m.concepts c join s.speciality spe WHERE c.concept_id in (:concept_ids) GROUP BY spe ORDER BY COUNT(c.id)")
    List<Speciality> getSpecialitiesByConceptsIds(@Param("concept_ids")List<Long> concept_ids);

    @Query("SELECT c FROM Module m join m.specialityModules s join m.concepts c join s.speciality spe WHERE c.concept_id in (:concept_ids) AND spe.id = :spe_id")
    List<Concept> getConceptBySpeIdAndConceptsIds(@Param("spe_id") Long id , @Param("concept_ids")List<Long> concept_ids);

    @Query("SELECT new com.isep.recommendator.app.model.SpecialityAndConcept(spe, c) FROM Module m join m.specialityModules s join m.concepts c join s.speciality spe WHERE c.concept_id in (:concept_ids) ORDER BY spe.speciality_id")
    List<SpecialityAndConcept> getSpecialityAndConceptByConceptIds(@Param("concept_ids")List<Long> concept_ids);

}
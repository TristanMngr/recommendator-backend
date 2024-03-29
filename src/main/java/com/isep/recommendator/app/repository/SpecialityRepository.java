package com.isep.recommendator.app.repository;

import com.isep.recommendator.app.custom_object.Form2Response;
import com.isep.recommendator.app.custom_object.SpeModuleConcept;
import com.isep.recommendator.app.model.Speciality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpecialityRepository extends JpaRepository<Speciality, Long> {
    @Query("SELECT s FROM Speciality s JOIN s.jobs j WHERE j.job_id IN (:job_ids)")
    List<Speciality> findByJobsIds(@Param("job_ids") List<Long> jobIds);
  
    Speciality findByName(String name);

    @Query("SELECT new com.isep.recommendator.app.custom_object.SpeModuleConcept(spe, m, c) FROM Module m join m.specialityModules s join m.concepts c join s.speciality spe WHERE c.concept_id in (:concept_ids) ORDER BY spe.speciality_id")
    List<SpeModuleConcept> getSpeModuleConceptByConceptIds(@Param("concept_ids")List<Long> concept_ids);

    @Query("SELECT new com.isep.recommendator.app.custom_object.Form2Response(s) FROM Speciality s WHERE s.speciality_id NOT IN (:ids)")
    List<Form2Response> getRemainingSpecialitiesAndNullMatchingConcepts(@Param("ids") List<Long> ids);

    @Query("SELECT new com.isep.recommendator.app.custom_object.Form2Response(s) FROM Speciality s")
    List<Form2Response> getAllSpecialitiesWithNoMatchingConcepts();

}

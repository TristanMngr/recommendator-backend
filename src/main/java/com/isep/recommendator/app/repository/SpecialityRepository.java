package com.isep.recommendator.app.repository;

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
}

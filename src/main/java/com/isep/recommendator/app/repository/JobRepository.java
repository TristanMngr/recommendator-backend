package com.isep.recommendator.app.repository;

import com.isep.recommendator.app.custom_object.Form1Response;
import com.isep.recommendator.app.custom_object.SpecialityWithMatchingJobs;
import com.isep.recommendator.app.model.Job;
import com.isep.recommendator.app.model.Speciality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    Job findByName(String name);

    // j'ai mis un order by juste pour les tests, pour l'algo il sert à rien, je ferais gaffe pour la
    // prochaine fois
    @Query(value = "SELECT s, j " +
            "FROM Speciality s " +
            "JOIN s.jobs j " +
            "WHERE j.job_id IN (:job_ids) " +
            "ORDER BY s.speciality_id")
    List<Object[]> findSpecialityWithMatchingJobsByJobIds(@Param("job_ids")List<Long> job_ids);

    @Query(value = "SELECT s, j " +
            "FROM Speciality s " +
            "JOIN s.jobs j " +
            "WHERE j.job_id NOT IN (:job_ids) " +
            "ORDER BY s.speciality_id")
    List<Object[]> findSpecialityWithoutMatchingJobsByJobIds(@Param("job_ids")List<Long> job_ids);

}

package com.isep.recommendator.app.repository;

import com.isep.recommendator.app.custom_object.SpecialityAndConceptObject;
import com.isep.recommendator.app.model.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {
    List<Module> findByName(String name);

    @Query("SELECT m FROM Module m JOIN m.specialityModules sm JOIN sm.speciality s WHERE s.speciality_id IN (:specialitiesIds)")
    List<Module> findBySpecialitiesIds(@Param("specialitiesIds") List<Long> specialitiesIds);
}
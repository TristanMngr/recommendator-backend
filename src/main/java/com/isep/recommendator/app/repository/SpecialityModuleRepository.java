package com.isep.recommendator.app.repository;

import com.isep.recommendator.app.model.SpecialityModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpecialityModuleRepository extends JpaRepository<SpecialityModule, Long> {

    @Query("SELECT sm FROM SpecialityModule sm WHERE sm.module.id = :module_id AND sm.speciality.id = :speciality_id")
    SpecialityModule findBySpecialitiesIds(@Param("speciality_id") Long speciality_id, @Param("module_id") Long module_id);
}

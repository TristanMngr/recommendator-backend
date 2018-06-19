package com.isep.recommendator.app.repository;

import com.isep.recommendator.app.custom_object.SpeModuleConcept;
import com.isep.recommendator.app.model.History;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HistoryRepository extends JpaRepository<History, Long> {

  /*@Query("SELECT m FROM  m JOIN m.specialityModules sm JOIN sm.speciality s WHERE s.speciality_id IN (:specialitiesIds)")
  @Query("SELECT h FROM h JOIN h.")
  List<History> getAllByUser(@Param("user_id") Long user_id);

  @Query("SELECT h FROM h JOIN ")
  History getByUser(@Param("user_id") Long user_id, @Param("history_id") Long history_id);*/
}

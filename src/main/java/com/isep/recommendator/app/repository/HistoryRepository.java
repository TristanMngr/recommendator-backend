package com.isep.recommendator.app.repository;

import com.isep.recommendator.app.custom_object.SpeModuleConcept;
import com.isep.recommendator.app.model.History;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HistoryRepository extends JpaRepository<History, Long> {

  @Query
  List<History> getAllByUser(@Param("user_id") Long user_id);

  @Query
  History getByUser(@Param("user_id") Long user_id, @Param("history_id") Long history_id);
}

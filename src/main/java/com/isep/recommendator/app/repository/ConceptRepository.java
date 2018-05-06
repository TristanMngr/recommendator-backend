package com.isep.recommendator.app.repository;

import com.isep.recommendator.app.model.Concept;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConceptRepository extends JpaRepository<Concept, Long> {

    Concept findByName(String name);
}
package com.isep.recommendator.repository;

import com.isep.recommendator.model.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {

}
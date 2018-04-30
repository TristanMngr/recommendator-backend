package com.isep.recommendator.app.repository;

import com.isep.recommendator.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String name);
}
package com.isep.recommendator.app.repository;

import com.isep.recommendator.app.model.History;
import com.isep.recommendator.app.model.User;

import org.hibernate.sql.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);


    @Query("SELECT u, h FROM u JOIN u.histories h WHERE u.user_id = :userId AND h.history_id = :historyId")
    History getHistoryByUser(@Param("userId") Long user_id, @Param("historyId") Long history_id);

    @Query("SELECT u, h FROM u JOIN u.histories h WHERE u.user_id = :userId")
    List<History> getAllHistoriesByUser(@Param("userId") Long user_id);

}

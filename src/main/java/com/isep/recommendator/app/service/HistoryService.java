package com.isep.recommendator.app.service;

import com.isep.recommendator.app.handler.ResourceNotFoundException;
import com.isep.recommendator.app.model.History;
import com.isep.recommendator.app.repository.HistoryRepository;
import com.isep.recommendator.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class HistoryService {

  private final HistoryRepository historyRepo;
  private final UserRepository userRepo;

  @Autowired
  public HistoryService(HistoryRepository historyRepo, UserRepository userRepo){
    this.historyRepo = historyRepo;
    this.userRepo = userRepo;
  }

  public History get(Long id){
    Optional history = historyRepo.findById(id);
    if (history.isPresent())
      return (History) history.get();
    else
      throw new ResourceNotFoundException("no history found with id " + id);
  }

  public History getByUser(Long user_id, Long history_id){
    return userRepo.getHistoryByUser(user_id, history_id);
  }

  public List<History> getAllByUser(Long user_id){
    return userRepo.getAllHistoriesByUser(user_id);
  }


}

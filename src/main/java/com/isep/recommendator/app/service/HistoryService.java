package com.isep.recommendator.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.isep.recommendator.app.model.History;
import com.isep.recommendator.app.repository.ConceptRepository;
import com.isep.recommendator.app.repository.ModuleRepository;
import com.isep.recommendator.app.repository.SpecialityModuleRepository;
import com.isep.recommendator.app.repository.UserRepository;
import com.isep.recommendator.app.repository.HistoryRepository;


import java.util.List;


@Service
public class HistoryService {

  private final HistoryRepository historyRepo;
  private final UserRepository userRepo;

  @Autowired
  public HistoryService(HistoryRepository historyRepo, UserRepository userRepo){
    this.historyRepo = historyRepo;
    this.userRepo = userRepo;
  }

  public History getByUser(Long user_id, Long history_id){
    return historyRepo.getByUser(user_id, history_id);
  }

  public List<History> getAllByUser(Long user_id){
    return historyRepo.getAllByUser(user_id);
  }


}

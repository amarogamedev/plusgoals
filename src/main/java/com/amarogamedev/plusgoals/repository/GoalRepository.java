package com.amarogamedev.plusgoals.repository;

import com.amarogamedev.plusgoals.domain.Goal;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoalRepository extends MongoRepository<Goal, String> {

}

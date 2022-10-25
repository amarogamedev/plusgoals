package com.amarogamedev.plusgoals.repository;

import com.amarogamedev.plusgoals.domain.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends MongoRepository<Task, String> {

}

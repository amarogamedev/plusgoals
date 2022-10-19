package com.amarogamedev.plusgoals.repository;

import com.amarogamedev.plusgoals.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

}

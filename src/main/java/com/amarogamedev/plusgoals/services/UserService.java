package com.amarogamedev.plusgoals.services;

import com.amarogamedev.plusgoals.domain.User;
import com.amarogamedev.plusgoals.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public List<User> findALl(){
        return userRepository.findAll();
    }
}

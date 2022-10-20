package com.amarogamedev.plusgoals.services;

import com.amarogamedev.plusgoals.domain.User;
import com.amarogamedev.plusgoals.dto.UserDTO;
import com.amarogamedev.plusgoals.repository.UserRepository;
import com.amarogamedev.plusgoals.services.exception.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public List<User> findALl(){
        return userRepository.findAll();
    }

    public User findById(String id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado"));
    }

    public User insert(User user) {
        return userRepository.insert(user);
    }

    public void delete(String id) {
        findById(id);
        userRepository.deleteById(id);
    }

    public User update(String id, User user) {
        User newUser = user;
        newUser.setId(id);
        return userRepository.save(newUser);
    }

    public User fromDto(UserDTO userDTO) {
        return new User(userDTO.getId(), userDTO.getName(), userDTO.getEmail(), userDTO.getPassword());
    }
}

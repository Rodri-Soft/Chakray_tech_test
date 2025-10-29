package com.example.demo.services.Interfaces;

import java.util.List;
import java.util.Optional;

import com.example.demo.models.User;

public interface IUserService {
  
  List<User> getAllUsers();
  List<User> getSortedUsers(String sortedBy);
  List<User> getUsersByFilter(String[] filters);;
  boolean existsByTaxId(String taxId);
  User saveUser(User user);
  Optional<User> updateUser(String id, User userUpdates); 
  void deleteUser(String id);
}

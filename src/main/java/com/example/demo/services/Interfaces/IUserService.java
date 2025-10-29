package com.example.demo.services.Interfaces;

import java.util.List;
import java.util.Optional;

import com.example.demo.models.User;
import com.example.demo.models.dto.SaveUserDTO;
import com.example.demo.models.dto.UpdateUserDTO;

public interface IUserService {
  
  List<User> getAllUsers();
  List<User> getSortedUsers(String sortedBy);
  List<User> getUsersByFilter(String[] filters);;
  boolean existsByTaxId(String taxId);
  User saveUser(SaveUserDTO user);
  Optional<User> updateUser(String id, UpdateUserDTO dto);
  void deleteUser(String id);
  User logInUser(String taxId, String password);
}

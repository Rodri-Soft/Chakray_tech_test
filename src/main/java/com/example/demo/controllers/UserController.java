package com.example.demo.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.Response;
import com.example.demo.models.User;
import com.example.demo.models.dto.LogInDTO;
import com.example.demo.models.dto.SaveUserDTO;
import com.example.demo.models.dto.UpdateUserDTO;
import com.example.demo.services.UserValidator;
import com.example.demo.services.Interfaces.IUserService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "Users management")
public class UserController {

  @Autowired
  IUserService userService;

  @GetMapping("/findAll")
  public List<User> getAllUsers() {
    return userService.getAllUsers();
  }

  @GetMapping()
  public ResponseEntity<List<User>> getFilteredUsers(@RequestParam(required = false) String sortedBy) {

    try {
      if (sortedBy == null || sortedBy.isBlank()) {
        return ResponseEntity.ok(userService.getAllUsers());
      }

      return ResponseEntity.ok(userService.getSortedUsers(sortedBy));

    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }

  @GetMapping("/filter")
  public ResponseEntity<Response> getUsersByFilter(@RequestParam String filter) {

    Response response = new Response();

    try {

      if (filter == null || filter.isBlank()) {
        response.setMessage("Filters required");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
      }

      String[] filters = filter.split("\\+");
      if (filters.length != 3) {
        response.setMessage("Filters required");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
      }

      List<User> result = userService.getUsersByFilter(filters);
      if (result.isEmpty()) {
        response.setMessage("Not users found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
      }

      response.message = "Users found correctly";
      response.data = result;

      return ResponseEntity.ok(response);

    } catch (Exception e) {
      response.message = e.getMessage();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

  }

  @PostMapping("/save")
  public ResponseEntity<Response> saveUser(@RequestBody SaveUserDTO user) {

    Response response = new Response();

    try {

      UserValidator userValidator = new UserValidator();
      userValidator.validateSaveUserDTO(user);

      if (userService.existsByTaxId(user.getTaxId())) {
        response.message = "RFC already created";
        return ResponseEntity.badRequest().body(response);
      }

      User savedUser =userService.saveUser(user);
      response.message = "User saved correctly";
      response.data = savedUser;
      return ResponseEntity.ok(response);

    } catch (Exception e) {
      response.message = e.getMessage();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

    }
  }

  @PatchMapping("/{id}")
  public ResponseEntity<Response> updateUser(@RequestBody UpdateUserDTO user, @PathVariable String id) {

    Response response = new Response();

    try {

      UserValidator userValidator = new UserValidator();
      userValidator.validateUpdateUserDTO(user);

      Optional<User> userOptional = userService.updateUser(id, user);
      if (userOptional.isEmpty()) {
        response.message = "User not found";
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
      }

      response.message = "User updated correctly";
      response.data = userOptional.get();
      return ResponseEntity.ok(response);

    } catch (Exception e) {
      response.message = e.getMessage();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

    }
  }


  @DeleteMapping("/{id}")  
  public ResponseEntity<Response> deleteUser(@PathVariable String id) {
    Response response = new Response();
    try {
      userService.deleteUser(id);
      response.message = "User deleted correctly";
      return ResponseEntity.ok(response);

    } catch (RuntimeException e) {      
      response.message = e.getMessage();
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

    } catch (Exception e) {      
      response.message = "Error deleting user: " + e.getMessage();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
  }

  @PostMapping("/login")
  public ResponseEntity<Response> login(@RequestBody LogInDTO logInDTO) {

    Response response = new Response();

    try {
        
      UserValidator userValidator = new UserValidator();
      userValidator.validateLogInDTO(logInDTO);

      User user = userService.logInUser(logInDTO.getTaxId(), logInDTO.getPassword());
      response.message = "Login successful";
      response.data = user;

      return ResponseEntity.ok(response);

    } catch (RuntimeException e) {
      response.message = e.getMessage();
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    } catch (Exception e) {
      response.message = "Error during login: " + e.getMessage();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
  }

}

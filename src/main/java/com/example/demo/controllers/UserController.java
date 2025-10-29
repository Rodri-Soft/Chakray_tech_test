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
import com.example.demo.services.Interfaces.IUserService;

import io.swagger.v3.oas.annotations.Operation;
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
  public ResponseEntity<Response> saveUser(@RequestBody User user) {

    Response response = new Response();

    try {

      if (user.getEmail() == null || user.getEmail().isBlank()) {
        response.message = "Email required";
        return ResponseEntity.badRequest().body(response);
      }
      if (user.getTaxId() == null || user.getTaxId().isBlank()) {
        response.message = "Tax_id required";
        return ResponseEntity.badRequest().body(response);
      }
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
  @Operation(summary = "Update user", description = "Update user")
  public ResponseEntity<Response> patchUser(@PathVariable String id, @RequestBody User user) {
    
    Response response = new Response();
    try {      
      Optional<User> updatedUser = userService.updateUser(id, user);
      response.message = "User updated correctly";
      response.data = updatedUser;
      return ResponseEntity.ok(response);

    } catch (RuntimeException e) {
      response.message = e.getMessage();
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

    } catch (Exception e) {
      response.message = "Error updating user: " + e.getMessage();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete user by ID")
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

}

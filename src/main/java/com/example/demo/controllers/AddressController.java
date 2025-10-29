package com.example.demo.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.Address;
import com.example.demo.models.Response;
import com.example.demo.models.dto.AddressDTO;
import com.example.demo.services.Interfaces.IAddressService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/addresses")
@Tag(name = "Address", description = "Address management")
public class AddressController {

  @Autowired
  IAddressService addressService;

  @GetMapping("/findAll")
  public ResponseEntity<List<Address>> getAllUsers() {
     try {
     
      return ResponseEntity.ok(addressService.getAllAddress());

    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }

  @PostMapping("/save")
  public ResponseEntity<Response> saveUser(@RequestBody AddressDTO address) {

    Response response = new Response();

    try {

      if (address.getName() == null || address.getName().isBlank()) {
        response.message = "Name required";
        return ResponseEntity.badRequest().body(response);
      }
      if (address.getStreet() == null || address.getStreet().isBlank()) {
        response.message = "Street required";
        return ResponseEntity.badRequest().body(response);
      }

      if (address.getCountryCode().isEmpty() && address.getCountryCode().length() != 2) {
        response.message = "Country code required in the correct format";
        return ResponseEntity.badRequest().body(response);
      }

      if (address.getUserId() == null || address.getUserId().isBlank()) {
        response.message = "User id required";
        return ResponseEntity.badRequest().body(response);
      }

      Optional<Address> newAddress = addressService.saveAddress(address);

      if (newAddress.isEmpty()) {
        response.message = "Address not saved";
        return ResponseEntity.badRequest().body(response);
      }

      response.message = "Address saved correctly";
      response.data = newAddress;
      return ResponseEntity.ok(response);

    } catch (Exception e) {
      response.message = e.getMessage();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

    }
  }

}

package com.example.demo.services.Interfaces;

import java.util.List;
import java.util.Optional;

import com.example.demo.models.Address;
import com.example.demo.models.dto.AddressDTO;

public interface IAddressService {
  
  List<Address> getAllAddress();
  Optional<Address> saveAddress(AddressDTO address);
}

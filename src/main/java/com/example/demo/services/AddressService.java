package com.example.demo.services;

import java.lang.StackWalker.Option;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.repositories.AddressRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.Interfaces.IAddressService;
import com.example.demo.models.Address;
import com.example.demo.models.User;
import com.example.demo.models.dto.AddressDTO;

@Service
public class AddressService implements IAddressService {

  @Autowired
  AddressRepository addressRepository;

  @Autowired
  UserRepository userRepository;

  public List<Address> getAllAddress() {
    return (List<Address>) addressRepository.findAll();
  }

  @Override
  public Optional<Address> saveAddress(AddressDTO addressDto) {    

    Optional<User> user = userRepository.findById(addressDto.getUserId());

    if (user.isEmpty()) {
      return null;
    }

    Address address = new Address();
    address.setName(addressDto.getName());
    address.setStreet(addressDto.getStreet());
    address.setCountryCode(addressDto.getCountryCode());
    address.setUser(user.get());

    Address savedAddress = addressRepository.save(address);    

    return Optional.ofNullable(savedAddress);
  }

}

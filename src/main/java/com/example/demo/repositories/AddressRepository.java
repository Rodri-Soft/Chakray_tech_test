package com.example.demo.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Address;

@Repository
public interface AddressRepository extends CrudRepository<Address, Long> {
  
}

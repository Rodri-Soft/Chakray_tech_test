package com.example.demo.models.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDTO {
  private String name;
  private String street;
  private String countryCode;
  private String userId;
}

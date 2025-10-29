package com.example.demo.models.dto;

import java.time.LocalDateTime;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaveUserDTO {
  
  private String email;  
  private String name;  
  private String phone;    
  private String taxId;    
}
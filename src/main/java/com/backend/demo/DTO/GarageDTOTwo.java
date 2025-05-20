package com.backend.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GarageDTOTwo {
    private String garageId;
    private String name;
    private String proprietorName;
    private String email;
    private String phoneNumber;
}
package org.example.hibernate.dto;

import org.example.hibernate.entity.PersonalInfo;

public record UserReadDto(Long id,
                          PersonalInfo personalInfo,
                          String username,
                          String info,
                          CompanyReadDto companyReadDto) {
}

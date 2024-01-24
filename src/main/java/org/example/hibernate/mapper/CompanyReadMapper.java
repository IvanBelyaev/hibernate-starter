package org.example.hibernate.mapper;

import org.example.hibernate.dto.CompanyReadDto;
import org.example.hibernate.entity.Company;
import org.hibernate.Hibernate;

public class CompanyReadMapper implements Mapper<Company, CompanyReadDto> {

    @Override
    public CompanyReadDto mapFrom(Company company) {
        Hibernate.initialize(company.getLocaleInfos());
        return new CompanyReadDto(company.getId(), company.getName(), company.getLocaleInfos());
    }
}

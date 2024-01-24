package org.example.hibernate.dao;

import jakarta.persistence.EntityManager;
import org.example.hibernate.entity.Company;

public class CompanyRepository extends BaseRepository<Integer, Company> {

    public CompanyRepository(EntityManager entityManager) {
        super(Company.class, entityManager);
    }
}

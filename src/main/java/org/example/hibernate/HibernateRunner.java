package org.example.hibernate;

import org.example.hibernate.entity.Company;
import org.example.hibernate.entity.User;
import org.example.hibernate.util.HibernateUtil;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateRunner {
    private static final Logger log = LoggerFactory.getLogger(HibernateRunner.class);

    public static void main(String[] args) {
        final Company google = Company.builder()
                .name("Google71")
                .build();
        User user = null;
        log.info("user in transient state, object: {}", user);
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();

                final User user1 = session.get(User.class, 1L);
                final Company companyProxy = user1.getCompany();
                final Company company1 = (Company) Hibernate.unproxy(user1.getCompany());

                session.getTransaction().commit();
            }
        }
    }
}

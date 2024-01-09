package org.example.hibernate.util;

import lombok.Cleanup;
import lombok.experimental.UtilityClass;
import org.example.hibernate.entity.Company;
import org.example.hibernate.entity.Payment;
import org.example.hibernate.entity.PersonalInfo;
import org.example.hibernate.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

@UtilityClass
public class TestDataImporter {
    public void importData(SessionFactory sessionFactory) {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        Company google = saveCompany(session, "google");
        Company meta = saveCompany(session, "meta");
        Company jetBrains = saveCompany(session, "jetBrains");

        User ivan = saveUser(session, "Ivan", "Ivanov", google);
        User semen = saveUser(session, "Semen", "Semenov", google);
        User kirill = saveUser(session, "Kirill", "Kirillov", meta);
        User maxim = saveUser(session, "Maxim", "Maximov", meta);
        User andrey = saveUser(session, "Andrey", "Andreev", jetBrains);

        savePayment(session, ivan, 100);
        savePayment(session, ivan, 200);
        savePayment(session, ivan, 300);

        savePayment(session, semen, 200);
        savePayment(session, semen, 300);
        savePayment(session, semen, 400);

        savePayment(session, kirill, 300);
        savePayment(session, kirill, 300);
        savePayment(session, kirill, 210);

        savePayment(session, maxim, 200);
        savePayment(session, maxim, 200);
        savePayment(session, maxim, 200);

        savePayment(session, andrey, 400);
        savePayment(session, andrey, 500);
        savePayment(session, andrey, 600);

        session.getTransaction().commit();
    }

    private Company saveCompany(Session session, String companyName) {
        Company company = Company.builder()
                .name(companyName)
                .build();
        session.persist(company);
        return company;
    }

    private User saveUser(Session session, String firstName, String lastName, Company company) {
        User user = User.builder()
                .username(firstName + " " + lastName)
                .personalInfo(
                        PersonalInfo.builder()
                                .firstName(firstName)
                                .lastName(lastName)
                                .build()
                )
                .company(company)
                .build();
        session.persist(user);
        return user;
    }

    private Payment savePayment(Session session, User receiver, Integer amount) {
        Payment payment = Payment.builder()
                .amount(amount)
                .receiver(receiver)
                .build();
        session.persist(payment);
        return payment;
    }
}

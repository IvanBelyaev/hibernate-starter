package org.example.hibernate.dao;

import com.querydsl.core.Tuple;
import lombok.Cleanup;
import org.example.hibernate.dto.UserFilter;
import org.example.hibernate.entity.Payment;
import org.example.hibernate.entity.User;
import org.example.hibernate.util.HibernateTestUtil;
import org.example.hibernate.util.TestDataImporter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserDaoTest {
    private SessionFactory sessionFactory = HibernateTestUtil.buildSessionFactory();
    private UserDao userDao = UserDao.getInstance();

    @BeforeAll
    public void initialize() {
        TestDataImporter.importData(sessionFactory);
    }

    @AfterAll
    public void cleanUp() {
        sessionFactory.close();
    }

    @Test
    void testFindAll() {
        @Cleanup Session session = sessionFactory.openSession();
        List<User> users = userDao.findAll(session);

        assertThat(users.stream().map(User::getFullName).collect(toList()))
                .containsExactlyInAnyOrder("Ivan Ivanov", "Semen Semenov", "Kirill Kirillov",
                        "Maxim Maximov", "Andrey Andreev");
    }

    @Test
    void testFindAllByFirstName() {
        @Cleanup Session session = sessionFactory.openSession();
        List<User> users = userDao.findAllByFirstName(session, "Ivan");

        assertThat(users).hasSize(1);
        assertThat(users.get(0).getFullName()).isEqualTo("Ivan Ivanov");
    }

    @Test
    void testFindLimitedOrderedByFirstName() {
        @Cleanup Session session = sessionFactory.openSession();
        List<User> users = userDao.findLimitedOrderedByFirstName(session, 3);

        assertThat(users.stream().map(User::getFullName).collect(toList()))
                .containsExactly("Andrey Andreev", "Ivan Ivanov", "Kirill Kirillov");
    }

    @Test
    void testFindAllByCompanyName() {
        @Cleanup Session session = sessionFactory.openSession();
        List<User> users = userDao.findAllByCompanyName(session, "google");

        assertThat(users.stream().map(User::getFullName).collect(toList()))
                .containsExactlyInAnyOrder("Ivan Ivanov", "Semen Semenov");
    }

    @Test
    void testFindAllPaymentsByCompanyName() {
        @Cleanup Session session = sessionFactory.openSession();
        List<Payment> payments = userDao.findAllPaymentsByCompanyName(session, "google");

        List<String> paymentsInfo = payments.stream()
                .map(p -> String.format("%s %s", p.getReceiver().getPersonalInfo().getFirstName(), p.getAmount()))
                .collect(toList());
        assertThat(paymentsInfo)
                .containsExactly("Ivan 100", "Ivan 200", "Ivan 300", "Semen 200", "Semen 300", "Semen 400");
    }

    @Test
    void testFindAveragePaymentAmountByFirstAndLastNames() {
        @Cleanup Session session = sessionFactory.openSession();
        var filter = UserFilter.builder()
                .firstName("Ivan")
                .lastName("Ivanov")
                .build();
        Double avgPayment =
                userDao.findAveragePaymentAmountByFirstAndLastNames(session, filter);

        assertThat(avgPayment).isEqualTo(200);
    }

    @Test
    void testFindCompanyNamesWithAvgUserPaymentsOrderedByCompanyName() {
        @Cleanup Session session = sessionFactory.openSession();
        List<Tuple> companyInfo = userDao.findCompanyNamesWithAvgUserPaymentsOrderedByCompanyName(session);

        assertThat(
                companyInfo.stream()
                        .map(it -> it.get(0, String.class) + " " + it.get(1, Double.class))
        )
                .containsExactlyInAnyOrder("google 250.0", "meta 235.0", "jetBrains 500.0");
    }

    @Test
    void testIsItPossible() {
        @Cleanup Session session = sessionFactory.openSession();
        List<Tuple> topUsers = userDao.isItPossible(session);

        List<String> usersInfo = topUsers.stream()
                .map(t -> t.get(0, User.class).getPersonalInfo().getFirstName() + " " + t.get(1, Double.class))
                .collect(toList());
        assertThat(usersInfo).containsExactlyInAnyOrder("Semen 300.0", "Andrey 500.0");
    }
}

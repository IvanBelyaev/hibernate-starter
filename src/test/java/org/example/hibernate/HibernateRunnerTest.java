package org.example.hibernate;

import jakarta.persistence.Column;
import jakarta.persistence.FlushModeType;
import jakarta.persistence.Table;
import lombok.Cleanup;
import org.example.hibernate.entity.*;
import org.example.hibernate.util.HibernateTestUtil;
import org.example.hibernate.util.TestDataImporter;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.jpa.AvailableHints;
import org.junit.jupiter.api.*;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HibernateRunnerTest {
    private SessionFactory sessionFactory = HibernateTestUtil.buildSessionFactory();

    @BeforeAll
    public void initialize() {
        TestDataImporter.importData(sessionFactory);
    }

    @AfterAll
    public void cleanUp() {
        sessionFactory.close();
    }

    private String insertUser = """
            insert into %s (%s)
            values (%s);
            """;

    @Test
    void insertEntity() {
        User user = User.builder()
                .username("testUser")
                .build();

        String tableName = ofNullable(user.getClass().getAnnotation(Table.class))
                .map(table -> table.schema() + "." + table.name())
                .orElse(user.getClass().getName());

        Field[] declaredFields = user.getClass().getDeclaredFields();
        String fieldNames = Arrays.stream(declaredFields)
                .map(field -> ofNullable(field.getAnnotation(Column.class))
                        .map(Column::name)
                        .orElse(field.getName()))
                .collect(joining(", "));

        String fieldValues = Arrays.stream(declaredFields)
                .map(field -> "?")
                .collect(joining(", "));

        System.out.printf((insertUser) + "%n", tableName, fieldNames, fieldValues);
    }

    @Test
    void oneToMany() {
        @Cleanup final Session session = sessionFactory.openSession();
        session.beginTransaction();

        final Company company = session.get(Company.class, 1L);
        System.out.println();

        session.getTransaction().commit();
    }

    @Test
    void addUserToNewCompany() {
        @Cleanup final Session session = sessionFactory.openSession();
        session.beginTransaction();

        final Company company = Company.builder()
                .name("Meta3")
                .build();
        final User user = User.builder()
                .username("testUser")
                .build();
        company.addUser(user);
        session.persist(company);

        session.getTransaction().rollback();
    }

    @Test
    void deleteCompany() {
        @Cleanup final Session session = sessionFactory.openSession();
        session.beginTransaction();

        final Company company = session.get(Company.class, 1);
        session.remove(company);

        session.getTransaction().rollback();
    }

    @Test
    void deleteUser() {
        @Cleanup final Session session = sessionFactory.openSession();
        session.beginTransaction();

        final User user = session.get(User.class, 2L);
        Optional.ofNullable(user).ifPresent(session::remove);

        session.getTransaction().rollback();
    }

    @Test
    void orphanRemoval() {
        @Cleanup Session session = sessionFactory.openSession();

        session.beginTransaction();

        Company company = session.getReference(Company.class, 3L);
//            company.getUsers().removeIf(user -> user.getId().equals(4L));

        session.getTransaction().rollback();
    }

    @Test
    void hibernateInitialize() {
        Company company;
        @Cleanup Session session = sessionFactory.openSession();

        session.beginTransaction();

        company = session.getReference(Company.class, 3L);
        Hibernate.initialize(company.getUsers());

        session.getTransaction().rollback();

        company.getUsers().size();
    }

    @Test
    void oneToOne() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

//            User user = session.get(User.class, 14L);
        Profile profile = session.get(Profile.class, 1L);
//
//            User user = User.builder()
//                    .username("test3@gmail.com")
//                    .build();
//            Profile profile = Profile.builder()
//                    .language("ru")
//                    .street("Lenina")
//                    .build();
//            profile.setUser(user);
//            session.persist(user);

        session.getTransaction().rollback();
    }

    @Test
    void manyToMany() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        User user = session.get(User.class, 1L);
        Chat chat = session.get(Chat.class, 1L);
        if (user != null && chat != null) {
            UserChat userChat = UserChat.builder()
                    .user(user)
                    .chat(chat)
                    .createdBy("admin")
                    .createdAt(Instant.now())
                    .build();
            session.persist(userChat);
        }

        session.getTransaction().rollback();

    }

    @Test
    void elementCollection() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        Company company = session.get(Company.class, 1);
        System.out.println(company.getLocaleInfos());
//            LocaleInfo localeOne = LocaleInfo.of("ru", "Описание на русском");
//            LocaleInfo localeTwo = LocaleInfo.of("en", "English description");
//            company.getLocaleInfos().add(localeOne);
//            company.getLocaleInfos().add(localeTwo);

        session.getTransaction().rollback();
    }

    @Test
    void collectionSort() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        Company company = session.get(Company.class, 3);
        company.getUsers().forEach((k, v) -> System.out.println(v));

        session.getTransaction().rollback();
    }

    @Test
    void mapKeyColumn() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        Company company = session.get(Company.class, 3);
        company.getLocaleInfos().forEach((k, v) -> System.out.println(v));

        session.getTransaction().rollback();
    }

    @Test
    void testTestContainer() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        Company company = Company.builder()
                .name("RedSoft")
                .build();
        session.persist(company);

        session.getTransaction().rollback();
    }

    @Disabled
    @Test
    void testInheritance() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        Company google = Company.builder()
                .name("Google")
                .build();
        session.persist(google);

        Programmer programmer = Programmer.builder()
                .username("ivan")
                .company(google)
                .language(Language.Java)
                .build();
        session.persist(programmer);

        Manager manager = Manager.builder()
                .username("Dmitriy")
                .company(google)
                .projectName("test project")
                .build();
        session.persist(manager);
        session.flush();
        session.clear();

        Programmer programmer1 = session.get(Programmer.class, 1L);
        User user = session.get(User.class, 2L);

        session.getTransaction().rollback();
    }

    @Test
    void testHql() {
        @Cleanup Session session = sessionFactory.openSession();
        session.beginTransaction();

        List<User> users = session.createNamedQuery(
                        "selectByName",
                        User.class
                )
                .setParameter("firstName", "Ivan")
                .setParameter("companyName", "google")
                .setHint(AvailableHints.HINT_FETCH_SIZE, 50)
                .setFlushMode(FlushModeType.AUTO)
                .list();

        session.getTransaction().commit();
    }
}
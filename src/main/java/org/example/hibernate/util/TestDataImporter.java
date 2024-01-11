package org.example.hibernate.util;

import lombok.Cleanup;
import lombok.experimental.UtilityClass;
import org.example.hibernate.entity.*;
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

        var familyChat = saveChat(session, "family");
        var jobChat = saveChat(session, "job");
        var schoolChat = saveChat(session, "school");

        saveUserChat(session, ivan, familyChat);
        saveUserChat(session, ivan, jobChat);
        saveUserChat(session, ivan, schoolChat);
        saveUserChat(session, semen, familyChat);
        saveUserChat(session, semen, jobChat);
        saveUserChat(session, kirill, jobChat);
        saveUserChat(session, maxim, familyChat);
        saveUserChat(session, maxim, schoolChat);
        saveUserChat(session, andrey, familyChat);
        saveUserChat(session, andrey, jobChat);

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

    private Chat saveChat(Session session, String chatName) {
        var chat = Chat.builder()
                .name(chatName)
                .build();
        session.persist(chat);
        return chat;
    }

    private UserChat saveUserChat(Session session, User user, Chat chat) {
        var userChat = UserChat.builder()
                .user(user)
                .chat(chat)
                .build();
        session.persist(userChat);
        return userChat;
    }
}

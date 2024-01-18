package org.example.hibernate;

import jakarta.transaction.Transactional;
import org.example.hibernate.entity.Payment;
import org.example.hibernate.entity.User;
import org.example.hibernate.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateRunner {
    private static final Logger log = LoggerFactory.getLogger(HibernateRunner.class);

    @Transactional
    public static void main(String[] args) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();

                User user = session.get(User.class, 1L);
                user.getCompany().getName();
//                user.getPayments().size();
                user.getUserChats().size();

                var payments= session.createQuery(
                        "select p from Payment p where p.receiver.id = :userId", Payment.class)
                        .setParameter("userId", 1L)
//                        .setCacheRegion("query_cache")
                        .setCacheable(true)
                        .getResultList();

                var statistics =
                        sessionFactory.getStatistics().getCacheRegionStatistics("Users");
                System.out.println(statistics);

                session.getTransaction().commit();
            }

            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();

                User user = session.get(User.class, 1L);
                user.getCompany().getName();
//                user.getPayments().size();
                user.getUserChats().size();

                var payments= session.createQuery(
                                "select p from Payment p where p.receiver.id = :userId", Payment.class)
                        .setParameter("userId", 1L)
//                        .setCacheRegion("query_cache")
                        .setCacheable(true)
                        .getResultList();

                var statistics =
                        sessionFactory.getStatistics().getCacheRegionStatistics("Users");
                System.out.println(statistics);

                session.getTransaction().commit();
            }
        }
    }
}

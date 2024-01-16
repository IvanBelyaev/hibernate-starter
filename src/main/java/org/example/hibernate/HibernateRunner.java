package org.example.hibernate;

import jakarta.transaction.Transactional;
import org.example.hibernate.entity.Payment;
import org.example.hibernate.interceptor.GlobalInterceptor;
import org.example.hibernate.util.HibernateUtil;
import org.example.hibernate.util.TestDataImporter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateRunner {
    private static final Logger log = LoggerFactory.getLogger(HibernateRunner.class);

    @Transactional
    public static void main(String[] args) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory
                     .withOptions()
                     .interceptor(new GlobalInterceptor())
                     .openSession()) {
            TestDataImporter.importData(sessionFactory);
            session.beginTransaction();

            var payment = session.get(Payment.class, 1L);
            payment.setAmount(payment.getAmount() + 10);

            var paymentForRemove = session.get(Payment.class, 2L);
            session.remove(paymentForRemove);

            session.getTransaction().commit();

        }
    }
}

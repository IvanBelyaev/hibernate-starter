package org.example.hibernate;

import jakarta.transaction.Transactional;
import org.example.hibernate.entity.Payment;
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
             Session sessionOne = sessionFactory.openSession();
             Session sessionTwo = sessionFactory.openSession()) {
            TestDataImporter.importData(sessionFactory);
            sessionTwo.beginTransaction();

//            sessionTwo.setDefaultReadOnly(true);

//            sessionTwo.createNativeQuery("SET TRANSACTION READ ONLY;", Void.class).executeUpdate();

//            sessionOne.doWork(connection ->
//                    System.out.printf("transaction isolation level - %d%n",
//                            connection.getTransactionIsolation()));

//            Map<String, Object> props = Map.of(AvailableHints.HINT_SPEC_LOCK_TIMEOUT, 10000);
//
//            CompletableFuture
//                    .runAsync(() -> {
//                        sessionOne.beginTransaction();
//
//
//                        var payment = sessionOne.find(Payment.class, 1L,
//                                LockModeType.PESSIMISTIC_READ, props);
//                        payment.setAmount(payment.getAmount() + 10);
//
//                        sessionOne.getTransaction().commit();
//                    });


            var theSamePayment = sessionTwo.find(Payment.class, 1L);
            theSamePayment.setAmount(theSamePayment.getAmount() + 20);

            sessionTwo.getTransaction().commit();

        }
    }
}

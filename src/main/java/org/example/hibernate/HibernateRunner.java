package org.example.hibernate;

import jakarta.transaction.Transactional;
import org.example.hibernate.entity.Payment;
import org.example.hibernate.util.HibernateUtil;
import org.hibernate.ReplicationMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateRunner {
    private static final Logger log = LoggerFactory.getLogger(HibernateRunner.class);

    @Transactional
    public static void main(String[] args) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            var payment = session.get(Payment.class, 1L);
            payment.setAmount(payment.getAmount() + 10);

            session.getTransaction().commit();

        }

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session2 = sessionFactory.openSession()) {
            session2.beginTransaction();

            var auditReader = AuditReaderFactory.get(session2);
            var payment = auditReader.find(Payment.class, 1L, 1L);
            session2.replicate(payment, ReplicationMode.OVERWRITE);

            var resultList = auditReader.createQuery()
                    .forEntitiesAtRevision(Payment.class, 400L)
                    .addProjection(AuditEntity.id())
                    .addProjection(AuditEntity.property("amount"))
                    .add(AuditEntity.property("amount").gt(300))
                    .add(AuditEntity.property("id").ge(14))
                    .getResultList();

            resultList.forEach(System.out::println);

            session2.getTransaction().commit();

        }
    }
}

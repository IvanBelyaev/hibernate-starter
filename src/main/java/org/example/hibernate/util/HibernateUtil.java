package org.example.hibernate.util;

import lombok.experimental.UtilityClass;
import org.example.hibernate.converter.BirthdayConverter;
import org.example.hibernate.entity.Audit;
import org.example.hibernate.entity.Revision;
import org.example.hibernate.interceptor.GlobalInterceptor;
import org.example.hibernate.listener.AuditTableListener;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Configuration;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;

@UtilityClass
public class HibernateUtil {
    public static SessionFactory buildSessionFactory() {
        Configuration configuration = buildConfiguration();
        var sessionFactory = configuration.buildSessionFactory();

//        registerListeners(sessionFactory);

        return sessionFactory;
    }

    private static void registerListeners(SessionFactory sessionFactory) {
        var sessionFactoryImpl = sessionFactory.unwrap(SessionFactoryImpl.class);
        var listenerRegistry =
                sessionFactoryImpl.getServiceRegistry().getService(EventListenerRegistry.class);
        var auditTableListener = new AuditTableListener();
        listenerRegistry.appendListeners(EventType.PRE_INSERT, auditTableListener);
        listenerRegistry.appendListeners(EventType.PRE_DELETE, auditTableListener);
    }

    public static Configuration buildConfiguration() {
        return new Configuration()
                .setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy())
                .addAnnotatedClass(Audit.class)
                .addAnnotatedClass(Revision.class)
                .addAttributeConverter(BirthdayConverter.class)
                .setInterceptor(new GlobalInterceptor())
                .configure();
    }
}

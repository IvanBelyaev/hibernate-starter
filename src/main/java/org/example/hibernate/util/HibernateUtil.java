package org.example.hibernate.util;

import lombok.experimental.UtilityClass;
import org.example.hibernate.converter.BirthdayConverter;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Configuration;

@UtilityClass
public class HibernateUtil {
    public static SessionFactory buildSessionFactory() {
        Configuration configuration = buildConfiguration();
        return configuration.buildSessionFactory();
    }

    public static Configuration buildConfiguration() {
        return new Configuration()
                .setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy())
                .addAttributeConverter(BirthdayConverter.class)
                .configure();
    }
}

package org.example.hibernate.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.example.hibernate.entity.Birthday;

import java.sql.Date;
import java.util.Optional;

@Converter(autoApply = true)
public class BirthdayConverter implements AttributeConverter<Birthday, Date> {
    @Override
    public Date convertToDatabaseColumn(Birthday birthday) {
        return Optional.ofNullable(birthday)
                .map(Birthday::birthday)
                .map(Date::valueOf)
                .orElse(null);
    }

    @Override
    public Birthday convertToEntityAttribute(Date birthday) {
        return Optional.ofNullable(birthday)
                .map(Date::toLocalDate)
                .map(Birthday::new)
                .orElse(null);
    }
}

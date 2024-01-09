package org.example.hibernate.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class PersonalInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = 8601019012476352625L;

    private String firstName;
    private String lastName;
    //    @Convert(converter = BirthdayConverter.class)
    private Birthday birthDate;
}

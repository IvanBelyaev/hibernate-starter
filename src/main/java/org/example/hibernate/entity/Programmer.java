package org.example.hibernate.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

//@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
//@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("programmer")
//@PrimaryKeyJoinColumn(name = "id")
public class Programmer /*extends User*/ {
    @Enumerated(EnumType.STRING)
    private Language language;

    @Builder
    public Programmer(Long id,
                      PersonalInfo personalInfo,
                      String username,
                      String info,
                      Company company,
                      Profile profile,
                      List<UserChat> userChats,
                      Language language) {
//        super(id, personalInfo, username, info, company, profile, userChats);
        this.language = language;
    }
}



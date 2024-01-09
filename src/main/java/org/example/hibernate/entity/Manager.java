package org.example.hibernate.entity;

import jakarta.persistence.DiscriminatorValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

//@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
//@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("manager")
//@PrimaryKeyJoinColumn(name = "id")
public class Manager /*extends User*/ {
    private String projectName;

    @Builder
    public Manager(Long id,
                      PersonalInfo personalInfo,
                      String username,
                      String info,
                      Company company,
                      Profile profile,
                      List<UserChat> userChats,
                      String projectName) {
//        super(id, personalInfo, username, info, company, profile, userChats);
        this.projectName = projectName;
    }
}

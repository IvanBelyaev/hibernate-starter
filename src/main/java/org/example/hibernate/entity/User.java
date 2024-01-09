package org.example.hibernate.entity;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.util.ArrayList;
import java.util.List;

@NamedQuery(name = "selectByName",
        query = "select u " +
                "from User u " +
                "join u.company c " +
                "where u.personalInfo.firstName = :firstName and c.name = :companyName"
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "username")
@ToString(exclude = {"company", "profile", "userChats", "payments"})
@Entity
@Table(name = "users", schema = "public")
@Builder
//@Inheritance(strategy = InheritanceType.JOINED)
public /*abstract */class User implements Comparable<User> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @AttributeOverride(name = "birthDate", column = @Column(name = "birth_date"))
    private PersonalInfo personalInfo;

    @Column(unique = true, nullable = false)
    private String username;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private String info;

    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;

    @OneToOne(
            mappedBy = "user",
            cascade = CascadeType.ALL
    )
    private Profile profile;

    @OneToMany(mappedBy = "receiver")
    @Builder.Default
    private List<Payment> payments = new ArrayList<>();

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<UserChat> userChats = new ArrayList<>();

    @Override
    public int compareTo(User o) {
        return this.getUsername().compareTo(o.username);
    }

    public String getFullName() {
        return getPersonalInfo().getFirstName() + " " + getPersonalInfo().getLastName();
    }
}

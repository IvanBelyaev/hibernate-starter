package org.example.hibernate.entity;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.FetchProfile.FetchOverride;

import java.util.ArrayList;
import java.util.List;

@NamedEntityGraph(
        name = "withCompanyAndChats",
        attributeNodes = {
                @NamedAttributeNode("company"),
                @NamedAttributeNode(value = "userChats", subgraph = "chats")
        },
        subgraphs = {
                @NamedSubgraph(name = "chats", attributeNodes = {
                        @NamedAttributeNode("chat")
                })
        }
)
@FetchProfile(name = "withCompanyAndPayments", fetchOverrides = {
        @FetchOverride(
                entity = User.class, association = "company", mode = FetchMode.JOIN
        ),
        @FetchOverride(
                entity = User.class, association = "payments", mode = FetchMode.JOIN
        )
})
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
@ToString(exclude = {"company", "userChats", "payments"})
@Entity
@Table(name = "users", schema = "public")
@Builder
//@Inheritance(strategy = InheritanceType.JOINED)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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
//    @Fetch(FetchMode.JOIN)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Company company;

//    @OneToOne(
//            mappedBy = "user",
//            cascade = CascadeType.ALL
//    )
//    private Profile profile;

    @OneToMany(mappedBy = "receiver")
    @Builder.Default
//    @BatchSize(size = 2)
    @Fetch(FetchMode.SUBSELECT)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private List<Payment> payments = new ArrayList<>();

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Users")
    private List<UserChat> userChats = new ArrayList<>();

    @Override
    public int compareTo(User o) {
        return this.getUsername().compareTo(o.username);
    }

    public String getFullName() {
        return getPersonalInfo().getFirstName() + " " + getPersonalInfo().getLastName();
    }
}

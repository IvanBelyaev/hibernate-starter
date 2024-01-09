package org.example.hibernate.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SortNatural;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "users")
@EqualsAndHashCode(of = "name")
@Builder
@Entity
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "company_locale")
//    @AttributeOverride(name = "locale", column = @Column(name = "locale"))
    @Column(name = "description")
    @MapKeyColumn(name = "locale")
    private Map<String, String> localeInfos = new HashMap<>();

    @Column(unique = true, nullable = false)
    private String name;

    @Builder.Default
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
//    @OrderBy("username desc")
//    @OrderColumn(name = "id")
    @SortNatural
    @MapKey(name = "username")
    private Map<String, User> users = new TreeMap<>();

    public void addUser(User user) {
        users.put(user.getUsername(), user);
        user.setCompany(this);
    }
}

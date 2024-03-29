package org.example.hibernate.dao;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.example.hibernate.dto.UserFilter;
import org.example.hibernate.entity.Payment;
import org.example.hibernate.entity.User;
import org.hibernate.Session;

import java.util.List;

import static org.example.hibernate.entity.QCompany.company;
import static org.example.hibernate.entity.QPayment.payment;
import static org.example.hibernate.entity.QUser.user;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDao {
    private static final UserDao INSTANCE = new UserDao();

    /**
     * Возвращает всех сотрудников
     */
    public List<User> findAll(Session session) {
//        return session.createQuery("select u from User u", User.class)
//                .list();

        return new JPAQuery<User>(session)
                .select(user)
                .from(user)
                .fetch();


//        var cb = session.getCriteriaBuilder();
//
//        var criteria = cb.createQuery(User.class);
//        var user = criteria.from(User.class);
//
//        criteria.select(user);
//
//        return session.createQuery(criteria)
//                .list();
    }

    /**
     * Возвращает всех сотрудников c указанным именем
     */
    public List<User> findAllByFirstName(Session session, String firstName) {
//        return session.createQuery("select u from User u where u.personalInfo.firstName = :firstName", User.class)
//                .setParameter("firstName", firstName)
//                .list();

        return new JPAQuery<User>(session)
                .select(user)
                .from(user)
                .where(user.personalInfo.firstName.eq(firstName))
                .fetch();


//        var cb = session.getCriteriaBuilder();
//
//        var criteria = cb.createQuery(User.class);
//        var user = criteria.from(User.class);
//
//        criteria.select(user).where(
//                cb.equal(user.get(User_.PERSONAL_INFO).get(PersonalInfo_.FIRST_NAME), firstName)
//        );
//
//        return session.createQuery(criteria)
//                .list();
    }

    /**
     * Возвращает первые {limit} сотрудников упорядоченных по имени
     */
    public List<User> findLimitedOrderedByFirstName(Session session, int limit) {
//        return session.createQuery("select u from User u order by u.personalInfo.firstName", User.class)
//                .setMaxResults(limit)
//                .list();

        return new JPAQuery<User>(session)
                .select(user)
                .from(user)
                .orderBy(user.personalInfo.firstName.asc())
                .limit(limit)
                .fetch();


//        var cb = session.getCriteriaBuilder();
//
//        var criteria = cb.createQuery(User.class);
//        var user = criteria.from(User.class);
//
//        criteria.select(user).orderBy(
//                cb.asc(user.get(User_.PERSONAL_INFO).get(PersonalInfo_.FIRST_NAME))
//        );
//
//        return session.createQuery(criteria)
//                .setMaxResults(limit)
//                .list();
    }

    /**
     * Возвращает всех сотрудников компании с указанным названием
     */
    public List<User> findAllByCompanyName(Session session, String companyName) {
//        return session.createQuery(
//                        "select u " +
//                                "from User u " +
//                                "join u.company c " +
//                                "where c.name = :companyName", User.class)
//                .setParameter("companyName", companyName)
//                .list();

        return new JPAQuery<User>(session)
                .select(user)
                .from(user)
                .join(user.company, company)
                .where(company.name.eq(companyName))
                .fetch();



//        var cb = session.getCriteriaBuilder();
//
//        var criteria = cb.createQuery(User.class);
//        var user = criteria.from(User.class);
//        var company = user.join(User_.company);
//
//        criteria.select(user).where(
//                cb.equal(company.get(Company_.name), companyName)
//        );
//
//        return session.createQuery(criteria)
//                .list();
    }

    /**
     * Возвращает все выплаты полученные сотрудниками компании с указаным именем
     * упорядоченные по имени сотрудника, а затем по размеру выплат
     */
    public List<Payment> findAllPaymentsByCompanyName(Session session, String companyName) {
//        return session.createQuery(
//                        "select p " +
//                                "from Payment p " +
//                                "join p.receiver u " +
//                                "join u.company c " +
//                                "where c.name = :companyName " +
//                                "order by u.personalInfo.firstName, p.amount", Payment.class)
//                .setParameter("companyName", companyName)
//                .list();

        return new JPAQuery<Payment>(session)
                .select(payment)
                .from(payment)
                .join(payment.receiver, user).fetchJoin()
                .join(user.company, company)
                .where(company.name.eq(companyName))
                .orderBy(user.personalInfo.firstName.asc(), payment.amount.asc())
                .fetch();


//        var cb = session.getCriteriaBuilder();
//
//        var criteria = cb.createQuery(Payment.class);
//        var payment = criteria.from(Payment.class);
//        var user = payment.join(Payment_.receiver);
//        payment.fetch(Payment_.receiver);
//        var company = user.join(User_.company);
//
//        criteria.select(payment).where(
//                cb.equal(company.get(Company_.name), companyName)
//        )
//                .orderBy(
//                        cb.asc(user.get(User_.personalInfo).get(PersonalInfo_.firstName)),
//                        cb.asc(payment.get(Payment_.amount))
//                );
//
//        return session.createQuery(criteria)
//                .list();
    }

    /**
     * Возвращает среднюю зарплату сотрудника с указанным именем и фамилией
     */
    public Double findAveragePaymentAmountByFirstAndLastNames(Session session, UserFilter filter) {
//        return session.createQuery(
//                        """
//                        select avg(p.amount)
//                        from Payment p
//                        join p.receiver u
//                        where u.personalInfo.firstName = :firstName and u.personalInfo.lastName = :lastName
//                        """, Double.class)
//                .setParameter("firstName", firstName)
//                .setParameter("lastName", lastName)
//                .uniqueResult();

        var predicate = QPredicate.build()
                .add(filter.getFirstName(), user.personalInfo.firstName::eq)
                .add(filter.getLastName(), user.personalInfo.lastName::eq)
                .buildAnd();

        return new JPAQuery<Double>(session)
                .select(payment.amount.avg())
                .from(payment)
                .join(payment.receiver, user)
                .where(predicate)
                .fetchOne();


//        var cb = session.getCriteriaBuilder();
//
//        var criteria = cb.createQuery(Double.class);
//        var payment = criteria.from(Payment.class);
//        var user = payment.join(Payment_.receiver);
//
//        List<Predicate> predicates = new ArrayList<>();
//        if (firstName != null) {
//            predicates.add(cb.equal(user.get(User_.personalInfo).get(PersonalInfo_.firstName), firstName));
//        }
//        if (lastName != null) {
//            predicates.add(cb.equal(user.get(User_.personalInfo).get(PersonalInfo_.lastName), lastName));
//        }
//
//        criteria.select(cb.avg(payment.get(Payment_.amount))).where(
//                predicates.toArray(Predicate[]::new)
//        );
//
//        return session.createQuery(criteria)
//                .uniqueResult();
    }

    /**
     * Возвращает для каждой компании: название, среднюю зарплату всех её сотрудников
     */
    public List<Tuple> findCompanyNamesWithAvgUserPaymentsOrderedByCompanyName(Session session) {
//        return session.createQuery(
//                     """
//                     select new org.example.hibernate.dto.CompanyDto(c.name, avg(p.amount))
//                     from Payment p
//                     join p.receiver u
//                     join u.company c
//                     group by c.name
//                     """, CompanyDto.class)
//                .list();

        return new JPAQuery<Tuple>(session)
                .select(company.name, payment.amount.avg())
                .from(payment)
                .join(payment.receiver, user)
                .join(user.company, company)
                .groupBy(company.name)
                .fetch();


//        var cb = session.getCriteriaBuilder();
//
//        var criteria = cb.createQuery(CompanyDto.class);
//        var payment = criteria.from(Payment.class);
//        var user = payment.join(Payment_.receiver);
//        var company = user.join(User_.company);
//
//        criteria.select(
//                cb.construct(CompanyDto.class,
//                        Arrays.asList(company.get(Company_.name), cb.avg(payment.get(Payment_.amount))))
//        ).groupBy(
//                company.get(Company_.name)
//        );
//
//        return session.createQuery(criteria)
//                .list();
    }

    /**
     * Возвращает список: сотрудник, средний размер выплат.
     * В выборку попадают только сотрудники зарплата которых выше средней зарплаты всех сотрудников
     */
    public List<Tuple> isItPossible(Session session) {
//        return session.createQuery(
//                    """
//                    select u, avg(p.amount)
//                    from User u
//                    join u.payments p
//                    group by u.id
//                    having avg(p.amount) > (select avg(p.amount) from Payment p)
//                    """, Tuple.class)
//                .list();

        return new JPAQuery<Tuple>(session)
                .select(user, payment.amount.avg())
                .from(user)
                .join(user.payments, payment)
                .groupBy(user.id)
                .having(
                        payment.amount.avg().gt(
                                new JPAQuery<Double>(session)
                                        .select(payment.amount.avg())
                                        .from(payment)
                                        .fetchOne()
                        ))
                .fetch();


//        var cb = session.getCriteriaBuilder();
//
//        var criteria = cb.createQuery(Tuple.class);
//        var user = criteria.from(User.class);
//        var payment = user.join(User_.payments);
//
//        var subquery = criteria.subquery(Double.class);
//        var subqueryPayment = subquery.from(Payment.class);
//
//        criteria.select(
//                cb.tuple(Arrays.asList(user, cb.avg(payment.get(Payment_.amount))))
//        )
//                .groupBy(user.get(User_.id))
//                .having(
//                        cb.greaterThan(
//                                cb.avg(payment.get(Payment_.amount)),
//                                subquery.select(cb.avg(subqueryPayment.get(Payment_.amount)))
//                        )
//                );
//
//        return session.createQuery(criteria)
//                .list();
    }

    public static UserDao getInstance() {
        return INSTANCE;
    }
}

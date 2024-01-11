package org.example.hibernate;

import org.example.hibernate.entity.User;
import org.example.hibernate.entity.UserChat;
import org.example.hibernate.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.jpa.AvailableHints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class HibernateRunner {
    private static final Logger log = LoggerFactory.getLogger(HibernateRunner.class);

    public static void main(String[] args) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();
//            session.enableFetchProfile("withCompanyAndPayments");

            var graph = session.createEntityGraph(User.class);
            graph.addAttributeNodes("company", "userChats");
            var chatSubgraph = graph.addSubgraph("userChats", UserChat.class);
            chatSubgraph.addAttributeNodes("chat");

            Map<String, Object> params = new HashMap<>();
//            params.put(AvailableHints.HINT_SPEC_LOAD_GRAPH, session.getEntityGraph("withCompanyAndChats"));
            params.put(AvailableHints.HINT_SPEC_FETCH_GRAPH, graph);

            var user = session.find(User.class, 1L, params);
            System.out.println(user.getUserChats().size());
            System.out.println(user.getCompany().getName());

            var users =
                    session.createQuery("select u from User u join fetch u.company", User.class)
//                            .setHint(AvailableHints.HINT_SPEC_LOAD_GRAPH,
//                                    session.getEntityGraph("withCompanyAndChats"))
                            .setHint(AvailableHints.HINT_SPEC_FETCH_GRAPH, graph)
                            .list();
            users.forEach(it -> System.out.println(it.getUserChats().size()));
            users.forEach(it -> System.out.println(it.getCompany().getName()));

            session.getTransaction().commit();
        }
    }
}

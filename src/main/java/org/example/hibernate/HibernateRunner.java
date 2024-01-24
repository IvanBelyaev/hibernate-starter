package org.example.hibernate;

import jakarta.transaction.Transactional;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import org.example.hibernate.dao.CompanyRepository;
import org.example.hibernate.dao.UserRepository;
import org.example.hibernate.dto.CreateUserDto;
import org.example.hibernate.entity.Birthday;
import org.example.hibernate.entity.PersonalInfo;
import org.example.hibernate.interceptor.TransactionInterceptor;
import org.example.hibernate.mapper.CompanyReadMapper;
import org.example.hibernate.mapper.CreateUserMapper;
import org.example.hibernate.mapper.UserReadMapper;
import org.example.hibernate.service.UserService;
import org.example.hibernate.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.time.LocalDate;

public class HibernateRunner {
    private static final Logger log = LoggerFactory.getLogger(HibernateRunner.class);

    @Transactional
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            var session = (Session) Proxy.newProxyInstance(SessionFactory.class.getClassLoader(), new Class[] { Session.class },
                    (proxy, method, args1) -> method.invoke(sessionFactory.getCurrentSession(), args1));

            var userRepository = new UserRepository(session);
            var companyRepository = new CompanyRepository(session);

            var companyReadMapper = new CompanyReadMapper();
            var userReadMapper = new UserReadMapper(companyReadMapper);
            var createUserMapper = new CreateUserMapper(companyRepository);
            var transactionInterceptor = new TransactionInterceptor(sessionFactory);

            UserService userService = new ByteBuddy()
                    .subclass(UserService.class)
                    .method(ElementMatchers.any())
                    .intercept(MethodDelegation.to(transactionInterceptor))
                    .make()
                    .load(UserService.class.getClassLoader())
                    .getLoaded()
                    .getDeclaredConstructor(UserRepository.class, UserReadMapper.class, CreateUserMapper.class)
                    .newInstance(userRepository, userReadMapper, createUserMapper);

            var userReadDto = userService.findById(1L);
            userReadDto.ifPresent(System.out::println);

            var createUserDto = new CreateUserDto(
                    PersonalInfo.builder()
                            .firstName("Oleg")
                            .lastName("Smirnov")
                            .birthDate(new Birthday(LocalDate.now()))
                            .build(),
                    "oleg6@gmail.com",
                    "{}",
                    1
            );
            userService.create(createUserDto);
        }
    }
}

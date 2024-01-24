package org.example.hibernate.service;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import lombok.RequiredArgsConstructor;
import org.example.hibernate.dao.UserRepository;
import org.example.hibernate.dto.CreateUserDto;
import org.example.hibernate.dto.UserReadDto;
import org.example.hibernate.entity.User;
import org.example.hibernate.mapper.CreateUserMapper;
import org.example.hibernate.mapper.Mapper;
import org.example.hibernate.mapper.UserReadMapper;
import org.example.hibernate.validation.CreateUser;
import org.hibernate.jpa.AvailableHints;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserReadMapper userReadMapper;
    private final CreateUserMapper createUserMapper;

    @Transactional
    public boolean delete(Long id) {
        var maybeUser = userRepository.findById(id);
        maybeUser.ifPresent(userRepository::delete);
        return maybeUser.isPresent();
    }

    @Transactional
    public <T> Optional<T> findById(Long id, Mapper<User, T> mapper) {
        Map<String, Object> properties =
                Map.of(AvailableHints.HINT_SPEC_LOAD_GRAPH,
                        userRepository.getEntityManager().getEntityGraph("withCompany"));
        return userRepository.findById(id, properties)
                .map(mapper::mapFrom);
    }

    @Transactional
    public Optional<UserReadDto> findById(Long id) {
        return findById(id, userReadMapper);
    }

    @Transactional
    public Long create(CreateUserDto createUserDto) {
        var validatorFactory = Validation.buildDefaultValidatorFactory();
        var validator = validatorFactory.getValidator();
        var constraintViolations = validator.validate(createUserDto, CreateUser.class);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }

        User user = createUserMapper.mapFrom(createUserDto);
        return userRepository.save(user).getId();
    }
}

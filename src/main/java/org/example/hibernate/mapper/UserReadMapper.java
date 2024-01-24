package org.example.hibernate.mapper;

import lombok.RequiredArgsConstructor;
import org.example.hibernate.dto.UserReadDto;
import org.example.hibernate.entity.User;

@RequiredArgsConstructor
public class UserReadMapper implements Mapper<User, UserReadDto> {

    private final CompanyReadMapper companyReadMapper;

    @Override
    public UserReadDto mapFrom(User user) {
        return new UserReadDto(
                user.getId(),
                user.getPersonalInfo(),
                user.getUsername(),
                user.getInfo(),
                companyReadMapper.mapFrom(user.getCompany()));
    }
}

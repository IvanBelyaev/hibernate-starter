package org.example.hibernate.mapper;

import lombok.RequiredArgsConstructor;
import org.example.hibernate.dao.CompanyRepository;
import org.example.hibernate.dto.CreateUserDto;
import org.example.hibernate.entity.User;

@RequiredArgsConstructor
public class CreateUserMapper implements Mapper<CreateUserDto, User> {

    private final CompanyRepository companyRepository;

    @Override
    public User mapFrom(CreateUserDto userDto) {
        return User.builder()
                .username(userDto.username())
                .personalInfo(userDto.personalInfo())
                .info(userDto.info())
                .company(companyRepository.findById(userDto.companyId()).orElseThrow(IllegalCallerException::new))
                .build();
    }
}

package com.tasktracker.apiservice.mapper;

import com.tasktracker.apiservice.dto.request.UserCreateEditDto;
import com.tasktracker.apiservice.dto.response.UserReadDto;
import com.tasktracker.apiservice.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserReadDto toDto(User user);

    @Mapping(target = "id", ignore = true)
    User toEntity(UserCreateEditDto dto);

}

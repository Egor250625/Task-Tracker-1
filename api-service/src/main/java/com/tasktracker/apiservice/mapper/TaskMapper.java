package com.tasktracker.apiservice.mapper;

import com.tasktracker.apiservice.dto.request.TaskCreateRequestDto;
import com.tasktracker.apiservice.dto.response.TaskResponseDto;
import com.tasktracker.apiservice.model.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    TaskResponseDto toDto(Task task);

    @Mapping(target = "id", ignore = true)
    Task toEntity(TaskCreateRequestDto dto);
}

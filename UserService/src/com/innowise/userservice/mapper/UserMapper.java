package com.innowise.userservice.mapper;

import com.innowise.userservice.entity.User;
import com.innowise.userservice.dto.response.UserResponse;
import com.innowise.userservice.dto.request.CreateUserRequest;
import com.innowise.userservice.dto.request.UpdateUserRequest;

import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        uses = {PaymentCardMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserMapper {

  User toEntity(CreateUserRequest request);

  UserResponse toResponse(User user);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateEntityFromRequest(UpdateUserRequest request, @MappingTarget User user);
}

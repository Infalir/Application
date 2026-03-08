package com.innowise.userservice.service;

import com.innowise.userservice.dto.request.CreateUserRequest;
import com.innowise.userservice.dto.request.UpdateUserRequest;
import com.innowise.userservice.dto.request.UserFilterRequest;
import com.innowise.userservice.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
  UserResponse createUser(CreateUserRequest request);
  UserResponse getUserById(Long id);
  Page<UserResponse> getAllUsers(UserFilterRequest filter, Pageable pageable);
  UserResponse updateUser(Long id, UpdateUserRequest request);
  void activateUser(Long id);
  void deactivateUser(Long id);
  void deleteUser(Long id);
}

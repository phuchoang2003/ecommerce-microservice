package com.hdp.customer_service.infrastructure.adapter.inbound.web.controller;

import com.hdp.common.web.dto.response.ApiResponse;
import com.hdp.customer_service.application.port.in.createuser.CreateUserCommandHandler;
import com.hdp.customer_service.application.port.in.createuser.CreateUserResult;
import com.hdp.customer_service.application.port.in.deleteuser.DeleteUserCommand;
import com.hdp.customer_service.application.port.in.deleteuser.DeleteUserCommandHandler;
import com.hdp.customer_service.application.port.in.getuser.GetUserByIdQuery;
import com.hdp.customer_service.application.port.in.getuser.GetUserByIdQueryHandler;
import com.hdp.customer_service.application.port.in.getuser.GetUserByIdResult;
import com.hdp.customer_service.application.port.in.updateuser.UpdateUserCommandHandler;
import com.hdp.customer_service.application.port.in.updateuser.UpdateUserResult;
import com.hdp.customer_service.infrastructure.adapter.inbound.web.dto.request.CreateUserRequest;
import com.hdp.customer_service.infrastructure.adapter.inbound.web.dto.response.UserResponse;
import com.hdp.customer_service.infrastructure.adapter.inbound.web.mapper.UserWebMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final CreateUserCommandHandler createUserCommandHandler;
    private final UpdateUserCommandHandler updateUserCommandHandler;
    private final DeleteUserCommandHandler deleteUserCommandHandler;
    private final GetUserByIdQueryHandler getUserByIdQueryHandler;

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(
            @Valid @RequestBody CreateUserRequest request) {
        CreateUserResult result = createUserCommandHandler.handle(UserWebMapper.toCreateUserCommand(request));
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(UserWebMapper.toResponse(result), "User created successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUser(@PathVariable UUID id) {
        GetUserByIdResult result = getUserByIdQueryHandler.handle(new GetUserByIdQuery(id));
        return ResponseEntity.ok(ApiResponse.success(UserWebMapper.toResponse(result), "User retrieved successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable UUID id,
            @Valid @RequestBody CreateUserRequest request) {
        UpdateUserResult result = updateUserCommandHandler.handle(UserWebMapper.toUpdateUserCommand(id, request));
        return ResponseEntity.ok(ApiResponse.success(UserWebMapper.toResponse(result), "User updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable UUID id) {
        deleteUserCommandHandler.handle(new DeleteUserCommand(id));
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .body(ApiResponse.success(null, "User deleted successfully"));
    }
}

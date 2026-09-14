package com.hdp.customer_service.application.handler.createuser;

import com.hdp.core.exception.DuplicateKeyBusinessException;
import com.hdp.customer_service.application.port.in.createuser.CreateUserCommand;
import com.hdp.customer_service.application.port.in.createuser.CreateUserCommandHandler;
import com.hdp.customer_service.application.port.in.createuser.CreateUserResult;
import com.hdp.customer_service.application.port.out.UserPersistencePort;
import com.hdp.customer_service.domain.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateUserCommandHandlerImpl implements CreateUserCommandHandler {

    private final UserPersistencePort userPersistence;

    @Override
    @Transactional
    public CreateUserResult handle(CreateUserCommand command) {
        if (userPersistence.existsByEmail(command.email())) {
            throw new DuplicateKeyBusinessException("User", command.email());
        }

        User user = User.create(
            command.fullName(),
            command.email(),
            command.phone(),
            command.dateOfBirth(),
            command.gender(),
            command.avatarUrl(),
            command.addresses()
        );

        User saved = userPersistence.save(user);
        log.info("User created: userId={}, email={}", saved.getId().value(), saved.getEmail());

        return toResult(saved);
    }

    private CreateUserResult toResult(User user) {
        return new CreateUserResult(
            user.getId().value(),
            user.getFullName(),
            user.getEmail(),
            user.getPhone(),
            user.getDateOfBirth(),
            user.getGender(),
            user.getAvatarUrl(),
            user.getAddresses()
        );
    }
}

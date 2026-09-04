package com.hdp.customer_service.application.handler.updateuser;

import com.hdp.core.exception.DuplicateKeyBusinessException;
import com.hdp.core.exception.NotFoundException;
import com.hdp.customer_service.application.port.in.updateuser.UpdateUserCommand;
import com.hdp.customer_service.application.port.in.updateuser.UpdateUserCommandHandler;
import com.hdp.customer_service.application.port.in.updateuser.UpdateUserResult;
import com.hdp.customer_service.application.port.out.UserPersistencePort;
import com.hdp.customer_service.domain.model.User;
import com.hdp.customer_service.domain.valueobject.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateUserCommandHandlerImpl implements UpdateUserCommandHandler {

    private final UserPersistencePort userPersistence;

    @Override
    @Transactional
    public UpdateUserResult handle(UpdateUserCommand command) {
        User user = userPersistence.getById(UserId.of(command.id()));
        if (user == null) {
            throw new NotFoundException("User", command.id());
        }

        if (!user.getEmail().equalsIgnoreCase(command.email())
                && userPersistence.existsByEmail(command.email())) {
            throw new DuplicateKeyBusinessException("User", command.email());
        }

        user.update(
            command.fullName(),
            command.email(),
            command.phone(),
            command.dateOfBirth(),
            command.gender(),
            command.avatarUrl(),
            command.addresses()
        );

        User saved = userPersistence.save(user);
        log.info("User updated: userId={}", saved.getId().value());

        return toResult(saved);
    }

    private UpdateUserResult toResult(User user) {
        return new UpdateUserResult(
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

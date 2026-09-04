package com.hdp.customer_service.application.handler.deleteuser;

import com.hdp.core.exception.NotFoundException;
import com.hdp.customer_service.application.port.in.deleteuser.DeleteUserCommand;
import com.hdp.customer_service.application.port.in.deleteuser.DeleteUserCommandHandler;
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
public class DeleteUserCommandHandlerImpl implements DeleteUserCommandHandler {

    private final UserPersistencePort userPersistence;

    @Override
    @Transactional
    public Void handle(DeleteUserCommand command) {
        User user = userPersistence.getById(UserId.of(command.id()));
        if (user == null) {
            throw new NotFoundException("User", command.id());
        }

        userPersistence.save(user);
        log.info("User soft-deleted: userId={}", command.id());

        return null;
    }
}

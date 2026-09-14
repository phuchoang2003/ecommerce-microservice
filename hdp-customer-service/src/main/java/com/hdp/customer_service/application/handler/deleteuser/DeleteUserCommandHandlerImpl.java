package com.hdp.customer_service.application.handler.deleteuser;

import com.hdp.core.exception.NotFoundException;
import com.hdp.customer_service.application.port.in.deleteuser.DeleteUserCommand;
import com.hdp.customer_service.application.port.in.deleteuser.DeleteUserCommandHandler;
import com.hdp.customer_service.application.port.out.UserPersistencePort;
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
        boolean exists = userPersistence.findByIdAndNotDeleted(command.id()).isPresent();
        if (!exists) {
            throw new NotFoundException("User", command.id());
        }

        userPersistence.softDelete(command.id());
        log.info("User soft-deleted: userId={}", command.id());

        return null;
    }
}
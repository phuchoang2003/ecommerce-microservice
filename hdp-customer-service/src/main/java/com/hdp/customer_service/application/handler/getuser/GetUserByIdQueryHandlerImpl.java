package com.hdp.customer_service.application.handler.getuser;

import com.hdp.core.exception.NotFoundException;
import com.hdp.customer_service.application.port.in.getuser.GetUserByIdQuery;
import com.hdp.customer_service.application.port.in.getuser.GetUserByIdQueryHandler;
import com.hdp.customer_service.application.port.in.getuser.GetUserByIdResult;
import com.hdp.customer_service.application.port.out.UserPersistencePort;
import com.hdp.customer_service.domain.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetUserByIdQueryHandlerImpl implements GetUserByIdQueryHandler {

    private final UserPersistencePort userPersistence;

    @Override
    @Transactional(readOnly = true)
    public GetUserByIdResult handle(GetUserByIdQuery query) {
        User user = userPersistence.findByIdAndNotDeleted(query.id())
            .orElseThrow(() -> new NotFoundException("User", query.id()));
        log.debug("User fetched: userId={}", query.id());
        return toResult(user);
    }

    private GetUserByIdResult toResult(User user) {
        return new GetUserByIdResult(
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

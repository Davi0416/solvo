package io.github.solvo.application.ports.out;

import io.github.solvo.domain.entities.Transfer;

public interface AuthorizationServicePort {
    Boolean authorize(Transfer transferRequest);
}

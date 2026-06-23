package io.github.solvo.infrastructure.persistence.mappers;

import io.github.solvo.domain.entities.Transfer;
import io.github.solvo.infrastructure.persistence.entities.TransferJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransferMapper {
    TransferJpaEntity toJpa(Transfer transfer);

    Transfer toDomain(TransferJpaEntity transferJpaEntity);
}

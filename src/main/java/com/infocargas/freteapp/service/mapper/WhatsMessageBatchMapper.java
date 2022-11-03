package com.infocargas.freteapp.service.mapper;

import com.infocargas.freteapp.domain.WhatsMessageBatch;
import com.infocargas.freteapp.service.dto.WhatsMessageBatchDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WhatsMessageBatch} and its DTO {@link WhatsMessageBatchDTO}.
 */
@Mapper(componentModel = "spring")
public interface WhatsMessageBatchMapper extends EntityMapper<WhatsMessageBatchDTO, WhatsMessageBatch> {}

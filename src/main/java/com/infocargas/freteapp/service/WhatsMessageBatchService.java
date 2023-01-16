package com.infocargas.freteapp.service;

import com.infocargas.freteapp.domain.WhatsMessageBatch;
import com.infocargas.freteapp.domain.enumeration.WhatsStatus;
import com.infocargas.freteapp.repository.WhatsMessageBatchRepository;
import com.infocargas.freteapp.service.dto.WhatsMessageBatchDTO;
import com.infocargas.freteapp.service.mapper.WhatsMessageBatchMapper;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link WhatsMessageBatch}.
 */
@Service
@Transactional
public class WhatsMessageBatchService {

    private final Logger log = LoggerFactory.getLogger(WhatsMessageBatchService.class);

    private final WhatsMessageBatchRepository whatsMessageBatchRepository;

    private final WhatsMessageBatchMapper whatsMessageBatchMapper;

    public WhatsMessageBatchService(
        WhatsMessageBatchRepository whatsMessageBatchRepository,
        WhatsMessageBatchMapper whatsMessageBatchMapper
    ) {
        this.whatsMessageBatchRepository = whatsMessageBatchRepository;
        this.whatsMessageBatchMapper = whatsMessageBatchMapper;
    }

    /**
     * Save a whatsMessageBatch.
     *
     * @param whatsMessageBatchDTO the entity to save.
     * @return the persisted entity.
     */
    public WhatsMessageBatchDTO save(WhatsMessageBatchDTO whatsMessageBatchDTO) {
        log.debug("Request to save WhatsMessageBatch : {}", whatsMessageBatchDTO);
        WhatsMessageBatch whatsMessageBatch = whatsMessageBatchMapper.toEntity(whatsMessageBatchDTO);
        whatsMessageBatch = whatsMessageBatchRepository.save(whatsMessageBatch);
        return whatsMessageBatchMapper.toDto(whatsMessageBatch);
    }

    /**
     * Update a whatsMessageBatch.
     *
     * @param whatsMessageBatchDTO the entity to save.
     * @return the persisted entity.
     */
    public WhatsMessageBatchDTO update(WhatsMessageBatchDTO whatsMessageBatchDTO) {
        log.debug("Request to save WhatsMessageBatch : {}", whatsMessageBatchDTO);
        WhatsMessageBatch whatsMessageBatch = whatsMessageBatchMapper.toEntity(whatsMessageBatchDTO);
        whatsMessageBatch = whatsMessageBatchRepository.save(whatsMessageBatch);
        return whatsMessageBatchMapper.toDto(whatsMessageBatch);
    }

    /**
     * Partially update a whatsMessageBatch.
     *
     * @param whatsMessageBatchDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<WhatsMessageBatchDTO> partialUpdate(WhatsMessageBatchDTO whatsMessageBatchDTO) {
        log.debug("Request to partially update WhatsMessageBatch : {}", whatsMessageBatchDTO);

        return whatsMessageBatchRepository
            .findById(whatsMessageBatchDTO.getId())
            .map(existingWhatsMessageBatch -> {
                whatsMessageBatchMapper.partialUpdate(existingWhatsMessageBatch, whatsMessageBatchDTO);

                return existingWhatsMessageBatch;
            })
            .map(whatsMessageBatchRepository::save)
            .map(whatsMessageBatchMapper::toDto);
    }

    /**
     * Get all the whatsMessageBatches.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<WhatsMessageBatchDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WhatsMessageBatches");
        return whatsMessageBatchRepository.findAll(pageable).map(whatsMessageBatchMapper::toDto);
    }

    /**
     * Get one whatsMessageBatch by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<WhatsMessageBatchDTO> findOne(Long id) {
        log.debug("Request to get WhatsMessageBatch : {}", id);
        return whatsMessageBatchRepository.findById(id).map(whatsMessageBatchMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<WhatsMessageBatchDTO> findByMessageId(String waIdTo) {
        log.debug("Request to get WhatsMessageBatch : {}", waIdTo);
        return whatsMessageBatchRepository.findByWaidToAndStatus(waIdTo, WhatsStatus.OPEN).map(whatsMessageBatchMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<WhatsMessageBatchDTO> findByMessageIdWithoutStatus(String waIdTo) {
        log.debug("Request to get WhatsMessageBatch : {}", waIdTo);
        return whatsMessageBatchMapper.toDto(whatsMessageBatchRepository.findByWaidTo(waIdTo));
    }

    @Transactional(readOnly = true)
    public List<WhatsMessageBatchDTO> findByNearRouteStatus(Integer perfilId, WhatsStatus status) {
        var lista = whatsMessageBatchRepository.findByPerfilIDAndStatus(perfilId, status);
        return whatsMessageBatchMapper.toDto(lista);
    }

    @Transactional(readOnly = true)
    public List<WhatsMessageBatchDTO> findByNearRouteStatusByOferta(Long ofertaId, WhatsStatus status) {
        var lista = whatsMessageBatchRepository.findByOfertaIdAndStatus(ofertaId, status);
        return whatsMessageBatchMapper.toDto(lista);
    }

    @Transactional(readOnly = true)
    public List<WhatsMessageBatchDTO> findByRouteStatus(WhatsStatus status) {
        var lista = whatsMessageBatchRepository.findWith30Minutes(status.name(), 30);
        return whatsMessageBatchMapper.toDto(lista);
    }

    /**
     * Delete the whatsMessageBatch by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete WhatsMessageBatch : {}", id);
        whatsMessageBatchRepository.deleteById(id);
    }
}

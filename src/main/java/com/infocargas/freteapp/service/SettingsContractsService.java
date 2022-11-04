package com.infocargas.freteapp.service;

import com.infocargas.freteapp.domain.SettingsContracts;
import com.infocargas.freteapp.repository.SettingsContractsRepository;
import com.infocargas.freteapp.service.dto.SettingsContractsDTO;
import com.infocargas.freteapp.service.mapper.SettingsContractsMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SettingsContracts}.
 */
@Service
@Transactional
public class SettingsContractsService {

    private final Logger log = LoggerFactory.getLogger(SettingsContractsService.class);

    private final SettingsContractsRepository settingsContractsRepository;

    private final SettingsContractsMapper settingsContractsMapper;

    public SettingsContractsService(
        SettingsContractsRepository settingsContractsRepository,
        SettingsContractsMapper settingsContractsMapper
    ) {
        this.settingsContractsRepository = settingsContractsRepository;
        this.settingsContractsMapper = settingsContractsMapper;
    }

    /**
     * Save a settingsContracts.
     *
     * @param settingsContractsDTO the entity to save.
     * @return the persisted entity.
     */
    public SettingsContractsDTO save(SettingsContractsDTO settingsContractsDTO) {
        log.debug("Request to save SettingsContracts : {}", settingsContractsDTO);
        SettingsContracts settingsContracts = settingsContractsMapper.toEntity(settingsContractsDTO);
        settingsContracts = settingsContractsRepository.save(settingsContracts);
        return settingsContractsMapper.toDto(settingsContracts);
    }

    /**
     * Update a settingsContracts.
     *
     * @param settingsContractsDTO the entity to save.
     * @return the persisted entity.
     */
    public SettingsContractsDTO update(SettingsContractsDTO settingsContractsDTO) {
        log.debug("Request to save SettingsContracts : {}", settingsContractsDTO);
        SettingsContracts settingsContracts = settingsContractsMapper.toEntity(settingsContractsDTO);
        settingsContracts = settingsContractsRepository.save(settingsContracts);
        return settingsContractsMapper.toDto(settingsContracts);
    }

    /**
     * Partially update a settingsContracts.
     *
     * @param settingsContractsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SettingsContractsDTO> partialUpdate(SettingsContractsDTO settingsContractsDTO) {
        log.debug("Request to partially update SettingsContracts : {}", settingsContractsDTO);

        return settingsContractsRepository
            .findById(settingsContractsDTO.getId())
            .map(existingSettingsContracts -> {
                settingsContractsMapper.partialUpdate(existingSettingsContracts, settingsContractsDTO);

                return existingSettingsContracts;
            })
            .map(settingsContractsRepository::save)
            .map(settingsContractsMapper::toDto);
    }

    /**
     * Get all the settingsContracts.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SettingsContractsDTO> findAll() {
        log.debug("Request to get all SettingsContracts");
        return settingsContractsRepository
            .findAll()
            .stream()
            .map(settingsContractsMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one settingsContracts by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SettingsContractsDTO> findOne(Long id) {
        log.debug("Request to get SettingsContracts : {}", id);
        return settingsContractsRepository.findById(id).map(settingsContractsMapper::toDto);
    }

    /**
     * Delete the settingsContracts by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SettingsContracts : {}", id);
        settingsContractsRepository.deleteById(id);
    }
}

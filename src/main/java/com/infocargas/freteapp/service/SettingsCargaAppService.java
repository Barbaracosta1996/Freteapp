package com.infocargas.freteapp.service;

import com.infocargas.freteapp.domain.SettingsCargaApp;
import com.infocargas.freteapp.repository.SettingsCargaAppRepository;
import com.infocargas.freteapp.service.dto.SettingsCargaAppDTO;
import com.infocargas.freteapp.service.mapper.SettingsCargaAppMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SettingsCargaApp}.
 */
@Service
@Transactional
public class SettingsCargaAppService {

    private final Logger log = LoggerFactory.getLogger(SettingsCargaAppService.class);

    private final SettingsCargaAppRepository settingsCargaAppRepository;

    private final SettingsCargaAppMapper settingsCargaAppMapper;

    public SettingsCargaAppService(SettingsCargaAppRepository settingsCargaAppRepository, SettingsCargaAppMapper settingsCargaAppMapper) {
        this.settingsCargaAppRepository = settingsCargaAppRepository;
        this.settingsCargaAppMapper = settingsCargaAppMapper;
    }

    /**
     * Save a settingsCargaApp.
     *
     * @param settingsCargaAppDTO the entity to save.
     * @return the persisted entity.
     */
    public SettingsCargaAppDTO save(SettingsCargaAppDTO settingsCargaAppDTO) {
        log.debug("Request to save SettingsCargaApp : {}", settingsCargaAppDTO);
        SettingsCargaApp settingsCargaApp = settingsCargaAppMapper.toEntity(settingsCargaAppDTO);
        settingsCargaApp = settingsCargaAppRepository.save(settingsCargaApp);
        return settingsCargaAppMapper.toDto(settingsCargaApp);
    }

    /**
     * Update a settingsCargaApp.
     *
     * @param settingsCargaAppDTO the entity to save.
     * @return the persisted entity.
     */
    public SettingsCargaAppDTO update(SettingsCargaAppDTO settingsCargaAppDTO) {
        log.debug("Request to save SettingsCargaApp : {}", settingsCargaAppDTO);
        SettingsCargaApp settingsCargaApp = settingsCargaAppMapper.toEntity(settingsCargaAppDTO);
        settingsCargaApp = settingsCargaAppRepository.save(settingsCargaApp);
        return settingsCargaAppMapper.toDto(settingsCargaApp);
    }

    /**
     * Partially update a settingsCargaApp.
     *
     * @param settingsCargaAppDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SettingsCargaAppDTO> partialUpdate(SettingsCargaAppDTO settingsCargaAppDTO) {
        log.debug("Request to partially update SettingsCargaApp : {}", settingsCargaAppDTO);

        return settingsCargaAppRepository
            .findById(settingsCargaAppDTO.getId())
            .map(existingSettingsCargaApp -> {
                settingsCargaAppMapper.partialUpdate(existingSettingsCargaApp, settingsCargaAppDTO);

                return existingSettingsCargaApp;
            })
            .map(settingsCargaAppRepository::save)
            .map(settingsCargaAppMapper::toDto);
    }

    /**
     * Get all the settingsCargaApps.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SettingsCargaAppDTO> findAll() {
        log.debug("Request to get all SettingsCargaApps");
        return settingsCargaAppRepository
            .findAll()
            .stream()
            .map(settingsCargaAppMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one settingsCargaApp by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SettingsCargaAppDTO> findOne(Long id) {
        log.debug("Request to get SettingsCargaApp : {}", id);
        return settingsCargaAppRepository.findById(id).map(settingsCargaAppMapper::toDto);
    }

    /**
     * Delete the settingsCargaApp by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SettingsCargaApp : {}", id);
        settingsCargaAppRepository.deleteById(id);
    }
}

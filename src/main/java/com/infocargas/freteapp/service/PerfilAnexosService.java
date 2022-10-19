package com.infocargas.freteapp.service;

import com.infocargas.freteapp.domain.PerfilAnexos;
import com.infocargas.freteapp.repository.PerfilAnexosRepository;
import com.infocargas.freteapp.service.dto.PerfilAnexosDTO;
import com.infocargas.freteapp.service.mapper.PerfilAnexosMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PerfilAnexos}.
 */
@Service
@Transactional
public class PerfilAnexosService {

    private final Logger log = LoggerFactory.getLogger(PerfilAnexosService.class);

    private final PerfilAnexosRepository perfilAnexosRepository;

    private final PerfilAnexosMapper perfilAnexosMapper;

    public PerfilAnexosService(PerfilAnexosRepository perfilAnexosRepository, PerfilAnexosMapper perfilAnexosMapper) {
        this.perfilAnexosRepository = perfilAnexosRepository;
        this.perfilAnexosMapper = perfilAnexosMapper;
    }

    /**
     * Save a perfilAnexos.
     *
     * @param perfilAnexosDTO the entity to save.
     * @return the persisted entity.
     */
    public PerfilAnexosDTO save(PerfilAnexosDTO perfilAnexosDTO) {
        log.debug("Request to save PerfilAnexos : {}", perfilAnexosDTO);
        PerfilAnexos perfilAnexos = perfilAnexosMapper.toEntity(perfilAnexosDTO);
        perfilAnexos = perfilAnexosRepository.save(perfilAnexos);
        return perfilAnexosMapper.toDto(perfilAnexos);
    }

    /**
     * Update a perfilAnexos.
     *
     * @param perfilAnexosDTO the entity to save.
     * @return the persisted entity.
     */
    public PerfilAnexosDTO update(PerfilAnexosDTO perfilAnexosDTO) {
        log.debug("Request to save PerfilAnexos : {}", perfilAnexosDTO);
        PerfilAnexos perfilAnexos = perfilAnexosMapper.toEntity(perfilAnexosDTO);
        perfilAnexos = perfilAnexosRepository.save(perfilAnexos);
        return perfilAnexosMapper.toDto(perfilAnexos);
    }

    /**
     * Partially update a perfilAnexos.
     *
     * @param perfilAnexosDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PerfilAnexosDTO> partialUpdate(PerfilAnexosDTO perfilAnexosDTO) {
        log.debug("Request to partially update PerfilAnexos : {}", perfilAnexosDTO);

        return perfilAnexosRepository
            .findById(perfilAnexosDTO.getId())
            .map(existingPerfilAnexos -> {
                perfilAnexosMapper.partialUpdate(existingPerfilAnexos, perfilAnexosDTO);

                return existingPerfilAnexos;
            })
            .map(perfilAnexosRepository::save)
            .map(perfilAnexosMapper::toDto);
    }

    /**
     * Get all the perfilAnexos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PerfilAnexosDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PerfilAnexos");
        return perfilAnexosRepository.findAll(pageable).map(perfilAnexosMapper::toDto);
    }

    /**
     * Get one perfilAnexos by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PerfilAnexosDTO> findOne(Long id) {
        log.debug("Request to get PerfilAnexos : {}", id);
        return perfilAnexosRepository.findById(id).map(perfilAnexosMapper::toDto);
    }

    /**
     * Delete the perfilAnexos by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PerfilAnexos : {}", id);
        perfilAnexosRepository.deleteById(id);
    }
}

package com.infocargas.freteapp.service;

import com.infocargas.freteapp.domain.Frota;
import com.infocargas.freteapp.repository.FrotaRepository;
import com.infocargas.freteapp.service.dto.FrotaDTO;
import com.infocargas.freteapp.service.mapper.FrotaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Frota}.
 */
@Service
@Transactional
public class FrotaService {

    private final Logger log = LoggerFactory.getLogger(FrotaService.class);

    private final FrotaRepository frotaRepository;

    private final FrotaMapper frotaMapper;

    public FrotaService(FrotaRepository frotaRepository, FrotaMapper frotaMapper) {
        this.frotaRepository = frotaRepository;
        this.frotaMapper = frotaMapper;
    }

    /**
     * Save a frota.
     *
     * @param frotaDTO the entity to save.
     * @return the persisted entity.
     */
    public FrotaDTO save(FrotaDTO frotaDTO) {
        log.debug("Request to save Frota : {}", frotaDTO);
        Frota frota = frotaMapper.toEntity(frotaDTO);
        frota = frotaRepository.save(frota);
        return frotaMapper.toDto(frota);
    }

    /**
     * Update a frota.
     *
     * @param frotaDTO the entity to save.
     * @return the persisted entity.
     */
    public FrotaDTO update(FrotaDTO frotaDTO) {
        log.debug("Request to save Frota : {}", frotaDTO);
        Frota frota = frotaMapper.toEntity(frotaDTO);
        frota = frotaRepository.save(frota);
        return frotaMapper.toDto(frota);
    }

    /**
     * Partially update a frota.
     *
     * @param frotaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FrotaDTO> partialUpdate(FrotaDTO frotaDTO) {
        log.debug("Request to partially update Frota : {}", frotaDTO);

        return frotaRepository
            .findById(frotaDTO.getId())
            .map(existingFrota -> {
                frotaMapper.partialUpdate(existingFrota, frotaDTO);

                return existingFrota;
            })
            .map(frotaRepository::save)
            .map(frotaMapper::toDto);
    }

    /**
     * Get all the frotas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<FrotaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Frotas");
        return frotaRepository.findAll(pageable).map(frotaMapper::toDto);
    }

    /**
     * Get one frota by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FrotaDTO> findOne(Long id) {
        log.debug("Request to get Frota : {}", id);
        return frotaRepository.findById(id).map(frotaMapper::toDto);
    }

    /**
     * Delete the frota by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Frota : {}", id);
        frotaRepository.deleteById(id);
    }
}

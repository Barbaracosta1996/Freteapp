package com.infocargas.freteapp.service;

import com.infocargas.freteapp.domain.CategoriaVeiculo;
import com.infocargas.freteapp.repository.CategoriaVeiculoRepository;
import com.infocargas.freteapp.service.dto.CategoriaVeiculoDTO;
import com.infocargas.freteapp.service.mapper.CategoriaVeiculoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CategoriaVeiculo}.
 */
@Service
@Transactional
public class CategoriaVeiculoService {

    private final Logger log = LoggerFactory.getLogger(CategoriaVeiculoService.class);

    private final CategoriaVeiculoRepository categoriaVeiculoRepository;

    private final CategoriaVeiculoMapper categoriaVeiculoMapper;

    public CategoriaVeiculoService(CategoriaVeiculoRepository categoriaVeiculoRepository, CategoriaVeiculoMapper categoriaVeiculoMapper) {
        this.categoriaVeiculoRepository = categoriaVeiculoRepository;
        this.categoriaVeiculoMapper = categoriaVeiculoMapper;
    }

    /**
     * Save a categoriaVeiculo.
     *
     * @param categoriaVeiculoDTO the entity to save.
     * @return the persisted entity.
     */
    public CategoriaVeiculoDTO save(CategoriaVeiculoDTO categoriaVeiculoDTO) {
        log.debug("Request to save CategoriaVeiculo : {}", categoriaVeiculoDTO);
        CategoriaVeiculo categoriaVeiculo = categoriaVeiculoMapper.toEntity(categoriaVeiculoDTO);
        categoriaVeiculo = categoriaVeiculoRepository.save(categoriaVeiculo);
        return categoriaVeiculoMapper.toDto(categoriaVeiculo);
    }

    /**
     * Update a categoriaVeiculo.
     *
     * @param categoriaVeiculoDTO the entity to save.
     * @return the persisted entity.
     */
    public CategoriaVeiculoDTO update(CategoriaVeiculoDTO categoriaVeiculoDTO) {
        log.debug("Request to save CategoriaVeiculo : {}", categoriaVeiculoDTO);
        CategoriaVeiculo categoriaVeiculo = categoriaVeiculoMapper.toEntity(categoriaVeiculoDTO);
        categoriaVeiculo = categoriaVeiculoRepository.save(categoriaVeiculo);
        return categoriaVeiculoMapper.toDto(categoriaVeiculo);
    }

    /**
     * Partially update a categoriaVeiculo.
     *
     * @param categoriaVeiculoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CategoriaVeiculoDTO> partialUpdate(CategoriaVeiculoDTO categoriaVeiculoDTO) {
        log.debug("Request to partially update CategoriaVeiculo : {}", categoriaVeiculoDTO);

        return categoriaVeiculoRepository
            .findById(categoriaVeiculoDTO.getId())
            .map(existingCategoriaVeiculo -> {
                categoriaVeiculoMapper.partialUpdate(existingCategoriaVeiculo, categoriaVeiculoDTO);

                return existingCategoriaVeiculo;
            })
            .map(categoriaVeiculoRepository::save)
            .map(categoriaVeiculoMapper::toDto);
    }

    /**
     * Get all the categoriaVeiculos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CategoriaVeiculoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CategoriaVeiculos");
        return categoriaVeiculoRepository.findAll(pageable).map(categoriaVeiculoMapper::toDto);
    }

    /**
     * Get one categoriaVeiculo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CategoriaVeiculoDTO> findOne(Long id) {
        log.debug("Request to get CategoriaVeiculo : {}", id);
        return categoriaVeiculoRepository.findById(id).map(categoriaVeiculoMapper::toDto);
    }

    /**
     * Delete the categoriaVeiculo by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CategoriaVeiculo : {}", id);
        categoriaVeiculoRepository.deleteById(id);
    }
}

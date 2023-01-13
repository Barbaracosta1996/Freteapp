package com.infocargas.freteapp.service;

import com.infocargas.freteapp.domain.RotasOfertas;
import com.infocargas.freteapp.domain.enumeration.StatusOferta;
import com.infocargas.freteapp.domain.enumeration.TipoOferta;
import com.infocargas.freteapp.repository.RotasOfertasRepository;
import com.infocargas.freteapp.service.dto.OfertasDTO;
import com.infocargas.freteapp.service.dto.RotasOfertasDTO;
import com.infocargas.freteapp.service.mapper.RotasOfertasMapper;
import com.mashape.unirest.http.exceptions.UnirestException;
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
 * Service Implementation for managing {@link RotasOfertas}.
 */
@Service
public class RotasOfertasService {

    private final Logger log = LoggerFactory.getLogger(RotasOfertasService.class);

    private final RotasOfertasRepository rotasOfertasRepository;

    private final RotasOfertasMapper rotasOfertasMapper;

    private final GooglePlacesServices googlePlacesServices;

    public RotasOfertasService(
        RotasOfertasRepository rotasOfertasRepository,
        RotasOfertasMapper rotasOfertasMapper,
        GooglePlacesServices googlePlacesServices
    ) {
        this.rotasOfertasRepository = rotasOfertasRepository;
        this.rotasOfertasMapper = rotasOfertasMapper;
        this.googlePlacesServices = googlePlacesServices;
    }

    /**
     * Save a rotasOfertas.
     *
     * @param rotasOfertasDTO the entity to save.
     * @return the persisted entity.
     */
    public RotasOfertasDTO save(RotasOfertasDTO rotasOfertasDTO) {
        log.debug("Request to save RotasOfertas : {}", rotasOfertasDTO);
        RotasOfertas rotasOfertas = rotasOfertasMapper.toEntity(rotasOfertasDTO);
        rotasOfertas = rotasOfertasRepository.save(rotasOfertas);
        return rotasOfertasMapper.toDto(rotasOfertas);
    }

    /**
     * Update a rotasOfertas.
     *
     * @param rotasOfertasDTO the entity to save.
     * @return the persisted entity.
     */
    public RotasOfertasDTO update(RotasOfertasDTO rotasOfertasDTO) {
        log.debug("Request to save RotasOfertas : {}", rotasOfertasDTO);
        RotasOfertas rotasOfertas = rotasOfertasMapper.toEntity(rotasOfertasDTO);
        rotasOfertas = rotasOfertasRepository.save(rotasOfertas);
        return rotasOfertasMapper.toDto(rotasOfertas);
    }

    /**
     * Partially update a rotasOfertas.
     *
     * @param rotasOfertasDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RotasOfertasDTO> partialUpdate(RotasOfertasDTO rotasOfertasDTO) {
        log.debug("Request to partially update RotasOfertas : {}", rotasOfertasDTO);

        return rotasOfertasRepository
            .findById(rotasOfertasDTO.getId())
            .map(existingRotasOfertas -> {
                rotasOfertasMapper.partialUpdate(existingRotasOfertas, rotasOfertasDTO);

                return existingRotasOfertas;
            })
            .map(rotasOfertasRepository::save)
            .map(rotasOfertasMapper::toDto);
    }

    /**
     * Get all the rotasOfertas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RotasOfertasDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RotasOfertas");
        return rotasOfertasRepository.findAll(pageable).map(rotasOfertasMapper::toDto);
    }

    /**
     * Get all the rotasOfertas.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<RotasOfertasDTO> findAllNotPerfilId(Long perfilId, TipoOferta tipo, StatusOferta statusOferta) {
        log.debug("Request to get all RotasOfertas");
        return rotasOfertasMapper.toDto(rotasOfertasRepository.allOfertasAvaiable(perfilId, tipo, statusOferta, ZonedDateTime.now()));
    }

    /**
     * Get all the rotasOfertas.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<RotasOfertasDTO> findAll() {
        log.debug("Request to get all RotasOfertas");
        List<RotasOfertas> lista = rotasOfertasRepository.findAll();
        return rotasOfertasMapper.toDto(lista);
    }

    @Transactional(readOnly = true)
    public List<RotasOfertasDTO> findAllByStatusAndDate(StatusOferta status) {
        log.debug("Request to get all RotasOfertas");
        List<RotasOfertas> lista = rotasOfertasRepository.findAllByOfertasStatusAndOfertasDataFechamentoGreaterThanEqual(
            status,
            ZonedDateTime.now()
        );
        return rotasOfertasMapper.toDto(lista);
    }

    @Transactional(readOnly = true)
    public List<RotasOfertasDTO> findByExpired(StatusOferta status) {
        log.debug("Request to get all Ofertas");
        return rotasOfertasMapper.toDto(
            rotasOfertasRepository.findRotasOfertasByOfertasStatusAndOfertasDataFechamentoLessThanEqual(status, ZonedDateTime.now())
        );
    }

    /**
     * Get one rotasOfertas by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RotasOfertasDTO> findOne(Long id) {
        log.debug("Request to get RotasOfertas : {}", id);
        return rotasOfertasRepository.findById(id).map(rotasOfertasMapper::toDto);
    }

    /**
     * Get one rotasOfertas by id.
     *
     * @param idOferta the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RotasOfertasDTO> findByIdOferta(Long idOferta) {
        log.debug("Request to get RotasOfertas : {}", idOferta);
        return rotasOfertasRepository.findByOfertasId(idOferta).map(rotasOfertasMapper::toDto);
    }

    public void saveNewRoute(OfertasDTO ofertasDTO) {
        try {
            RotasOfertasDTO rotasOfertasDTO = new RotasOfertasDTO();
            rotasOfertasDTO.setOfertas(ofertasDTO);
            String routes = googlePlacesServices.getRouteDirections(ofertasDTO);
            rotasOfertasDTO.setRotas(routes);
            save(rotasOfertasDTO);
        } catch (UnirestException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateRoute(OfertasDTO ofertasDTO) {
        Optional<RotasOfertasDTO> rotasOfertasDTO = findByIdOferta(ofertasDTO.getId());

        rotasOfertasDTO.ifPresentOrElse(
            rotaOfertasUpdate -> {
                try {
                    String routes = googlePlacesServices.getRouteDirections(ofertasDTO);
                    rotaOfertasUpdate.setRotas(routes);
                    save(rotaOfertasUpdate);
                } catch (UnirestException e) {
                    throw new RuntimeException(e);
                }
            },
            () -> saveNewRoute(ofertasDTO)
        );
    }

    /**
     * Delete the rotasOfertas by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete RotasOfertas : {}", id);
        rotasOfertasRepository.deleteById(id);
    }
}

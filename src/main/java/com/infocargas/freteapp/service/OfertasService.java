package com.infocargas.freteapp.service;

import com.infocargas.freteapp.controller.FacebookController;
import com.infocargas.freteapp.domain.Ofertas;
import com.infocargas.freteapp.domain.enumeration.StatusOferta;
import com.infocargas.freteapp.domain.enumeration.TipoOferta;
import com.infocargas.freteapp.proxy.FacebookApiProxy;
import com.infocargas.freteapp.repository.OfertasRepository;
import com.infocargas.freteapp.service.dto.OfertasDTO;
import com.infocargas.freteapp.service.mapper.OfertasMapper;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Ofertas}.
 */
@Service
@Transactional
public class OfertasService {

    private final Logger log = LoggerFactory.getLogger(OfertasService.class);

    private final OfertasRepository ofertasRepository;

    private final OfertasMapper ofertasMapper;

    private final RotasOfertasService rotasOfertasService;

    private final FacebookController facebookController;

    public OfertasService(
        OfertasRepository ofertasRepository,
        OfertasMapper ofertasMapper,
        RotasOfertasService rotasOfertasService,
        FacebookController facebookController
    ) {
        this.ofertasRepository = ofertasRepository;
        this.ofertasMapper = ofertasMapper;
        this.rotasOfertasService = rotasOfertasService;
        this.facebookController = facebookController;
    }

    /**
     * Save an ofertas.
     *
     * @param ofertasDTO the entity to save.
     * @return the persisted entity.
     */
    public OfertasDTO save(OfertasDTO ofertasDTO) {
        log.debug("Request to save Ofertas : {}", ofertasDTO);
        Ofertas ofertas = ofertasMapper.toEntity(ofertasDTO);
        ofertas = ofertasRepository.save(ofertas);
        return ofertasMapper.toDto(ofertas);
    }

    public OfertasDTO createPortal(OfertasDTO ofertasDTO) {
        OfertasDTO ofertas = save(ofertasDTO);
        facebookController.createRegistrationOffer(ofertasDTO);

        if (ofertas.getId() != null) {
            rotasOfertasService.saveNewRoute(ofertas);
        }

        return ofertas;
    }

    public OfertasDTO updatePortal(OfertasDTO ofertasDTO) {
        OfertasDTO ofertas = save(ofertasDTO);

        if (ofertas.getStatus() == StatusOferta.AGUARDANDO_PROPOSTA) {
            rotasOfertasService.updateRoute(ofertas);
            facebookController.sendOneMessage(
                ofertas.getPerfil().getUser().getPhone(),
                String.format(
                    "Você acaba de atualizar uma oferta de: %s para: %s para o dia: %s. Fique atento às notificações de transporte aqui no Whatsapp.",
                    ofertas.getOrigem(),
                    ofertas.getDestino(),
                    ofertas.getDataFechamento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                )
            );
        }

        return ofertas;
    }

    /**
     * Update an ofertas.
     *
     * @param ofertasDTO the entity to save.
     * @return the persisted entity.
     */
    public OfertasDTO update(OfertasDTO ofertasDTO) {
        log.debug("Request to save Ofertas : {}", ofertasDTO);
        Ofertas ofertas = ofertasMapper.toEntity(ofertasDTO);
        ofertas = ofertasRepository.save(ofertas);
        return ofertasMapper.toDto(ofertas);
    }

    /**
     * Partially update an ofertas.
     *
     * @param ofertasDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OfertasDTO> partialUpdate(OfertasDTO ofertasDTO) {
        log.debug("Request to partially update Ofertas : {}", ofertasDTO);

        return ofertasRepository
            .findById(ofertasDTO.getId())
            .map(existingOfertas -> {
                ofertasMapper.partialUpdate(existingOfertas, ofertasDTO);

                return existingOfertas;
            })
            .map(ofertasRepository::save)
            .map(ofertasMapper::toDto);
    }

    /**
     * Get all the ofertas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<OfertasDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Ofertas");
        return ofertasRepository.findAll(pageable).map(ofertasMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<OfertasDTO> findByExpired(StatusOferta status) {
        log.debug("Request to get all Ofertas");
        return ofertasMapper.toDto(ofertasRepository.findAllByStatusAndDataFechamentoLessThanEqual(status, ZonedDateTime.now()));
    }

    /**
     * Get one ofertas by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OfertasDTO> findOne(Long id) {
        log.debug("Request to get Ofertas : {}", id);
        return ofertasRepository.findById(id).map(ofertasMapper::toDto);
    }

    /**
     * Delete the ofertas by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Ofertas : {}", id);
        ofertasRepository.deleteById(id);
    }
}

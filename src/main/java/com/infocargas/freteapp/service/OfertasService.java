package com.infocargas.freteapp.service;

import com.infocargas.freteapp.domain.Ofertas;
import com.infocargas.freteapp.domain.enumeration.TipoOferta;
import com.infocargas.freteapp.repository.OfertasRepository;
import com.infocargas.freteapp.service.dto.OfertasDTO;
import com.infocargas.freteapp.service.mapper.OfertasMapper;
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

    private final ClickaTellService clickaTellService;

    public OfertasService(OfertasRepository ofertasRepository, OfertasMapper ofertasMapper, RotasOfertasService rotasOfertasService, ClickaTellService clickaTellService) {
        this.ofertasRepository = ofertasRepository;
        this.ofertasMapper = ofertasMapper;
        this.clickaTellService = clickaTellService;
        this.rotasOfertasService = rotasOfertasService;
    }

    /**
     * Save a ofertas.
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

    public OfertasDTO createPortal(OfertasDTO ofertasDTO){;
        OfertasDTO ofertas = save(ofertasDTO);

        if (ofertas.getTipoOferta() == TipoOferta.CARGA){
            clickaTellService.sendSms("Você divulgou uma nova carga em breve vamos lhe notificar possíveis vagas.", "+55" + ofertasDTO.getPerfil().getTelefoneComercial());
        } else {
            clickaTellService.sendSms("Você divulgou uma vaga em breve vamos lhe notificar possíveis cargas.", "+55" + ofertasDTO.getPerfil().getTelefoneComercial());
        }

        if (ofertas.getId() != null){
            rotasOfertasService.saveNewRoute(ofertas);
        }
        return ofertas;
    }

    /**
     * Update a ofertas.
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
     * Partially update a ofertas.
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

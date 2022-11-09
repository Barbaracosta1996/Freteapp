package com.infocargas.freteapp.service;

import com.infocargas.freteapp.domain.Ofertas;
import com.infocargas.freteapp.domain.Ofertas_;
import com.infocargas.freteapp.domain.Solicitacao;
import com.infocargas.freteapp.domain.enumeration.AnwserStatus;
import com.infocargas.freteapp.domain.enumeration.StatusOferta;
import com.infocargas.freteapp.domain.enumeration.TipoConta;
import com.infocargas.freteapp.repository.SolicitacaoRepository;
import com.infocargas.freteapp.service.dto.OfertasDTO;
import com.infocargas.freteapp.service.dto.SolicitacaoDTO;
import com.infocargas.freteapp.service.mapper.SolicitacaoMapper;
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
 * Service Implementation for managing {@link Solicitacao}.
 */
@Service
@Transactional
public class SolicitacaoService {

    private final Logger log = LoggerFactory.getLogger(SolicitacaoService.class);

    private final SolicitacaoRepository solicitacaoRepository;

    private final SolicitacaoMapper solicitacaoMapper;

    private final OfertasService ofertasService;

    private final ClickaTellService clickaTellService;

    public SolicitacaoService(
        SolicitacaoRepository solicitacaoRepository,
        SolicitacaoMapper solicitacaoMapper,
        OfertasService ofertasService,
        ClickaTellService clickaTellService
    ) {
        this.solicitacaoRepository = solicitacaoRepository;
        this.solicitacaoMapper = solicitacaoMapper;
        this.ofertasService = ofertasService;
        this.clickaTellService = clickaTellService;
    }

    /**
     * Save a solicitacao.
     *
     * @param solicitacaoDTO the entity to save.
     * @return the persisted entity.
     */
    public SolicitacaoDTO save(SolicitacaoDTO solicitacaoDTO) {
        log.debug("Request to save Solicitacao : {}", solicitacaoDTO);
        Solicitacao solicitacao = solicitacaoMapper.toEntity(solicitacaoDTO);
        solicitacao = solicitacaoRepository.save(solicitacao);
        return solicitacaoMapper.toDto(solicitacao);
    }

    /**
     * Update a solicitacao.
     *
     * @param solicitacaoDTO the entity to save.
     * @return the persisted entity.
     */
    public SolicitacaoDTO update(SolicitacaoDTO solicitacaoDTO) {
        Solicitacao solicitacao = solicitacaoMapper.toEntity(solicitacaoDTO);
        solicitacao = solicitacaoRepository.save(solicitacao);
        ofertasService.save(solicitacaoDTO.getOfertas());
        return solicitacaoMapper.toDto(solicitacao);
    }

    /**
     * Partially update a solicitacao.
     *
     * @param solicitacaoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SolicitacaoDTO> partialUpdate(SolicitacaoDTO solicitacaoDTO) {
        log.debug("Request to partially update Solicitacao : {}", solicitacaoDTO);

        return solicitacaoRepository
            .findById(solicitacaoDTO.getId())
            .map(existingSolicitacao -> {
                solicitacaoMapper.partialUpdate(existingSolicitacao, solicitacaoDTO);

                return existingSolicitacao;
            })
            .map(solicitacaoRepository::save)
            .map(solicitacaoMapper::toDto);
    }

    /**
     * Get all the solicitacaos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SolicitacaoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Solicitacaos");
        return solicitacaoRepository.findAll(pageable).map(solicitacaoMapper::toDto);
    }

    /**
     * Get one solicitacao by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SolicitacaoDTO> findOne(Long id) {
        log.debug("Request to get Solicitacao : {}", id);
        return solicitacaoRepository.findById(id).map(solicitacaoMapper::toDto);
    }

    /**
     * Get one solicitacao by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public List<SolicitacaoDTO> findAllByPerfil(Long id) {
        log.debug("Request to get Solicitacao : {}", id);
        return solicitacaoMapper.toDto(solicitacaoRepository.findByOfertasPerfilId(id));
    }

    /**
     * Delete the solicitacao by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Solicitacao : {}", id);
        solicitacaoRepository.deleteById(id);
    }
}

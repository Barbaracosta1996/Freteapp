package com.infocargas.freteapp.service;

import com.infocargas.freteapp.domain.*; // for static metamodels
import com.infocargas.freteapp.domain.Solicitacao;
import com.infocargas.freteapp.repository.SolicitacaoRepository;
import com.infocargas.freteapp.service.criteria.SolicitacaoCriteria;
import com.infocargas.freteapp.service.dto.SolicitacaoDTO;
import com.infocargas.freteapp.service.mapper.SolicitacaoMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Solicitacao} entities in the database.
 * The main input is a {@link SolicitacaoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SolicitacaoDTO} or a {@link Page} of {@link SolicitacaoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SolicitacaoQueryService extends QueryService<Solicitacao> {

    private final Logger log = LoggerFactory.getLogger(SolicitacaoQueryService.class);

    private final SolicitacaoRepository solicitacaoRepository;

    private final SolicitacaoMapper solicitacaoMapper;

    public SolicitacaoQueryService(SolicitacaoRepository solicitacaoRepository, SolicitacaoMapper solicitacaoMapper) {
        this.solicitacaoRepository = solicitacaoRepository;
        this.solicitacaoMapper = solicitacaoMapper;
    }

    /**
     * Return a {@link List} of {@link SolicitacaoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SolicitacaoDTO> findByCriteria(SolicitacaoCriteria criteria) {
        log.debug("find by criteria : {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        final Specification<Solicitacao> specification = createSpecification(criteria);
        return solicitacaoMapper.toDto(solicitacaoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SolicitacaoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SolicitacaoDTO> findByCriteria(SolicitacaoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria.toString().replaceAll("[\n\r\t]", "_"), page);
        final Specification<Solicitacao> specification = createSpecification(criteria);
        return solicitacaoRepository.findAll(specification, page).map(solicitacaoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SolicitacaoCriteria criteria) {
        log.debug("count by criteria : {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        final Specification<Solicitacao> specification = createSpecification(criteria);
        return solicitacaoRepository.count(specification);
    }

    /**
     * Function to convert {@link SolicitacaoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Solicitacao> createSpecification(SolicitacaoCriteria criteria) {
        Specification<Solicitacao> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Solicitacao_.id));
            }
            if (criteria.getDataProposta() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDataProposta(), Solicitacao_.dataProposta));
            }
            if (criteria.getDataModificacao() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDataModificacao(), Solicitacao_.dataModificacao));
            }
            if (criteria.getAceitar() != null) {
                specification = specification.and(buildSpecification(criteria.getAceitar(), Solicitacao_.aceitar));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Solicitacao_.status));
            }
            if (criteria.getObs() != null) {
                specification = specification.and(buildStringSpecification(criteria.getObs(), Solicitacao_.obs));
            }
            if (criteria.getValorFrete() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValorFrete(), Solicitacao_.valorFrete));
            }
            if (criteria.getOfertasId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getOfertasId(), root -> root.join(Solicitacao_.ofertas, JoinType.LEFT).get(Ofertas_.id))
                    );
            }
            if (criteria.getOfertasUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getOfertasUserId(), root -> root.join(Solicitacao_.ofertas, JoinType.LEFT)
                            .join(Ofertas_.perfil, JoinType.LEFT)
                            .join(Perfil_.user, JoinType.LEFT)
                            .get(User_.id))
                    );
            }
            if (criteria.getPerfilId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getPerfilId(), root -> root.join(Solicitacao_.perfil, JoinType.LEFT).get(Perfil_.id))
                    );
            }
        }
        return specification;
    }
}

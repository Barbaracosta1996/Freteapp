package com.infocargas.freteapp.service;

import com.infocargas.freteapp.domain.*; // for static metamodels
import com.infocargas.freteapp.domain.Ofertas;
import com.infocargas.freteapp.repository.OfertasRepository;
import com.infocargas.freteapp.service.criteria.OfertasCriteria;
import com.infocargas.freteapp.service.dto.OfertasDTO;
import com.infocargas.freteapp.service.mapper.OfertasMapper;
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
 * Service for executing complex queries for {@link Ofertas} entities in the database.
 * The main input is a {@link OfertasCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OfertasDTO} or a {@link Page} of {@link OfertasDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OfertasQueryService extends QueryService<Ofertas> {

    private final Logger log = LoggerFactory.getLogger(OfertasQueryService.class);

    private final OfertasRepository ofertasRepository;

    private final OfertasMapper ofertasMapper;

    public OfertasQueryService(OfertasRepository ofertasRepository, OfertasMapper ofertasMapper) {
        this.ofertasRepository = ofertasRepository;
        this.ofertasMapper = ofertasMapper;
    }

    /**
     * Return a {@link List} of {@link OfertasDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OfertasDTO> findByCriteria(OfertasCriteria criteria) {
        log.debug("find by criteria : {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        final Specification<Ofertas> specification = createSpecification(criteria);
        return ofertasMapper.toDto(ofertasRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link OfertasDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OfertasDTO> findByCriteria(OfertasCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria.toString().replaceAll("[\n\r\t]", "_"), page);
        final Specification<Ofertas> specification = createSpecification(criteria);
        return ofertasRepository.findAll(specification, page).map(ofertasMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OfertasCriteria criteria) {
        log.debug("count by criteria : {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        final Specification<Ofertas> specification = createSpecification(criteria);
        return ofertasRepository.count(specification);
    }

    /**
     * Function to convert {@link OfertasCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Ofertas> createSpecification(OfertasCriteria criteria) {
        Specification<Ofertas> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Ofertas_.id));
            }
            if (criteria.getDataPostagem() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDataPostagem(), Ofertas_.dataPostagem));
            }
            if (criteria.getQuantidade() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantidade(), Ofertas_.quantidade));
            }
            if (criteria.getTipoCarga() != null) {
                specification = specification.and(buildSpecification(criteria.getTipoCarga(), Ofertas_.tipoCarga));
            }
            if (criteria.getDataColeta() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDataColeta(), Ofertas_.dataColeta));
            }
            if (criteria.getDataEntrega() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDataEntrega(), Ofertas_.dataEntrega));
            }
            if (criteria.getDataModificacao() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDataModificacao(), Ofertas_.dataModificacao));
            }
            if (criteria.getDataFechamento() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDataFechamento(), Ofertas_.dataFechamento));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Ofertas_.status));
            }
            if (criteria.getTipoOferta() != null) {
                specification = specification.and(buildSpecification(criteria.getTipoOferta(), Ofertas_.tipoOferta));
            }
            if (criteria.getTipoTransporte() != null) {
                specification = specification.and(buildSpecification(criteria.getTipoTransporte(), Ofertas_.tipoTransporte));
            }
            if (criteria.getDestino() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDestino(), Ofertas_.destino));
            }
            if (criteria.getOrigem() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOrigem(), Ofertas_.origem));
            }
            if (criteria.getPerfilId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getPerfilId(), root -> root.join(Ofertas_.perfil, JoinType.LEFT).get(Perfil_.id))
                    );
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(Ofertas_.perfil, JoinType.LEFT)
                            .join(Perfil_.user, JoinType.LEFT).get(User_.id))
                    );
            }
        }
        return specification;
    }
}

package com.infocargas.freteapp.service;

import com.infocargas.freteapp.domain.*; // for static metamodels
import com.infocargas.freteapp.domain.PerfilAnexos;
import com.infocargas.freteapp.repository.PerfilAnexosRepository;
import com.infocargas.freteapp.service.criteria.PerfilAnexosCriteria;
import com.infocargas.freteapp.service.dto.PerfilAnexosDTO;
import com.infocargas.freteapp.service.mapper.PerfilAnexosMapper;
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
 * Service for executing complex queries for {@link PerfilAnexos} entities in the database.
 * The main input is a {@link PerfilAnexosCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PerfilAnexosDTO} or a {@link Page} of {@link PerfilAnexosDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PerfilAnexosQueryService extends QueryService<PerfilAnexos> {

    private final Logger log = LoggerFactory.getLogger(PerfilAnexosQueryService.class);

    private final PerfilAnexosRepository perfilAnexosRepository;

    private final PerfilAnexosMapper perfilAnexosMapper;

    public PerfilAnexosQueryService(PerfilAnexosRepository perfilAnexosRepository, PerfilAnexosMapper perfilAnexosMapper) {
        this.perfilAnexosRepository = perfilAnexosRepository;
        this.perfilAnexosMapper = perfilAnexosMapper;
    }

    /**
     * Return a {@link List} of {@link PerfilAnexosDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PerfilAnexosDTO> findByCriteria(PerfilAnexosCriteria criteria) {
        log.debug("find by criteria : {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        final Specification<PerfilAnexos> specification = createSpecification(criteria);
        return perfilAnexosMapper.toDto(perfilAnexosRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PerfilAnexosDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PerfilAnexosDTO> findByCriteria(PerfilAnexosCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria.toString().replaceAll("[\n\r\t]", "_"), page);
        final Specification<PerfilAnexos> specification = createSpecification(criteria);
        return perfilAnexosRepository.findAll(specification, page).map(perfilAnexosMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PerfilAnexosCriteria criteria) {
        log.debug("count by criteria : {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        final Specification<PerfilAnexos> specification = createSpecification(criteria);
        return perfilAnexosRepository.count(specification);
    }

    /**
     * Function to convert {@link PerfilAnexosCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PerfilAnexos> createSpecification(PerfilAnexosCriteria criteria) {
        Specification<PerfilAnexos> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PerfilAnexos_.id));
            }
            if (criteria.getDataPostagem() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDataPostagem(), PerfilAnexos_.dataPostagem));
            }
            if (criteria.getTipoDocumento() != null) {
                specification = specification.and(buildSpecification(criteria.getTipoDocumento(), PerfilAnexos_.tipoDocumento));
            }
            if (criteria.getDescricao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescricao(), PerfilAnexos_.descricao));
            }
            if (criteria.getPerfilId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getPerfilId(), root -> root.join(PerfilAnexos_.perfil, JoinType.LEFT).get(Perfil_.id))
                    );
            }
        }
        return specification;
    }
}

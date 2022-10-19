package com.infocargas.freteapp.service;

import com.infocargas.freteapp.domain.*; // for static metamodels
import com.infocargas.freteapp.domain.RotasOfertas;
import com.infocargas.freteapp.repository.RotasOfertasRepository;
import com.infocargas.freteapp.service.criteria.RotasOfertasCriteria;
import com.infocargas.freteapp.service.dto.RotasOfertasDTO;
import com.infocargas.freteapp.service.mapper.RotasOfertasMapper;
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
 * Service for executing complex queries for {@link RotasOfertas} entities in the database.
 * The main input is a {@link RotasOfertasCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RotasOfertasDTO} or a {@link Page} of {@link RotasOfertasDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RotasOfertasQueryService extends QueryService<RotasOfertas> {

    private final Logger log = LoggerFactory.getLogger(RotasOfertasQueryService.class);

    private final RotasOfertasRepository rotasOfertasRepository;

    private final RotasOfertasMapper rotasOfertasMapper;

    public RotasOfertasQueryService(RotasOfertasRepository rotasOfertasRepository, RotasOfertasMapper rotasOfertasMapper) {
        this.rotasOfertasRepository = rotasOfertasRepository;
        this.rotasOfertasMapper = rotasOfertasMapper;
    }

    /**
     * Return a {@link List} of {@link RotasOfertasDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RotasOfertasDTO> findByCriteria(RotasOfertasCriteria criteria) {
        log.debug("find by criteria : {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        final Specification<RotasOfertas> specification = createSpecification(criteria);
        return rotasOfertasMapper.toDto(rotasOfertasRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link RotasOfertasDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RotasOfertasDTO> findByCriteria(RotasOfertasCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria.toString().replaceAll("[\n\r\t]", "_"), page);
        final Specification<RotasOfertas> specification = createSpecification(criteria);
        return rotasOfertasRepository.findAll(specification, page).map(rotasOfertasMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RotasOfertasCriteria criteria) {
        log.debug("count by criteria : {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        final Specification<RotasOfertas> specification = createSpecification(criteria);
        return rotasOfertasRepository.count(specification);
    }

    /**
     * Function to convert {@link RotasOfertasCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<RotasOfertas> createSpecification(RotasOfertasCriteria criteria) {
        Specification<RotasOfertas> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), RotasOfertas_.id));
            }
            if (criteria.getOfertasId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getOfertasId(),
                            root -> root.join(RotasOfertas_.ofertas, JoinType.LEFT).get(Ofertas_.id)
                        )
                    );
            }
        }
        return specification;
    }
}

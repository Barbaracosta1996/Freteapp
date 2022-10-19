package com.infocargas.freteapp.service;

import com.infocargas.freteapp.domain.*; // for static metamodels
import com.infocargas.freteapp.domain.Frota;
import com.infocargas.freteapp.repository.FrotaRepository;
import com.infocargas.freteapp.service.criteria.FrotaCriteria;
import com.infocargas.freteapp.service.dto.FrotaDTO;
import com.infocargas.freteapp.service.mapper.FrotaMapper;
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
 * Service for executing complex queries for {@link Frota} entities in the database.
 * The main input is a {@link FrotaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FrotaDTO} or a {@link Page} of {@link FrotaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FrotaQueryService extends QueryService<Frota> {

    private final Logger log = LoggerFactory.getLogger(FrotaQueryService.class);

    private final FrotaRepository frotaRepository;

    private final FrotaMapper frotaMapper;

    public FrotaQueryService(FrotaRepository frotaRepository, FrotaMapper frotaMapper) {
        this.frotaRepository = frotaRepository;
        this.frotaMapper = frotaMapper;
    }

    /**
     * Return a {@link List} of {@link FrotaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FrotaDTO> findByCriteria(FrotaCriteria criteria) {
        log.debug("find by criteria : {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        final Specification<Frota> specification = createSpecification(criteria);
        return frotaMapper.toDto(frotaRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FrotaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FrotaDTO> findByCriteria(FrotaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria.toString().replaceAll("[\n\r\t]", "_"), page);
        final Specification<Frota> specification = createSpecification(criteria);
        return frotaRepository.findAll(specification, page).map(frotaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FrotaCriteria criteria) {
        log.debug("count by criteria : {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        final Specification<Frota> specification = createSpecification(criteria);
        return frotaRepository.count(specification);
    }

    /**
     * Function to convert {@link FrotaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Frota> createSpecification(FrotaCriteria criteria) {
        Specification<Frota> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Frota_.id));
            }
            if (criteria.getNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNome(), Frota_.nome));
            }
            if (criteria.getModelo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getModelo(), Frota_.modelo));
            }
            if (criteria.getMarca() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMarca(), Frota_.marca));
            }
            if (criteria.getAno() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAno(), Frota_.ano));
            }
            if (criteria.getTipoCategoria() != null) {
                specification = specification.and(buildSpecification(criteria.getTipoCategoria(), Frota_.tipoCategoria));
            }
            if (criteria.getObsCategoriaOutro() != null) {
                specification = specification.and(buildStringSpecification(criteria.getObsCategoriaOutro(), Frota_.obsCategoriaOutro));
            }
            if (criteria.getPerfilId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getPerfilId(), root -> root.join(Frota_.perfil, JoinType.LEFT).get(Perfil_.id))
                    );
            }
            if (criteria.getCategoriaVeiculoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCategoriaVeiculoId(),
                            root -> root.join(Frota_.categoriaVeiculo, JoinType.LEFT).get(CategoriaVeiculo_.id)
                        )
                    );
            }
        }
        return specification;
    }
}

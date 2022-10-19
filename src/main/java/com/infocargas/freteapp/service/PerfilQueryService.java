package com.infocargas.freteapp.service;

import com.infocargas.freteapp.domain.*; // for static metamodels
import com.infocargas.freteapp.domain.Perfil;
import com.infocargas.freteapp.repository.PerfilRepository;
import com.infocargas.freteapp.service.criteria.PerfilCriteria;
import com.infocargas.freteapp.service.dto.PerfilDTO;
import com.infocargas.freteapp.service.mapper.PerfilMapper;
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
 * Service for executing complex queries for {@link Perfil} entities in the database.
 * The main input is a {@link PerfilCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PerfilDTO} or a {@link Page} of {@link PerfilDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PerfilQueryService extends QueryService<Perfil> {

    private final Logger log = LoggerFactory.getLogger(PerfilQueryService.class);

    private final PerfilRepository perfilRepository;

    private final PerfilMapper perfilMapper;

    public PerfilQueryService(PerfilRepository perfilRepository, PerfilMapper perfilMapper) {
        this.perfilRepository = perfilRepository;
        this.perfilMapper = perfilMapper;
    }

    /**
     * Return a {@link List} of {@link PerfilDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PerfilDTO> findByCriteria(PerfilCriteria criteria) {
        log.debug("find by criteria : {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        final Specification<Perfil> specification = createSpecification(criteria);
        return perfilMapper.toDto(perfilRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PerfilDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PerfilDTO> findByCriteria(PerfilCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria.toString().replaceAll("[\n\r\t]", "_"), page);
        final Specification<Perfil> specification = createSpecification(criteria);
        return perfilRepository.findAll(specification, page).map(perfilMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PerfilCriteria criteria) {
        log.debug("count by criteria : {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        final Specification<Perfil> specification = createSpecification(criteria);
        return perfilRepository.count(specification);
    }

    /**
     * Function to convert {@link PerfilCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Perfil> createSpecification(PerfilCriteria criteria) {
        Specification<Perfil> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Perfil_.id));
            }
            if (criteria.getTipoConta() != null) {
                specification = specification.and(buildSpecification(criteria.getTipoConta(), Perfil_.tipoConta));
            }
            if (criteria.getCpf() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCpf(), Perfil_.cpf));
            }
            if (criteria.getCnpj() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCnpj(), Perfil_.cnpj));
            }
            if (criteria.getRua() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRua(), Perfil_.rua));
            }
            if (criteria.getNumero() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNumero(), Perfil_.numero));
            }
            if (criteria.getBairro() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBairro(), Perfil_.bairro));
            }
            if (criteria.getCidade() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCidade(), Perfil_.cidade));
            }
            if (criteria.getEstado() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEstado(), Perfil_.estado));
            }
            if (criteria.getCep() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCep(), Perfil_.cep));
            }
            if (criteria.getPais() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPais(), Perfil_.pais));
            }
            if (criteria.getNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNome(), Perfil_.nome));
            }
            if (criteria.getRazaosocial() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRazaosocial(), Perfil_.razaosocial));
            }
            if (criteria.getTelefoneComercial() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTelefoneComercial(), Perfil_.telefoneComercial));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(Perfil_.user, JoinType.LEFT).get(User_.id))
                    );
            }
        }
        return specification;
    }
}

package com.infocargas.freteapp.service;

import com.infocargas.freteapp.domain.*; // for static metamodels
import com.infocargas.freteapp.domain.WhatsMessageBatch;
import com.infocargas.freteapp.repository.WhatsMessageBatchRepository;
import com.infocargas.freteapp.service.criteria.WhatsMessageBatchCriteria;
import com.infocargas.freteapp.service.dto.WhatsMessageBatchDTO;
import com.infocargas.freteapp.service.mapper.WhatsMessageBatchMapper;
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
 * Service for executing complex queries for {@link WhatsMessageBatch} entities in the database.
 * The main input is a {@link WhatsMessageBatchCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link WhatsMessageBatchDTO} or a {@link Page} of {@link WhatsMessageBatchDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class WhatsMessageBatchQueryService extends QueryService<WhatsMessageBatch> {

    private final Logger log = LoggerFactory.getLogger(WhatsMessageBatchQueryService.class);

    private final WhatsMessageBatchRepository whatsMessageBatchRepository;

    private final WhatsMessageBatchMapper whatsMessageBatchMapper;

    public WhatsMessageBatchQueryService(
        WhatsMessageBatchRepository whatsMessageBatchRepository,
        WhatsMessageBatchMapper whatsMessageBatchMapper
    ) {
        this.whatsMessageBatchRepository = whatsMessageBatchRepository;
        this.whatsMessageBatchMapper = whatsMessageBatchMapper;
    }

    /**
     * Return a {@link List} of {@link WhatsMessageBatchDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<WhatsMessageBatchDTO> findByCriteria(WhatsMessageBatchCriteria criteria) {
        log.debug("find by criteria : {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        final Specification<WhatsMessageBatch> specification = createSpecification(criteria);
        return whatsMessageBatchMapper.toDto(whatsMessageBatchRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link WhatsMessageBatchDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<WhatsMessageBatchDTO> findByCriteria(WhatsMessageBatchCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria.toString().replaceAll("[\n\r\t]", "_"), page);
        final Specification<WhatsMessageBatch> specification = createSpecification(criteria);
        return whatsMessageBatchRepository.findAll(specification, page).map(whatsMessageBatchMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(WhatsMessageBatchCriteria criteria) {
        log.debug("count by criteria : {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        final Specification<WhatsMessageBatch> specification = createSpecification(criteria);
        return whatsMessageBatchRepository.count(specification);
    }

    /**
     * Function to convert {@link WhatsMessageBatchCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<WhatsMessageBatch> createSpecification(WhatsMessageBatchCriteria criteria) {
        Specification<WhatsMessageBatch> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), WhatsMessageBatch_.id));
            }
            if (criteria.getTipo() != null) {
                specification = specification.and(buildSpecification(criteria.getTipo(), WhatsMessageBatch_.tipo));
            }
            if (criteria.getWaidTo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getWaidTo(), WhatsMessageBatch_.waidTo));
            }
            if (criteria.getPerfilID() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPerfilID(), WhatsMessageBatch_.perfilID));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), WhatsMessageBatch_.status));
            }
            if (criteria.getOfertaId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOfertaId(), WhatsMessageBatch_.ofertaId));
            }
            if (criteria.getTipoOferta() != null) {
                specification = specification.and(buildSpecification(criteria.getTipoOferta(), WhatsMessageBatch_.tipoOferta));
            }
            if (criteria.getNotificationDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getNotificationDate(), WhatsMessageBatch_.notificationDate));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), WhatsMessageBatch_.createdDate));
            }
        }
        return specification;
    }
}

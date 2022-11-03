package com.infocargas.freteapp.web.rest;

import com.infocargas.freteapp.repository.WhatsMessageBatchRepository;
import com.infocargas.freteapp.service.WhatsMessageBatchQueryService;
import com.infocargas.freteapp.service.WhatsMessageBatchService;
import com.infocargas.freteapp.service.criteria.WhatsMessageBatchCriteria;
import com.infocargas.freteapp.service.dto.WhatsMessageBatchDTO;
import com.infocargas.freteapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.infocargas.freteapp.domain.WhatsMessageBatch}.
 */
@RestController
@RequestMapping("/api")
public class WhatsMessageBatchResource {

    private final Logger log = LoggerFactory.getLogger(WhatsMessageBatchResource.class);

    private static final String ENTITY_NAME = "whatsMessageBatch";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WhatsMessageBatchService whatsMessageBatchService;

    private final WhatsMessageBatchRepository whatsMessageBatchRepository;

    private final WhatsMessageBatchQueryService whatsMessageBatchQueryService;

    public WhatsMessageBatchResource(
        WhatsMessageBatchService whatsMessageBatchService,
        WhatsMessageBatchRepository whatsMessageBatchRepository,
        WhatsMessageBatchQueryService whatsMessageBatchQueryService
    ) {
        this.whatsMessageBatchService = whatsMessageBatchService;
        this.whatsMessageBatchRepository = whatsMessageBatchRepository;
        this.whatsMessageBatchQueryService = whatsMessageBatchQueryService;
    }

    /**
     * {@code POST  /whats-message-batches} : Create a new whatsMessageBatch.
     *
     * @param whatsMessageBatchDTO the whatsMessageBatchDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new whatsMessageBatchDTO, or with status {@code 400 (Bad Request)} if the whatsMessageBatch has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/whats-message-batches")
    public ResponseEntity<WhatsMessageBatchDTO> createWhatsMessageBatch(@Valid @RequestBody WhatsMessageBatchDTO whatsMessageBatchDTO)
        throws URISyntaxException {
        log.debug("REST request to save WhatsMessageBatch : {}", whatsMessageBatchDTO);
        if (whatsMessageBatchDTO.getId() != null) {
            throw new BadRequestAlertException("A new whatsMessageBatch cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WhatsMessageBatchDTO result = whatsMessageBatchService.save(whatsMessageBatchDTO);
        return ResponseEntity
            .created(new URI("/api/whats-message-batches/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /whats-message-batches/:id} : Updates an existing whatsMessageBatch.
     *
     * @param id the id of the whatsMessageBatchDTO to save.
     * @param whatsMessageBatchDTO the whatsMessageBatchDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated whatsMessageBatchDTO,
     * or with status {@code 400 (Bad Request)} if the whatsMessageBatchDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the whatsMessageBatchDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/whats-message-batches/{id}")
    public ResponseEntity<WhatsMessageBatchDTO> updateWhatsMessageBatch(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WhatsMessageBatchDTO whatsMessageBatchDTO
    ) throws URISyntaxException {
        log.debug("REST request to update WhatsMessageBatch : {}, {}", id, whatsMessageBatchDTO);
        if (whatsMessageBatchDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, whatsMessageBatchDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!whatsMessageBatchRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WhatsMessageBatchDTO result = whatsMessageBatchService.update(whatsMessageBatchDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, whatsMessageBatchDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /whats-message-batches/:id} : Partial updates given fields of an existing whatsMessageBatch, field will ignore if it is null
     *
     * @param id the id of the whatsMessageBatchDTO to save.
     * @param whatsMessageBatchDTO the whatsMessageBatchDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated whatsMessageBatchDTO,
     * or with status {@code 400 (Bad Request)} if the whatsMessageBatchDTO is not valid,
     * or with status {@code 404 (Not Found)} if the whatsMessageBatchDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the whatsMessageBatchDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/whats-message-batches/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WhatsMessageBatchDTO> partialUpdateWhatsMessageBatch(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WhatsMessageBatchDTO whatsMessageBatchDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update WhatsMessageBatch partially : {}, {}", id, whatsMessageBatchDTO);
        if (whatsMessageBatchDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, whatsMessageBatchDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!whatsMessageBatchRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WhatsMessageBatchDTO> result = whatsMessageBatchService.partialUpdate(whatsMessageBatchDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, whatsMessageBatchDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /whats-message-batches} : get all the whatsMessageBatches.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of whatsMessageBatches in body.
     */
    @GetMapping("/whats-message-batches")
    public ResponseEntity<List<WhatsMessageBatchDTO>> getAllWhatsMessageBatches(
        WhatsMessageBatchCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get WhatsMessageBatches by criteria: {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        Page<WhatsMessageBatchDTO> page = whatsMessageBatchQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /whats-message-batches/count} : count all the whatsMessageBatches.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/whats-message-batches/count")
    public ResponseEntity<Long> countWhatsMessageBatches(WhatsMessageBatchCriteria criteria) {
        log.debug("REST request to count WhatsMessageBatches by criteria: {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        return ResponseEntity.ok().body(whatsMessageBatchQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /whats-message-batches/:id} : get the "id" whatsMessageBatch.
     *
     * @param id the id of the whatsMessageBatchDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the whatsMessageBatchDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/whats-message-batches/{id}")
    public ResponseEntity<WhatsMessageBatchDTO> getWhatsMessageBatch(@PathVariable Long id) {
        log.debug("REST request to get WhatsMessageBatch : {}", id);
        Optional<WhatsMessageBatchDTO> whatsMessageBatchDTO = whatsMessageBatchService.findOne(id);
        return ResponseUtil.wrapOrNotFound(whatsMessageBatchDTO);
    }

    /**
     * {@code DELETE  /whats-message-batches/:id} : delete the "id" whatsMessageBatch.
     *
     * @param id the id of the whatsMessageBatchDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/whats-message-batches/{id}")
    public ResponseEntity<Void> deleteWhatsMessageBatch(@PathVariable Long id) {
        log.debug("REST request to delete WhatsMessageBatch : {}", id);
        whatsMessageBatchService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}

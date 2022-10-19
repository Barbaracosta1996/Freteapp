package com.infocargas.freteapp.web.rest;

import com.infocargas.freteapp.repository.FrotaRepository;
import com.infocargas.freteapp.service.FrotaQueryService;
import com.infocargas.freteapp.service.FrotaService;
import com.infocargas.freteapp.service.criteria.FrotaCriteria;
import com.infocargas.freteapp.service.dto.FrotaDTO;
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
 * REST controller for managing {@link com.infocargas.freteapp.domain.Frota}.
 */
@RestController
@RequestMapping("/api")
public class FrotaResource {

    private final Logger log = LoggerFactory.getLogger(FrotaResource.class);

    private static final String ENTITY_NAME = "frota";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FrotaService frotaService;

    private final FrotaRepository frotaRepository;

    private final FrotaQueryService frotaQueryService;

    public FrotaResource(FrotaService frotaService, FrotaRepository frotaRepository, FrotaQueryService frotaQueryService) {
        this.frotaService = frotaService;
        this.frotaRepository = frotaRepository;
        this.frotaQueryService = frotaQueryService;
    }

    /**
     * {@code POST  /frotas} : Create a new frota.
     *
     * @param frotaDTO the frotaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new frotaDTO, or with status {@code 400 (Bad Request)} if the frota has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/frotas")
    public ResponseEntity<FrotaDTO> createFrota(@Valid @RequestBody FrotaDTO frotaDTO) throws URISyntaxException {
        log.debug("REST request to save Frota : {}", frotaDTO);
        if (frotaDTO.getId() != null) {
            throw new BadRequestAlertException("A new frota cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FrotaDTO result = frotaService.save(frotaDTO);
        return ResponseEntity
            .created(new URI("/api/frotas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /frotas/:id} : Updates an existing frota.
     *
     * @param id the id of the frotaDTO to save.
     * @param frotaDTO the frotaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated frotaDTO,
     * or with status {@code 400 (Bad Request)} if the frotaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the frotaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/frotas/{id}")
    public ResponseEntity<FrotaDTO> updateFrota(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FrotaDTO frotaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Frota : {}, {}", id, frotaDTO);
        if (frotaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, frotaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!frotaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FrotaDTO result = frotaService.update(frotaDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, frotaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /frotas/:id} : Partial updates given fields of an existing frota, field will ignore if it is null
     *
     * @param id the id of the frotaDTO to save.
     * @param frotaDTO the frotaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated frotaDTO,
     * or with status {@code 400 (Bad Request)} if the frotaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the frotaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the frotaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/frotas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FrotaDTO> partialUpdateFrota(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FrotaDTO frotaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Frota partially : {}, {}", id, frotaDTO);
        if (frotaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, frotaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!frotaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FrotaDTO> result = frotaService.partialUpdate(frotaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, frotaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /frotas} : get all the frotas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of frotas in body.
     */
    @GetMapping("/frotas")
    public ResponseEntity<List<FrotaDTO>> getAllFrotas(
        FrotaCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Frotas by criteria: {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        Page<FrotaDTO> page = frotaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /frotas/count} : count all the frotas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/frotas/count")
    public ResponseEntity<Long> countFrotas(FrotaCriteria criteria) {
        log.debug("REST request to count Frotas by criteria: {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        return ResponseEntity.ok().body(frotaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /frotas/:id} : get the "id" frota.
     *
     * @param id the id of the frotaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the frotaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/frotas/{id}")
    public ResponseEntity<FrotaDTO> getFrota(@PathVariable Long id) {
        log.debug("REST request to get Frota : {}", id);
        Optional<FrotaDTO> frotaDTO = frotaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(frotaDTO);
    }

    /**
     * {@code DELETE  /frotas/:id} : delete the "id" frota.
     *
     * @param id the id of the frotaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/frotas/{id}")
    public ResponseEntity<Void> deleteFrota(@PathVariable Long id) {
        log.debug("REST request to delete Frota : {}", id);
        frotaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}

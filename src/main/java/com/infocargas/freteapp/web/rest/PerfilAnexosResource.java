package com.infocargas.freteapp.web.rest;

import com.infocargas.freteapp.repository.PerfilAnexosRepository;
import com.infocargas.freteapp.service.PerfilAnexosQueryService;
import com.infocargas.freteapp.service.PerfilAnexosService;
import com.infocargas.freteapp.service.criteria.PerfilAnexosCriteria;
import com.infocargas.freteapp.service.dto.PerfilAnexosDTO;
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
 * REST controller for managing {@link com.infocargas.freteapp.domain.PerfilAnexos}.
 */
@RestController
@RequestMapping("/api")
public class PerfilAnexosResource {

    private final Logger log = LoggerFactory.getLogger(PerfilAnexosResource.class);

    private static final String ENTITY_NAME = "perfilAnexos";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PerfilAnexosService perfilAnexosService;

    private final PerfilAnexosRepository perfilAnexosRepository;

    private final PerfilAnexosQueryService perfilAnexosQueryService;

    public PerfilAnexosResource(
        PerfilAnexosService perfilAnexosService,
        PerfilAnexosRepository perfilAnexosRepository,
        PerfilAnexosQueryService perfilAnexosQueryService
    ) {
        this.perfilAnexosService = perfilAnexosService;
        this.perfilAnexosRepository = perfilAnexosRepository;
        this.perfilAnexosQueryService = perfilAnexosQueryService;
    }

    /**
     * {@code POST  /perfil-anexos} : Create a new perfilAnexos.
     *
     * @param perfilAnexosDTO the perfilAnexosDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new perfilAnexosDTO, or with status {@code 400 (Bad Request)} if the perfilAnexos has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/perfil-anexos")
    public ResponseEntity<PerfilAnexosDTO> createPerfilAnexos(@Valid @RequestBody PerfilAnexosDTO perfilAnexosDTO)
        throws URISyntaxException {
        log.debug("REST request to save PerfilAnexos : {}", perfilAnexosDTO);
        if (perfilAnexosDTO.getId() != null) {
            throw new BadRequestAlertException("A new perfilAnexos cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PerfilAnexosDTO result = perfilAnexosService.save(perfilAnexosDTO);
        return ResponseEntity
            .created(new URI("/api/perfil-anexos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /perfil-anexos/:id} : Updates an existing perfilAnexos.
     *
     * @param id the id of the perfilAnexosDTO to save.
     * @param perfilAnexosDTO the perfilAnexosDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated perfilAnexosDTO,
     * or with status {@code 400 (Bad Request)} if the perfilAnexosDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the perfilAnexosDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/perfil-anexos/{id}")
    public ResponseEntity<PerfilAnexosDTO> updatePerfilAnexos(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PerfilAnexosDTO perfilAnexosDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PerfilAnexos : {}, {}", id, perfilAnexosDTO);
        if (perfilAnexosDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, perfilAnexosDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!perfilAnexosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PerfilAnexosDTO result = perfilAnexosService.update(perfilAnexosDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, perfilAnexosDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /perfil-anexos/:id} : Partial updates given fields of an existing perfilAnexos, field will ignore if it is null
     *
     * @param id the id of the perfilAnexosDTO to save.
     * @param perfilAnexosDTO the perfilAnexosDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated perfilAnexosDTO,
     * or with status {@code 400 (Bad Request)} if the perfilAnexosDTO is not valid,
     * or with status {@code 404 (Not Found)} if the perfilAnexosDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the perfilAnexosDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/perfil-anexos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PerfilAnexosDTO> partialUpdatePerfilAnexos(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PerfilAnexosDTO perfilAnexosDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PerfilAnexos partially : {}, {}", id, perfilAnexosDTO);
        if (perfilAnexosDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, perfilAnexosDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!perfilAnexosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PerfilAnexosDTO> result = perfilAnexosService.partialUpdate(perfilAnexosDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, perfilAnexosDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /perfil-anexos} : get all the perfilAnexos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of perfilAnexos in body.
     */
    @GetMapping("/perfil-anexos")
    public ResponseEntity<List<PerfilAnexosDTO>> getAllPerfilAnexos(
        PerfilAnexosCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get PerfilAnexos by criteria: {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        Page<PerfilAnexosDTO> page = perfilAnexosQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /perfil-anexos/count} : count all the perfilAnexos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/perfil-anexos/count")
    public ResponseEntity<Long> countPerfilAnexos(PerfilAnexosCriteria criteria) {
        log.debug("REST request to count PerfilAnexos by criteria: {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        return ResponseEntity.ok().body(perfilAnexosQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /perfil-anexos/:id} : get the "id" perfilAnexos.
     *
     * @param id the id of the perfilAnexosDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the perfilAnexosDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/perfil-anexos/{id}")
    public ResponseEntity<PerfilAnexosDTO> getPerfilAnexos(@PathVariable Long id) {
        log.debug("REST request to get PerfilAnexos : {}", id);
        Optional<PerfilAnexosDTO> perfilAnexosDTO = perfilAnexosService.findOne(id);
        return ResponseUtil.wrapOrNotFound(perfilAnexosDTO);
    }

    /**
     * {@code DELETE  /perfil-anexos/:id} : delete the "id" perfilAnexos.
     *
     * @param id the id of the perfilAnexosDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/perfil-anexos/{id}")
    public ResponseEntity<Void> deletePerfilAnexos(@PathVariable Long id) {
        log.debug("REST request to delete PerfilAnexos : {}", id);
        perfilAnexosService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}

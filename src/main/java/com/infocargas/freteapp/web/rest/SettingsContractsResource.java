package com.infocargas.freteapp.web.rest;

import com.infocargas.freteapp.repository.SettingsContractsRepository;
import com.infocargas.freteapp.service.SettingsContractsService;
import com.infocargas.freteapp.service.dto.SettingsContractsDTO;
import com.infocargas.freteapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.infocargas.freteapp.domain.SettingsContracts}.
 */
@RestController
@RequestMapping("/api")
public class SettingsContractsResource {

    private final Logger log = LoggerFactory.getLogger(SettingsContractsResource.class);

    private static final String ENTITY_NAME = "settingsContracts";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SettingsContractsService settingsContractsService;

    private final SettingsContractsRepository settingsContractsRepository;

    public SettingsContractsResource(
        SettingsContractsService settingsContractsService,
        SettingsContractsRepository settingsContractsRepository
    ) {
        this.settingsContractsService = settingsContractsService;
        this.settingsContractsRepository = settingsContractsRepository;
    }

    /**
     * {@code POST  /settings-contracts} : Create a new settingsContracts.
     *
     * @param settingsContractsDTO the settingsContractsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new settingsContractsDTO, or with status {@code 400 (Bad Request)} if the settingsContracts has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/settings-contracts")
    public ResponseEntity<SettingsContractsDTO> createSettingsContracts(@RequestBody SettingsContractsDTO settingsContractsDTO)
        throws URISyntaxException {
        log.debug("REST request to save SettingsContracts : {}", settingsContractsDTO);
        if (settingsContractsDTO.getId() != null) {
            throw new BadRequestAlertException("A new settingsContracts cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SettingsContractsDTO result = settingsContractsService.save(settingsContractsDTO);
        return ResponseEntity
            .created(new URI("/api/settings-contracts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /settings-contracts/:id} : Updates an existing settingsContracts.
     *
     * @param id the id of the settingsContractsDTO to save.
     * @param settingsContractsDTO the settingsContractsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated settingsContractsDTO,
     * or with status {@code 400 (Bad Request)} if the settingsContractsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the settingsContractsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping("/settings-contracts/{id}")
    public ResponseEntity<SettingsContractsDTO> updateSettingsContracts(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SettingsContractsDTO settingsContractsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SettingsContracts : {}, {}", id, settingsContractsDTO);
        if (settingsContractsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, settingsContractsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!settingsContractsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SettingsContractsDTO result = settingsContractsService.update(settingsContractsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, settingsContractsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /settings-contracts/:id} : Partial updates given fields of an existing settingsContracts, field will ignore if it is null
     *
     * @param id the id of the settingsContractsDTO to save.
     * @param settingsContractsDTO the settingsContractsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated settingsContractsDTO,
     * or with status {@code 400 (Bad Request)} if the settingsContractsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the settingsContractsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the settingsContractsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PatchMapping(value = "/settings-contracts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SettingsContractsDTO> partialUpdateSettingsContracts(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SettingsContractsDTO settingsContractsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SettingsContracts partially : {}, {}", id, settingsContractsDTO);
        if (settingsContractsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, settingsContractsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!settingsContractsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SettingsContractsDTO> result = settingsContractsService.partialUpdate(settingsContractsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, settingsContractsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /settings-contracts} : get all the settingsContracts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of settingsContracts in body.
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/settings-contracts")
    public List<SettingsContractsDTO> getAllSettingsContracts() {
        log.debug("REST request to get all SettingsContracts");
        return settingsContractsService.findAll();
    }

    /**
     * {@code GET  /settings-contracts/:id} : get the "id" settingsContracts.
     *
     * @param id the id of the settingsContractsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the settingsContractsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/settings-contracts/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<SettingsContractsDTO> getSettingsContracts(@PathVariable Long id) {
        log.debug("REST request to get SettingsContracts : {}", id);
        Optional<SettingsContractsDTO> settingsContractsDTO = settingsContractsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(settingsContractsDTO);
    }

    /**
     * {@code GET  /settings-contracts/:id} : get the "id" settingsContracts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the settingsContractsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/settings-contracts/contract")
    public ResponseEntity<SettingsContractsDTO> getSettingsContracts() {
        List<SettingsContractsDTO> contractsDTOS = settingsContractsService.findAll();

        if (contractsDTOS.size() > 0) {
            SettingsContractsDTO settingsContractsDTO = contractsDTOS.get(0);
            return ResponseEntity.ok(settingsContractsDTO);
        }

        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, null)).build();
    }

    /**
     * {@code DELETE  /settings-contracts/:id} : delete the "id" settingsContracts.
     *
     * @param id the id of the settingsContractsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @DeleteMapping("/settings-contracts/{id}")
    public ResponseEntity<Void> deleteSettingsContracts(@PathVariable Long id) {
        log.debug("REST request to delete SettingsContracts : {}", id);
        settingsContractsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}

package com.infocargas.freteapp.web.rest;

import com.infocargas.freteapp.repository.SettingsCargaAppRepository;
import com.infocargas.freteapp.service.SettingsCargaAppService;
import com.infocargas.freteapp.service.dto.SettingsCargaAppDTO;
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
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.infocargas.freteapp.domain.SettingsCargaApp}.
 */
@RestController
@RequestMapping("/api")
public class SettingsCargaAppResource {

    private final Logger log = LoggerFactory.getLogger(SettingsCargaAppResource.class);

    private static final String ENTITY_NAME = "settingsCargaApp";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SettingsCargaAppService settingsCargaAppService;

    private final SettingsCargaAppRepository settingsCargaAppRepository;

    public SettingsCargaAppResource(
        SettingsCargaAppService settingsCargaAppService,
        SettingsCargaAppRepository settingsCargaAppRepository
    ) {
        this.settingsCargaAppService = settingsCargaAppService;
        this.settingsCargaAppRepository = settingsCargaAppRepository;
    }

    /**
     * {@code POST  /settings-carga-apps} : Create a new settingsCargaApp.
     *
     * @param settingsCargaAppDTO the settingsCargaAppDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new settingsCargaAppDTO, or with status {@code 400 (Bad Request)} if the settingsCargaApp has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/settings-carga-apps")
    public ResponseEntity<SettingsCargaAppDTO> createSettingsCargaApp(@RequestBody SettingsCargaAppDTO settingsCargaAppDTO)
        throws URISyntaxException {
        log.debug("REST request to save SettingsCargaApp : {}", settingsCargaAppDTO);
        if (settingsCargaAppDTO.getId() != null) {
            throw new BadRequestAlertException("A new settingsCargaApp cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SettingsCargaAppDTO result = settingsCargaAppService.save(settingsCargaAppDTO);
        return ResponseEntity
            .created(new URI("/api/settings-carga-apps/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /settings-carga-apps/:id} : Updates an existing settingsCargaApp.
     *
     * @param id the id of the settingsCargaAppDTO to save.
     * @param settingsCargaAppDTO the settingsCargaAppDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated settingsCargaAppDTO,
     * or with status {@code 400 (Bad Request)} if the settingsCargaAppDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the settingsCargaAppDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/settings-carga-apps/{id}")
    public ResponseEntity<SettingsCargaAppDTO> updateSettingsCargaApp(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SettingsCargaAppDTO settingsCargaAppDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SettingsCargaApp : {}, {}", id, settingsCargaAppDTO);
        if (settingsCargaAppDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, settingsCargaAppDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!settingsCargaAppRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SettingsCargaAppDTO result = settingsCargaAppService.update(settingsCargaAppDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, settingsCargaAppDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /settings-carga-apps/:id} : Partial updates given fields of an existing settingsCargaApp, field will ignore if it is null
     *
     * @param id the id of the settingsCargaAppDTO to save.
     * @param settingsCargaAppDTO the settingsCargaAppDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated settingsCargaAppDTO,
     * or with status {@code 400 (Bad Request)} if the settingsCargaAppDTO is not valid,
     * or with status {@code 404 (Not Found)} if the settingsCargaAppDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the settingsCargaAppDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/settings-carga-apps/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SettingsCargaAppDTO> partialUpdateSettingsCargaApp(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SettingsCargaAppDTO settingsCargaAppDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SettingsCargaApp partially : {}, {}", id, settingsCargaAppDTO);
        if (settingsCargaAppDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, settingsCargaAppDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!settingsCargaAppRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SettingsCargaAppDTO> result = settingsCargaAppService.partialUpdate(settingsCargaAppDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, settingsCargaAppDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /settings-carga-apps} : get all the settingsCargaApps.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of settingsCargaApps in body.
     */
    @GetMapping("/settings-carga-apps")
    public List<SettingsCargaAppDTO> getAllSettingsCargaApps() {
        log.debug("REST request to get all SettingsCargaApps");
        return settingsCargaAppService.findAll();
    }

    /**
     * {@code GET  /settings-carga-apps/:id} : get the "id" settingsCargaApp.
     *
     * @param id the id of the settingsCargaAppDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the settingsCargaAppDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/settings-carga-apps/{id}")
    public ResponseEntity<SettingsCargaAppDTO> getSettingsCargaApp(@PathVariable Long id) {
        log.debug("REST request to get SettingsCargaApp : {}", id);
        Optional<SettingsCargaAppDTO> settingsCargaAppDTO = settingsCargaAppService.findOne(id);
        return ResponseUtil.wrapOrNotFound(settingsCargaAppDTO);
    }

    /**
     * {@code DELETE  /settings-carga-apps/:id} : delete the "id" settingsCargaApp.
     *
     * @param id the id of the settingsCargaAppDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/settings-carga-apps/{id}")
    public ResponseEntity<Void> deleteSettingsCargaApp(@PathVariable Long id) {
        log.debug("REST request to delete SettingsCargaApp : {}", id);
        settingsCargaAppService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}

package com.infocargas.freteapp.web.rest;

import com.infocargas.freteapp.repository.PerfilRepository;
import com.infocargas.freteapp.security.SecurityUtils;
import com.infocargas.freteapp.service.PerfilQueryService;
import com.infocargas.freteapp.service.PerfilService;
import com.infocargas.freteapp.service.UserService;
import com.infocargas.freteapp.service.criteria.PerfilCriteria;
import com.infocargas.freteapp.service.dto.PerfilDTO;
import com.infocargas.freteapp.service.dto.UserDTO;
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
 * REST controller for managing {@link com.infocargas.freteapp.domain.Perfil}.
 */
@RestController
@RequestMapping("/api")
public class PerfilResource {

    private final Logger log = LoggerFactory.getLogger(PerfilResource.class);

    private static final String ENTITY_NAME = "perfil";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PerfilService perfilService;

    private final PerfilRepository perfilRepository;

    private final PerfilQueryService perfilQueryService;

    private final UserService userService;

    public PerfilResource(PerfilService perfilService, PerfilRepository perfilRepository, PerfilQueryService perfilQueryService,
                          UserService userService) {
        this.perfilService = perfilService;
        this.perfilRepository = perfilRepository;
        this.perfilQueryService = perfilQueryService;
        this.userService = userService;
    }

    /**
     * {@code POST  /perfils} : Create a new perfil.
     *
     * @param perfilDTO the perfilDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new perfilDTO, or with status {@code 400 (Bad Request)} if the perfil has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/perfils")
    public ResponseEntity<PerfilDTO> createPerfil(@Valid @RequestBody PerfilDTO perfilDTO) throws URISyntaxException {
        log.debug("REST request to save Perfil : {}", perfilDTO);
        if (perfilDTO.getId() != null) {
            throw new BadRequestAlertException("A new perfil cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PerfilDTO result = perfilService.save(perfilDTO);
        return ResponseEntity
            .created(new URI("/api/perfils/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("/perfils/createProfile")
    public ResponseEntity<PerfilDTO> createProfile(@Valid @RequestBody PerfilDTO perfilDTO) throws URISyntaxException {
        log.debug("REST request to save new Profile : {}", perfilDTO);
        if (perfilDTO.getId() != null) {
            throw new BadRequestAlertException("A new perfil cannot already have an ID", ENTITY_NAME, "idexists");
        }

        UserDTO userDTO = userService.getUserWithAuthoritiesToDto();

        if (userDTO == null){
            throw new BadRequestAlertException("Usuário corrente não encontrado", ENTITY_NAME, "idexists");
        }

        perfilDTO.setUser(userDTO);
        perfilDTO.setPais("br");

        PerfilDTO result = perfilService.save(perfilDTO);
        return ResponseEntity
            .created(new URI("/api/perfils/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /perfils/:id} : Updates an existing perfil.
     *
     * @param id the id of the perfilDTO to save.
     * @param perfilDTO the perfilDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated perfilDTO,
     * or with status {@code 400 (Bad Request)} if the perfilDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the perfilDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/perfils/{id}")
    public ResponseEntity<PerfilDTO> updatePerfil(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PerfilDTO perfilDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Perfil : {}, {}", id, perfilDTO);
        if (perfilDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, perfilDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!perfilRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PerfilDTO result = perfilService.update(perfilDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, perfilDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /perfils/:id} : Partial updates given fields of an existing perfil, field will ignore if it is null
     *
     * @param id the id of the perfilDTO to save.
     * @param perfilDTO the perfilDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated perfilDTO,
     * or with status {@code 400 (Bad Request)} if the perfilDTO is not valid,
     * or with status {@code 404 (Not Found)} if the perfilDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the perfilDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/perfils/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PerfilDTO> partialUpdatePerfil(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PerfilDTO perfilDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Perfil partially : {}, {}", id, perfilDTO);
        if (perfilDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, perfilDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!perfilRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PerfilDTO> result = perfilService.partialUpdate(perfilDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, perfilDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /perfils} : get all the perfils.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of perfils in body.
     */
    @GetMapping("/perfils")
    public ResponseEntity<List<PerfilDTO>> getAllPerfils(
        PerfilCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Perfils by criteria: {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        Page<PerfilDTO> page = perfilQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /perfils/count} : count all the perfils.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/perfils/count")
    public ResponseEntity<Long> countPerfils(PerfilCriteria criteria) {
        log.debug("REST request to count Perfils by criteria: {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        return ResponseEntity.ok().body(perfilQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /perfils/:id} : get the "id" perfil.
     *
     * @param id the id of the perfilDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the perfilDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/perfils/{id}")
    public ResponseEntity<PerfilDTO> getPerfil(@PathVariable Long id) {
        log.debug("REST request to get Perfil : {}", id);
        Optional<PerfilDTO> perfilDTO = perfilService.findOne(id);
        return ResponseUtil.wrapOrNotFound(perfilDTO);
    }

    /**
     * {@code GET  /perfils/:id} : get the "id" perfil.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the perfilDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/perfils/current")
    public ResponseEntity<PerfilDTO> getCurrentUserPerfil() {

        String userLogin = SecurityUtils
            .getCurrentUserLogin()
            .orElseThrow(() -> new RuntimeException("Current user login not found"));

        Optional<PerfilDTO> perfilDTO = perfilService.findByUser(userLogin);
        return ResponseUtil.wrapOrNotFound(perfilDTO);
    }

    /**
     * {@code DELETE  /perfils/:id} : delete the "id" perfil.
     *
     * @param id the id of the perfilDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/perfils/{id}")
    public ResponseEntity<Void> deletePerfil(@PathVariable Long id) {
        log.debug("REST request to delete Perfil : {}", id);
        perfilService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}

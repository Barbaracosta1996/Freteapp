package com.infocargas.freteapp.web.rest;

import com.infocargas.freteapp.domain.enumeration.StatusOferta;
import com.infocargas.freteapp.repository.OfertasRepository;
import com.infocargas.freteapp.service.*;
import com.infocargas.freteapp.service.criteria.OfertasCriteria;
import com.infocargas.freteapp.service.dto.OfertasDTO;
import com.infocargas.freteapp.service.dto.PerfilDTO;
import com.infocargas.freteapp.service.dto.UserDTO;
import com.infocargas.freteapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.infocargas.freteapp.domain.Ofertas}.
 */
@RestController
@RequestMapping("/api")
public class OfertasResource {

    private final Logger log = LoggerFactory.getLogger(OfertasResource.class);

    private static final String ENTITY_NAME = "ofertas";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OfertasService ofertasService;

    private final OfertasRepository ofertasRepository;

    private final OfertasQueryService ofertasQueryService;

    private final RotasOfertasService rotasOfertasService;

    private final UserService userService;

    private final PerfilService perfilService;

    public OfertasResource(
        OfertasService ofertasService,
        OfertasRepository ofertasRepository,
        OfertasQueryService ofertasQueryService,
        RotasOfertasService rotasOfertasService,
        UserService userService,
        PerfilService perfilService
    ) {
        this.ofertasService = ofertasService;
        this.ofertasRepository = ofertasRepository;
        this.ofertasQueryService = ofertasQueryService;
        this.rotasOfertasService = rotasOfertasService;
        this.userService = userService;
        this.perfilService = perfilService;
    }

    /**
     * {@code POST  /ofertas} : Create a new ofertas.
     *
     * @param ofertasDTO the ofertasDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ofertasDTO, or with status {@code 400 (Bad Request)} if the ofertas has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ofertas")
    public ResponseEntity<OfertasDTO> createOfertas(@Valid @RequestBody OfertasDTO ofertasDTO) throws URISyntaxException {
        log.debug("REST request to save Ofertas : {}", ofertasDTO);
        if (ofertasDTO.getId() != null) {
            throw new BadRequestAlertException("A new ofertas cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OfertasDTO result = ofertasService.save(ofertasDTO);
        return ResponseEntity
            .created(new URI("/api/ofertas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code POST  /ofertas} : Create a new ofertas.
     *
     * @param ofertasDTO the ofertasDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ofertasDTO, or with status {@code 400 (Bad Request)} if the ofertas has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ofertas/portal")
    public ResponseEntity<OfertasDTO> createOfertasPortal(@RequestBody OfertasDTO ofertasDTO) throws URISyntaxException {
        log.debug("REST request to save Ofertas do Portal : {}", ofertasDTO);
        if (ofertasDTO.getId() != null) {
            throw new BadRequestAlertException("A new ofertas cannot already have an ID", ENTITY_NAME, "idexists");
        }

        UserDTO userDTO = userService.getUserWithAuthoritiesToDto();
        PerfilDTO perfilDTO = perfilService.findByUserDTO(userDTO.getLogin());

        ofertasDTO.setPerfil(perfilDTO);
        ofertasDTO.setDataPostagem(ZonedDateTime.now());
        ofertasDTO.setStatus(StatusOferta.AGUARDANDO_PROPOSTA);

        OfertasDTO result = ofertasService.createPortal(ofertasDTO);

        if (result.getId() != null) {
            rotasOfertasService.saveNewRoute(result);
        }

        return ResponseEntity
            .created(new URI("/api/ofertas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/ofertas/portal/admin")
    public ResponseEntity<OfertasDTO> createOfertasAdmin(@RequestBody OfertasDTO ofertasDTO) throws URISyntaxException {
        log.debug("REST request to save Ofertas do Portal : {}", ofertasDTO);
        if (ofertasDTO.getId() != null) {
            throw new BadRequestAlertException("A new ofertas cannot already have an ID", ENTITY_NAME, "idexists");
        }

        //        UserDTO userDTO = userService.getUserWithAuthoritiesByLogin(ofertasDTO.getPerfil().getUser().getLogin());
        //        PerfilDTO perfilDTO = perfilService.findByUserDTO();

        //        ofertasDTO.setPerfil();
        ofertasDTO.setDataPostagem(ZonedDateTime.now());
        ofertasDTO.setStatus(StatusOferta.AGUARDANDO_PROPOSTA);

        OfertasDTO result = ofertasService.createPortal(ofertasDTO);

        return ResponseEntity
            .created(new URI("/api/ofertas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ofertas/:id} : Updates an existing ofertas.
     *
     * @param id the id of the ofertasDTO to save.
     * @param ofertasDTO the ofertasDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ofertasDTO,
     * or with status {@code 400 (Bad Request)} if the ofertasDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ofertasDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ofertas/portal/{id}")
    public ResponseEntity<OfertasDTO> updateOfertasPortal(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OfertasDTO ofertasDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Ofertas : {}, {}", id, ofertasDTO);
        if (ofertasDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ofertasDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ofertasRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OfertasDTO result = ofertasService.updatePortal(ofertasDTO);

        rotasOfertasService.updateRoute(result);

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, ofertasDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ofertas/:id} : Updates an existing ofertas.
     *
     * @param id the id of the ofertasDTO to save.
     * @param ofertasDTO the ofertasDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ofertasDTO,
     * or with status {@code 400 (Bad Request)} if the ofertasDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ofertasDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ofertas/{id}")
    public ResponseEntity<OfertasDTO> updateOfertas(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OfertasDTO ofertasDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Ofertas : {}, {}", id, ofertasDTO);
        if (ofertasDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ofertasDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ofertasRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OfertasDTO result = ofertasService.update(ofertasDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, ofertasDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ofertas/:id} : Partial updates given fields of an existing ofertas, field will ignore if it is null
     *
     * @param id the id of the ofertasDTO to save.
     * @param ofertasDTO the ofertasDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ofertasDTO,
     * or with status {@code 400 (Bad Request)} if the ofertasDTO is not valid,
     * or with status {@code 404 (Not Found)} if the ofertasDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the ofertasDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ofertas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OfertasDTO> partialUpdateOfertas(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OfertasDTO ofertasDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Ofertas partially : {}, {}", id, ofertasDTO);
        if (ofertasDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ofertasDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ofertasRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OfertasDTO> result = ofertasService.partialUpdate(ofertasDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, ofertasDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ofertas} : get all the ofertas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ofertas in body.
     */
    @GetMapping("/ofertas")
    public ResponseEntity<List<OfertasDTO>> getAllOfertas(
        OfertasCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Ofertas by criteria: {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        Page<OfertasDTO> page = ofertasQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ofertas/count} : count all the ofertas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/ofertas/count")
    public ResponseEntity<Long> countOfertas(OfertasCriteria criteria) {
        log.debug("REST request to count Ofertas by criteria: {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        return ResponseEntity.ok().body(ofertasQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /ofertas/:id} : get the "id" ofertas.
     *
     * @param id the id of the ofertasDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ofertasDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ofertas/{id}")
    public ResponseEntity<OfertasDTO> getOfertas(@PathVariable Long id) {
        Optional<OfertasDTO> ofertasDTO = ofertasService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ofertasDTO);
    }

    /**
     * {@code DELETE  /ofertas/:id} : delete the "id" ofertas.
     *
     * @param id the id of the ofertasDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ofertas/{id}")
    public ResponseEntity<Void> deleteOfertas(@PathVariable Long id) {
        ofertasService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}

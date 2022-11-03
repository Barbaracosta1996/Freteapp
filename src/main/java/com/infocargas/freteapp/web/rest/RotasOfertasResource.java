package com.infocargas.freteapp.web.rest;

import com.google.gson.Gson;
import com.infocargas.freteapp.domain.enumeration.StatusOferta;
import com.infocargas.freteapp.domain.enumeration.TipoOferta;
import com.infocargas.freteapp.repository.RotasOfertasRepository;
import com.infocargas.freteapp.service.OfertasService;
import com.infocargas.freteapp.service.RotasOfertasQueryService;
import com.infocargas.freteapp.service.RotasOfertasService;
import com.infocargas.freteapp.service.criteria.RotasOfertasCriteria;
import com.infocargas.freteapp.service.dto.OfertasDTO;
import com.infocargas.freteapp.service.dto.RotasOfertasDTO;
import com.infocargas.freteapp.service.dto.routes.GoogleLegs;
import com.infocargas.freteapp.service.dto.routes.GoogleRoutes;
import com.infocargas.freteapp.utils.GeoUtils;
import com.infocargas.freteapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
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
 * REST controller for managing {@link com.infocargas.freteapp.domain.RotasOfertas}.
 */
@RestController
@RequestMapping("/api")
public class RotasOfertasResource {

    private final Logger log = LoggerFactory.getLogger(RotasOfertasResource.class);

    private static final String ENTITY_NAME = "rotasOfertas";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RotasOfertasService rotasOfertasService;

    private final RotasOfertasRepository rotasOfertasRepository;

    private final RotasOfertasQueryService rotasOfertasQueryService;

    public RotasOfertasResource(
        RotasOfertasService rotasOfertasService,
        RotasOfertasRepository rotasOfertasRepository,
        RotasOfertasQueryService rotasOfertasQueryService,
        OfertasService ofertasService
    ) {
        this.rotasOfertasService = rotasOfertasService;
        this.rotasOfertasRepository = rotasOfertasRepository;
        this.rotasOfertasQueryService = rotasOfertasQueryService;
    }

    /**
     * {@code POST  /rotas-ofertas} : Create a new rotasOfertas.
     *
     * @param rotasOfertasDTO the rotasOfertasDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rotasOfertasDTO, or with status {@code 400 (Bad Request)} if the rotasOfertas has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rotas-ofertas")
    public ResponseEntity<RotasOfertasDTO> createRotasOfertas(@Valid @RequestBody RotasOfertasDTO rotasOfertasDTO)
        throws URISyntaxException {
        log.debug("REST request to save RotasOfertas : {}", rotasOfertasDTO);
        if (rotasOfertasDTO.getId() != null) {
            throw new BadRequestAlertException("A new rotasOfertas cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RotasOfertasDTO result = rotasOfertasService.save(rotasOfertasDTO);
        return ResponseEntity
            .created(new URI("/api/rotas-ofertas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rotas-ofertas/:id} : Updates an existing rotasOfertas.
     *
     * @param id the id of the rotasOfertasDTO to save.
     * @param rotasOfertasDTO the rotasOfertasDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rotasOfertasDTO,
     * or with status {@code 400 (Bad Request)} if the rotasOfertasDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rotasOfertasDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rotas-ofertas/{id}")
    public ResponseEntity<RotasOfertasDTO> updateRotasOfertas(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RotasOfertasDTO rotasOfertasDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RotasOfertas : {}, {}", id, rotasOfertasDTO);
        if (rotasOfertasDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rotasOfertasDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rotasOfertasRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RotasOfertasDTO result = rotasOfertasService.update(rotasOfertasDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, rotasOfertasDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /rotas-ofertas/:id} : Partial updates given fields of an existing rotasOfertas, field will ignore if it is null
     *
     * @param id the id of the rotasOfertasDTO to save.
     * @param rotasOfertasDTO the rotasOfertasDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rotasOfertasDTO,
     * or with status {@code 400 (Bad Request)} if the rotasOfertasDTO is not valid,
     * or with status {@code 404 (Not Found)} if the rotasOfertasDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the rotasOfertasDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/rotas-ofertas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RotasOfertasDTO> partialUpdateRotasOfertas(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RotasOfertasDTO rotasOfertasDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RotasOfertas partially : {}, {}", id, rotasOfertasDTO);
        if (rotasOfertasDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rotasOfertasDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rotasOfertasRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RotasOfertasDTO> result = rotasOfertasService.partialUpdate(rotasOfertasDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, rotasOfertasDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /rotas-ofertas} : get all the rotasOfertas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rotasOfertas in body.
     */
    @GetMapping("/rotas-ofertas")
    public ResponseEntity<List<RotasOfertasDTO>> getAllRotasOfertas(
        RotasOfertasCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get RotasOfertas by criteria: {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        Page<RotasOfertasDTO> page = rotasOfertasQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rotas-ofertas/count} : count all the rotasOfertas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/rotas-ofertas/count")
    public ResponseEntity<Long> countRotasOfertas(RotasOfertasCriteria criteria) {
        log.debug("REST request to count RotasOfertas by criteria: {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        return ResponseEntity.ok().body(rotasOfertasQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /rotas-ofertas/:id} : get the "id" rotasOfertas.
     *
     * @param id the id of the rotasOfertasDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rotasOfertasDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rotas-ofertas/{id}")
    public ResponseEntity<RotasOfertasDTO> getRotasOfertas(@PathVariable Long id) {
        log.debug("REST request to get RotasOfertas : {}", id);
        Optional<RotasOfertasDTO> rotasOfertasDTO = rotasOfertasService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rotasOfertasDTO);
    }

    /**
     * {@code DELETE  /rotas-ofertas/:id} : delete the "id" rotasOfertas.
     *
     * @param id the id of the rotasOfertasDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rotas-ofertas/{id}")
    public ResponseEntity<Void> deleteRotasOfertas(@PathVariable Long id) {
        log.debug("REST request to delete RotasOfertas : {}", id);
        rotasOfertasService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}

package com.infocargas.freteapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.infocargas.freteapp.IntegrationTest;
import com.infocargas.freteapp.domain.WhatsMessageBatch;
import com.infocargas.freteapp.domain.enumeration.TipoOferta;
import com.infocargas.freteapp.domain.enumeration.WhatsAppType;
import com.infocargas.freteapp.domain.enumeration.WhatsStatus;
import com.infocargas.freteapp.repository.WhatsMessageBatchRepository;
import com.infocargas.freteapp.service.criteria.WhatsMessageBatchCriteria;
import com.infocargas.freteapp.service.dto.WhatsMessageBatchDTO;
import com.infocargas.freteapp.service.mapper.WhatsMessageBatchMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link WhatsMessageBatchResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WhatsMessageBatchResourceIT {

    private static final WhatsAppType DEFAULT_TIPO = WhatsAppType.INDICATION_ALERT;
    private static final WhatsAppType UPDATED_TIPO = WhatsAppType.LIST_ALERT;

    private static final String DEFAULT_WAID_TO = "AAAAAAAAAA";
    private static final String UPDATED_WAID_TO = "BBBBBBBBBB";

    private static final Integer DEFAULT_PERFIL_ID = 1;
    private static final Integer UPDATED_PERFIL_ID = 2;
    private static final Integer SMALLER_PERFIL_ID = 1 - 1;

    private static final WhatsStatus DEFAULT_STATUS = WhatsStatus.OPEN;
    private static final WhatsStatus UPDATED_STATUS = WhatsStatus.CLOSED;

    private static final Long DEFAULT_OFERTA_ID = 1L;
    private static final Long UPDATED_OFERTA_ID = 2L;
    private static final Long SMALLER_OFERTA_ID = 1L - 1L;

    private static final TipoOferta DEFAULT_TIPO_OFERTA = TipoOferta.CARGA;
    private static final TipoOferta UPDATED_TIPO_OFERTA = TipoOferta.VAGAS;

    private static final String ENTITY_API_URL = "/api/whats-message-batches";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WhatsMessageBatchRepository whatsMessageBatchRepository;

    @Autowired
    private WhatsMessageBatchMapper whatsMessageBatchMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWhatsMessageBatchMockMvc;

    private WhatsMessageBatch whatsMessageBatch;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WhatsMessageBatch createEntity(EntityManager em) {
        WhatsMessageBatch whatsMessageBatch = new WhatsMessageBatch()
            .tipo(DEFAULT_TIPO)
            .waidTo(DEFAULT_WAID_TO)
            .perfilID(DEFAULT_PERFIL_ID)
            .status(DEFAULT_STATUS)
            .ofertaId(DEFAULT_OFERTA_ID)
            .tipoOferta(DEFAULT_TIPO_OFERTA);
        return whatsMessageBatch;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WhatsMessageBatch createUpdatedEntity(EntityManager em) {
        WhatsMessageBatch whatsMessageBatch = new WhatsMessageBatch()
            .tipo(UPDATED_TIPO)
            .waidTo(UPDATED_WAID_TO)
            .perfilID(UPDATED_PERFIL_ID)
            .status(UPDATED_STATUS)
            .ofertaId(UPDATED_OFERTA_ID)
            .tipoOferta(UPDATED_TIPO_OFERTA);
        return whatsMessageBatch;
    }

    @BeforeEach
    public void initTest() {
        whatsMessageBatch = createEntity(em);
    }

    @Test
    @Transactional
    void createWhatsMessageBatch() throws Exception {
        int databaseSizeBeforeCreate = whatsMessageBatchRepository.findAll().size();
        // Create the WhatsMessageBatch
        WhatsMessageBatchDTO whatsMessageBatchDTO = whatsMessageBatchMapper.toDto(whatsMessageBatch);
        restWhatsMessageBatchMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(whatsMessageBatchDTO))
            )
            .andExpect(status().isCreated());

        // Validate the WhatsMessageBatch in the database
        List<WhatsMessageBatch> whatsMessageBatchList = whatsMessageBatchRepository.findAll();
        assertThat(whatsMessageBatchList).hasSize(databaseSizeBeforeCreate + 1);
        WhatsMessageBatch testWhatsMessageBatch = whatsMessageBatchList.get(whatsMessageBatchList.size() - 1);
        assertThat(testWhatsMessageBatch.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testWhatsMessageBatch.getWaidTo()).isEqualTo(DEFAULT_WAID_TO);
        assertThat(testWhatsMessageBatch.getPerfilID()).isEqualTo(DEFAULT_PERFIL_ID);
        assertThat(testWhatsMessageBatch.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testWhatsMessageBatch.getOfertaId()).isEqualTo(DEFAULT_OFERTA_ID);
        assertThat(testWhatsMessageBatch.getTipoOferta()).isEqualTo(DEFAULT_TIPO_OFERTA);
    }

    @Test
    @Transactional
    void createWhatsMessageBatchWithExistingId() throws Exception {
        // Create the WhatsMessageBatch with an existing ID
        whatsMessageBatch.setId(1L);
        WhatsMessageBatchDTO whatsMessageBatchDTO = whatsMessageBatchMapper.toDto(whatsMessageBatch);

        int databaseSizeBeforeCreate = whatsMessageBatchRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWhatsMessageBatchMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(whatsMessageBatchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WhatsMessageBatch in the database
        List<WhatsMessageBatch> whatsMessageBatchList = whatsMessageBatchRepository.findAll();
        assertThat(whatsMessageBatchList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTipoIsRequired() throws Exception {
        int databaseSizeBeforeTest = whatsMessageBatchRepository.findAll().size();
        // set the field null
        whatsMessageBatch.setTipo(null);

        // Create the WhatsMessageBatch, which fails.
        WhatsMessageBatchDTO whatsMessageBatchDTO = whatsMessageBatchMapper.toDto(whatsMessageBatch);

        restWhatsMessageBatchMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(whatsMessageBatchDTO))
            )
            .andExpect(status().isBadRequest());

        List<WhatsMessageBatch> whatsMessageBatchList = whatsMessageBatchRepository.findAll();
        assertThat(whatsMessageBatchList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkWaidToIsRequired() throws Exception {
        int databaseSizeBeforeTest = whatsMessageBatchRepository.findAll().size();
        // set the field null
        whatsMessageBatch.setWaidTo(null);

        // Create the WhatsMessageBatch, which fails.
        WhatsMessageBatchDTO whatsMessageBatchDTO = whatsMessageBatchMapper.toDto(whatsMessageBatch);

        restWhatsMessageBatchMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(whatsMessageBatchDTO))
            )
            .andExpect(status().isBadRequest());

        List<WhatsMessageBatch> whatsMessageBatchList = whatsMessageBatchRepository.findAll();
        assertThat(whatsMessageBatchList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPerfilIDIsRequired() throws Exception {
        int databaseSizeBeforeTest = whatsMessageBatchRepository.findAll().size();
        // set the field null
        whatsMessageBatch.setPerfilID(null);

        // Create the WhatsMessageBatch, which fails.
        WhatsMessageBatchDTO whatsMessageBatchDTO = whatsMessageBatchMapper.toDto(whatsMessageBatch);

        restWhatsMessageBatchMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(whatsMessageBatchDTO))
            )
            .andExpect(status().isBadRequest());

        List<WhatsMessageBatch> whatsMessageBatchList = whatsMessageBatchRepository.findAll();
        assertThat(whatsMessageBatchList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = whatsMessageBatchRepository.findAll().size();
        // set the field null
        whatsMessageBatch.setStatus(null);

        // Create the WhatsMessageBatch, which fails.
        WhatsMessageBatchDTO whatsMessageBatchDTO = whatsMessageBatchMapper.toDto(whatsMessageBatch);

        restWhatsMessageBatchMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(whatsMessageBatchDTO))
            )
            .andExpect(status().isBadRequest());

        List<WhatsMessageBatch> whatsMessageBatchList = whatsMessageBatchRepository.findAll();
        assertThat(whatsMessageBatchList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWhatsMessageBatches() throws Exception {
        // Initialize the database
        whatsMessageBatchRepository.saveAndFlush(whatsMessageBatch);

        // Get all the whatsMessageBatchList
        restWhatsMessageBatchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(whatsMessageBatch.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].waidTo").value(hasItem(DEFAULT_WAID_TO)))
            .andExpect(jsonPath("$.[*].perfilID").value(hasItem(DEFAULT_PERFIL_ID)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].ofertaId").value(hasItem(DEFAULT_OFERTA_ID.intValue())))
            .andExpect(jsonPath("$.[*].tipoOferta").value(hasItem(DEFAULT_TIPO_OFERTA.toString())));
    }

    @Test
    @Transactional
    void getWhatsMessageBatch() throws Exception {
        // Initialize the database
        whatsMessageBatchRepository.saveAndFlush(whatsMessageBatch);

        // Get the whatsMessageBatch
        restWhatsMessageBatchMockMvc
            .perform(get(ENTITY_API_URL_ID, whatsMessageBatch.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(whatsMessageBatch.getId().intValue()))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO.toString()))
            .andExpect(jsonPath("$.waidTo").value(DEFAULT_WAID_TO))
            .andExpect(jsonPath("$.perfilID").value(DEFAULT_PERFIL_ID))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.ofertaId").value(DEFAULT_OFERTA_ID.intValue()))
            .andExpect(jsonPath("$.tipoOferta").value(DEFAULT_TIPO_OFERTA.toString()));
    }

    @Test
    @Transactional
    void getWhatsMessageBatchesByIdFiltering() throws Exception {
        // Initialize the database
        whatsMessageBatchRepository.saveAndFlush(whatsMessageBatch);

        Long id = whatsMessageBatch.getId();

        defaultWhatsMessageBatchShouldBeFound("id.equals=" + id);
        defaultWhatsMessageBatchShouldNotBeFound("id.notEquals=" + id);

        defaultWhatsMessageBatchShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultWhatsMessageBatchShouldNotBeFound("id.greaterThan=" + id);

        defaultWhatsMessageBatchShouldBeFound("id.lessThanOrEqual=" + id);
        defaultWhatsMessageBatchShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllWhatsMessageBatchesByTipoIsEqualToSomething() throws Exception {
        // Initialize the database
        whatsMessageBatchRepository.saveAndFlush(whatsMessageBatch);

        // Get all the whatsMessageBatchList where tipo equals to DEFAULT_TIPO
        defaultWhatsMessageBatchShouldBeFound("tipo.equals=" + DEFAULT_TIPO);

        // Get all the whatsMessageBatchList where tipo equals to UPDATED_TIPO
        defaultWhatsMessageBatchShouldNotBeFound("tipo.equals=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllWhatsMessageBatchesByTipoIsInShouldWork() throws Exception {
        // Initialize the database
        whatsMessageBatchRepository.saveAndFlush(whatsMessageBatch);

        // Get all the whatsMessageBatchList where tipo in DEFAULT_TIPO or UPDATED_TIPO
        defaultWhatsMessageBatchShouldBeFound("tipo.in=" + DEFAULT_TIPO + "," + UPDATED_TIPO);

        // Get all the whatsMessageBatchList where tipo equals to UPDATED_TIPO
        defaultWhatsMessageBatchShouldNotBeFound("tipo.in=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllWhatsMessageBatchesByTipoIsNullOrNotNull() throws Exception {
        // Initialize the database
        whatsMessageBatchRepository.saveAndFlush(whatsMessageBatch);

        // Get all the whatsMessageBatchList where tipo is not null
        defaultWhatsMessageBatchShouldBeFound("tipo.specified=true");

        // Get all the whatsMessageBatchList where tipo is null
        defaultWhatsMessageBatchShouldNotBeFound("tipo.specified=false");
    }

    @Test
    @Transactional
    void getAllWhatsMessageBatchesByWaidToIsEqualToSomething() throws Exception {
        // Initialize the database
        whatsMessageBatchRepository.saveAndFlush(whatsMessageBatch);

        // Get all the whatsMessageBatchList where waidTo equals to DEFAULT_WAID_TO
        defaultWhatsMessageBatchShouldBeFound("waidTo.equals=" + DEFAULT_WAID_TO);

        // Get all the whatsMessageBatchList where waidTo equals to UPDATED_WAID_TO
        defaultWhatsMessageBatchShouldNotBeFound("waidTo.equals=" + UPDATED_WAID_TO);
    }

    @Test
    @Transactional
    void getAllWhatsMessageBatchesByWaidToIsInShouldWork() throws Exception {
        // Initialize the database
        whatsMessageBatchRepository.saveAndFlush(whatsMessageBatch);

        // Get all the whatsMessageBatchList where waidTo in DEFAULT_WAID_TO or UPDATED_WAID_TO
        defaultWhatsMessageBatchShouldBeFound("waidTo.in=" + DEFAULT_WAID_TO + "," + UPDATED_WAID_TO);

        // Get all the whatsMessageBatchList where waidTo equals to UPDATED_WAID_TO
        defaultWhatsMessageBatchShouldNotBeFound("waidTo.in=" + UPDATED_WAID_TO);
    }

    @Test
    @Transactional
    void getAllWhatsMessageBatchesByWaidToIsNullOrNotNull() throws Exception {
        // Initialize the database
        whatsMessageBatchRepository.saveAndFlush(whatsMessageBatch);

        // Get all the whatsMessageBatchList where waidTo is not null
        defaultWhatsMessageBatchShouldBeFound("waidTo.specified=true");

        // Get all the whatsMessageBatchList where waidTo is null
        defaultWhatsMessageBatchShouldNotBeFound("waidTo.specified=false");
    }

    @Test
    @Transactional
    void getAllWhatsMessageBatchesByWaidToContainsSomething() throws Exception {
        // Initialize the database
        whatsMessageBatchRepository.saveAndFlush(whatsMessageBatch);

        // Get all the whatsMessageBatchList where waidTo contains DEFAULT_WAID_TO
        defaultWhatsMessageBatchShouldBeFound("waidTo.contains=" + DEFAULT_WAID_TO);

        // Get all the whatsMessageBatchList where waidTo contains UPDATED_WAID_TO
        defaultWhatsMessageBatchShouldNotBeFound("waidTo.contains=" + UPDATED_WAID_TO);
    }

    @Test
    @Transactional
    void getAllWhatsMessageBatchesByWaidToNotContainsSomething() throws Exception {
        // Initialize the database
        whatsMessageBatchRepository.saveAndFlush(whatsMessageBatch);

        // Get all the whatsMessageBatchList where waidTo does not contain DEFAULT_WAID_TO
        defaultWhatsMessageBatchShouldNotBeFound("waidTo.doesNotContain=" + DEFAULT_WAID_TO);

        // Get all the whatsMessageBatchList where waidTo does not contain UPDATED_WAID_TO
        defaultWhatsMessageBatchShouldBeFound("waidTo.doesNotContain=" + UPDATED_WAID_TO);
    }

    @Test
    @Transactional
    void getAllWhatsMessageBatchesByPerfilIDIsEqualToSomething() throws Exception {
        // Initialize the database
        whatsMessageBatchRepository.saveAndFlush(whatsMessageBatch);

        // Get all the whatsMessageBatchList where perfilID equals to DEFAULT_PERFIL_ID
        defaultWhatsMessageBatchShouldBeFound("perfilID.equals=" + DEFAULT_PERFIL_ID);

        // Get all the whatsMessageBatchList where perfilID equals to UPDATED_PERFIL_ID
        defaultWhatsMessageBatchShouldNotBeFound("perfilID.equals=" + UPDATED_PERFIL_ID);
    }

    @Test
    @Transactional
    void getAllWhatsMessageBatchesByPerfilIDIsInShouldWork() throws Exception {
        // Initialize the database
        whatsMessageBatchRepository.saveAndFlush(whatsMessageBatch);

        // Get all the whatsMessageBatchList where perfilID in DEFAULT_PERFIL_ID or UPDATED_PERFIL_ID
        defaultWhatsMessageBatchShouldBeFound("perfilID.in=" + DEFAULT_PERFIL_ID + "," + UPDATED_PERFIL_ID);

        // Get all the whatsMessageBatchList where perfilID equals to UPDATED_PERFIL_ID
        defaultWhatsMessageBatchShouldNotBeFound("perfilID.in=" + UPDATED_PERFIL_ID);
    }

    @Test
    @Transactional
    void getAllWhatsMessageBatchesByPerfilIDIsNullOrNotNull() throws Exception {
        // Initialize the database
        whatsMessageBatchRepository.saveAndFlush(whatsMessageBatch);

        // Get all the whatsMessageBatchList where perfilID is not null
        defaultWhatsMessageBatchShouldBeFound("perfilID.specified=true");

        // Get all the whatsMessageBatchList where perfilID is null
        defaultWhatsMessageBatchShouldNotBeFound("perfilID.specified=false");
    }

    @Test
    @Transactional
    void getAllWhatsMessageBatchesByPerfilIDIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        whatsMessageBatchRepository.saveAndFlush(whatsMessageBatch);

        // Get all the whatsMessageBatchList where perfilID is greater than or equal to DEFAULT_PERFIL_ID
        defaultWhatsMessageBatchShouldBeFound("perfilID.greaterThanOrEqual=" + DEFAULT_PERFIL_ID);

        // Get all the whatsMessageBatchList where perfilID is greater than or equal to UPDATED_PERFIL_ID
        defaultWhatsMessageBatchShouldNotBeFound("perfilID.greaterThanOrEqual=" + UPDATED_PERFIL_ID);
    }

    @Test
    @Transactional
    void getAllWhatsMessageBatchesByPerfilIDIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        whatsMessageBatchRepository.saveAndFlush(whatsMessageBatch);

        // Get all the whatsMessageBatchList where perfilID is less than or equal to DEFAULT_PERFIL_ID
        defaultWhatsMessageBatchShouldBeFound("perfilID.lessThanOrEqual=" + DEFAULT_PERFIL_ID);

        // Get all the whatsMessageBatchList where perfilID is less than or equal to SMALLER_PERFIL_ID
        defaultWhatsMessageBatchShouldNotBeFound("perfilID.lessThanOrEqual=" + SMALLER_PERFIL_ID);
    }

    @Test
    @Transactional
    void getAllWhatsMessageBatchesByPerfilIDIsLessThanSomething() throws Exception {
        // Initialize the database
        whatsMessageBatchRepository.saveAndFlush(whatsMessageBatch);

        // Get all the whatsMessageBatchList where perfilID is less than DEFAULT_PERFIL_ID
        defaultWhatsMessageBatchShouldNotBeFound("perfilID.lessThan=" + DEFAULT_PERFIL_ID);

        // Get all the whatsMessageBatchList where perfilID is less than UPDATED_PERFIL_ID
        defaultWhatsMessageBatchShouldBeFound("perfilID.lessThan=" + UPDATED_PERFIL_ID);
    }

    @Test
    @Transactional
    void getAllWhatsMessageBatchesByPerfilIDIsGreaterThanSomething() throws Exception {
        // Initialize the database
        whatsMessageBatchRepository.saveAndFlush(whatsMessageBatch);

        // Get all the whatsMessageBatchList where perfilID is greater than DEFAULT_PERFIL_ID
        defaultWhatsMessageBatchShouldNotBeFound("perfilID.greaterThan=" + DEFAULT_PERFIL_ID);

        // Get all the whatsMessageBatchList where perfilID is greater than SMALLER_PERFIL_ID
        defaultWhatsMessageBatchShouldBeFound("perfilID.greaterThan=" + SMALLER_PERFIL_ID);
    }

    @Test
    @Transactional
    void getAllWhatsMessageBatchesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        whatsMessageBatchRepository.saveAndFlush(whatsMessageBatch);

        // Get all the whatsMessageBatchList where status equals to DEFAULT_STATUS
        defaultWhatsMessageBatchShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the whatsMessageBatchList where status equals to UPDATED_STATUS
        defaultWhatsMessageBatchShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllWhatsMessageBatchesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        whatsMessageBatchRepository.saveAndFlush(whatsMessageBatch);

        // Get all the whatsMessageBatchList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultWhatsMessageBatchShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the whatsMessageBatchList where status equals to UPDATED_STATUS
        defaultWhatsMessageBatchShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllWhatsMessageBatchesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        whatsMessageBatchRepository.saveAndFlush(whatsMessageBatch);

        // Get all the whatsMessageBatchList where status is not null
        defaultWhatsMessageBatchShouldBeFound("status.specified=true");

        // Get all the whatsMessageBatchList where status is null
        defaultWhatsMessageBatchShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllWhatsMessageBatchesByOfertaIdIsEqualToSomething() throws Exception {
        // Initialize the database
        whatsMessageBatchRepository.saveAndFlush(whatsMessageBatch);

        // Get all the whatsMessageBatchList where ofertaId equals to DEFAULT_OFERTA_ID
        defaultWhatsMessageBatchShouldBeFound("ofertaId.equals=" + DEFAULT_OFERTA_ID);

        // Get all the whatsMessageBatchList where ofertaId equals to UPDATED_OFERTA_ID
        defaultWhatsMessageBatchShouldNotBeFound("ofertaId.equals=" + UPDATED_OFERTA_ID);
    }

    @Test
    @Transactional
    void getAllWhatsMessageBatchesByOfertaIdIsInShouldWork() throws Exception {
        // Initialize the database
        whatsMessageBatchRepository.saveAndFlush(whatsMessageBatch);

        // Get all the whatsMessageBatchList where ofertaId in DEFAULT_OFERTA_ID or UPDATED_OFERTA_ID
        defaultWhatsMessageBatchShouldBeFound("ofertaId.in=" + DEFAULT_OFERTA_ID + "," + UPDATED_OFERTA_ID);

        // Get all the whatsMessageBatchList where ofertaId equals to UPDATED_OFERTA_ID
        defaultWhatsMessageBatchShouldNotBeFound("ofertaId.in=" + UPDATED_OFERTA_ID);
    }

    @Test
    @Transactional
    void getAllWhatsMessageBatchesByOfertaIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        whatsMessageBatchRepository.saveAndFlush(whatsMessageBatch);

        // Get all the whatsMessageBatchList where ofertaId is not null
        defaultWhatsMessageBatchShouldBeFound("ofertaId.specified=true");

        // Get all the whatsMessageBatchList where ofertaId is null
        defaultWhatsMessageBatchShouldNotBeFound("ofertaId.specified=false");
    }

    @Test
    @Transactional
    void getAllWhatsMessageBatchesByOfertaIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        whatsMessageBatchRepository.saveAndFlush(whatsMessageBatch);

        // Get all the whatsMessageBatchList where ofertaId is greater than or equal to DEFAULT_OFERTA_ID
        defaultWhatsMessageBatchShouldBeFound("ofertaId.greaterThanOrEqual=" + DEFAULT_OFERTA_ID);

        // Get all the whatsMessageBatchList where ofertaId is greater than or equal to UPDATED_OFERTA_ID
        defaultWhatsMessageBatchShouldNotBeFound("ofertaId.greaterThanOrEqual=" + UPDATED_OFERTA_ID);
    }

    @Test
    @Transactional
    void getAllWhatsMessageBatchesByOfertaIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        whatsMessageBatchRepository.saveAndFlush(whatsMessageBatch);

        // Get all the whatsMessageBatchList where ofertaId is less than or equal to DEFAULT_OFERTA_ID
        defaultWhatsMessageBatchShouldBeFound("ofertaId.lessThanOrEqual=" + DEFAULT_OFERTA_ID);

        // Get all the whatsMessageBatchList where ofertaId is less than or equal to SMALLER_OFERTA_ID
        defaultWhatsMessageBatchShouldNotBeFound("ofertaId.lessThanOrEqual=" + SMALLER_OFERTA_ID);
    }

    @Test
    @Transactional
    void getAllWhatsMessageBatchesByOfertaIdIsLessThanSomething() throws Exception {
        // Initialize the database
        whatsMessageBatchRepository.saveAndFlush(whatsMessageBatch);

        // Get all the whatsMessageBatchList where ofertaId is less than DEFAULT_OFERTA_ID
        defaultWhatsMessageBatchShouldNotBeFound("ofertaId.lessThan=" + DEFAULT_OFERTA_ID);

        // Get all the whatsMessageBatchList where ofertaId is less than UPDATED_OFERTA_ID
        defaultWhatsMessageBatchShouldBeFound("ofertaId.lessThan=" + UPDATED_OFERTA_ID);
    }

    @Test
    @Transactional
    void getAllWhatsMessageBatchesByOfertaIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        whatsMessageBatchRepository.saveAndFlush(whatsMessageBatch);

        // Get all the whatsMessageBatchList where ofertaId is greater than DEFAULT_OFERTA_ID
        defaultWhatsMessageBatchShouldNotBeFound("ofertaId.greaterThan=" + DEFAULT_OFERTA_ID);

        // Get all the whatsMessageBatchList where ofertaId is greater than SMALLER_OFERTA_ID
        defaultWhatsMessageBatchShouldBeFound("ofertaId.greaterThan=" + SMALLER_OFERTA_ID);
    }

    @Test
    @Transactional
    void getAllWhatsMessageBatchesByTipoOfertaIsEqualToSomething() throws Exception {
        // Initialize the database
        whatsMessageBatchRepository.saveAndFlush(whatsMessageBatch);

        // Get all the whatsMessageBatchList where tipoOferta equals to DEFAULT_TIPO_OFERTA
        defaultWhatsMessageBatchShouldBeFound("tipoOferta.equals=" + DEFAULT_TIPO_OFERTA);

        // Get all the whatsMessageBatchList where tipoOferta equals to UPDATED_TIPO_OFERTA
        defaultWhatsMessageBatchShouldNotBeFound("tipoOferta.equals=" + UPDATED_TIPO_OFERTA);
    }

    @Test
    @Transactional
    void getAllWhatsMessageBatchesByTipoOfertaIsInShouldWork() throws Exception {
        // Initialize the database
        whatsMessageBatchRepository.saveAndFlush(whatsMessageBatch);

        // Get all the whatsMessageBatchList where tipoOferta in DEFAULT_TIPO_OFERTA or UPDATED_TIPO_OFERTA
        defaultWhatsMessageBatchShouldBeFound("tipoOferta.in=" + DEFAULT_TIPO_OFERTA + "," + UPDATED_TIPO_OFERTA);

        // Get all the whatsMessageBatchList where tipoOferta equals to UPDATED_TIPO_OFERTA
        defaultWhatsMessageBatchShouldNotBeFound("tipoOferta.in=" + UPDATED_TIPO_OFERTA);
    }

    @Test
    @Transactional
    void getAllWhatsMessageBatchesByTipoOfertaIsNullOrNotNull() throws Exception {
        // Initialize the database
        whatsMessageBatchRepository.saveAndFlush(whatsMessageBatch);

        // Get all the whatsMessageBatchList where tipoOferta is not null
        defaultWhatsMessageBatchShouldBeFound("tipoOferta.specified=true");

        // Get all the whatsMessageBatchList where tipoOferta is null
        defaultWhatsMessageBatchShouldNotBeFound("tipoOferta.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultWhatsMessageBatchShouldBeFound(String filter) throws Exception {
        restWhatsMessageBatchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(whatsMessageBatch.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].waidTo").value(hasItem(DEFAULT_WAID_TO)))
            .andExpect(jsonPath("$.[*].perfilID").value(hasItem(DEFAULT_PERFIL_ID)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].ofertaId").value(hasItem(DEFAULT_OFERTA_ID.intValue())))
            .andExpect(jsonPath("$.[*].tipoOferta").value(hasItem(DEFAULT_TIPO_OFERTA.toString())));

        // Check, that the count call also returns 1
        restWhatsMessageBatchMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultWhatsMessageBatchShouldNotBeFound(String filter) throws Exception {
        restWhatsMessageBatchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restWhatsMessageBatchMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingWhatsMessageBatch() throws Exception {
        // Get the whatsMessageBatch
        restWhatsMessageBatchMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWhatsMessageBatch() throws Exception {
        // Initialize the database
        whatsMessageBatchRepository.saveAndFlush(whatsMessageBatch);

        int databaseSizeBeforeUpdate = whatsMessageBatchRepository.findAll().size();

        // Update the whatsMessageBatch
        WhatsMessageBatch updatedWhatsMessageBatch = whatsMessageBatchRepository.findById(whatsMessageBatch.getId()).get();
        // Disconnect from session so that the updates on updatedWhatsMessageBatch are not directly saved in db
        em.detach(updatedWhatsMessageBatch);
        updatedWhatsMessageBatch
            .tipo(UPDATED_TIPO)
            .waidTo(UPDATED_WAID_TO)
            .perfilID(UPDATED_PERFIL_ID)
            .status(UPDATED_STATUS)
            .ofertaId(UPDATED_OFERTA_ID)
            .tipoOferta(UPDATED_TIPO_OFERTA);
        WhatsMessageBatchDTO whatsMessageBatchDTO = whatsMessageBatchMapper.toDto(updatedWhatsMessageBatch);

        restWhatsMessageBatchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, whatsMessageBatchDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(whatsMessageBatchDTO))
            )
            .andExpect(status().isOk());

        // Validate the WhatsMessageBatch in the database
        List<WhatsMessageBatch> whatsMessageBatchList = whatsMessageBatchRepository.findAll();
        assertThat(whatsMessageBatchList).hasSize(databaseSizeBeforeUpdate);
        WhatsMessageBatch testWhatsMessageBatch = whatsMessageBatchList.get(whatsMessageBatchList.size() - 1);
        assertThat(testWhatsMessageBatch.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testWhatsMessageBatch.getWaidTo()).isEqualTo(UPDATED_WAID_TO);
        assertThat(testWhatsMessageBatch.getPerfilID()).isEqualTo(UPDATED_PERFIL_ID);
        assertThat(testWhatsMessageBatch.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testWhatsMessageBatch.getOfertaId()).isEqualTo(UPDATED_OFERTA_ID);
        assertThat(testWhatsMessageBatch.getTipoOferta()).isEqualTo(UPDATED_TIPO_OFERTA);
    }

    @Test
    @Transactional
    void putNonExistingWhatsMessageBatch() throws Exception {
        int databaseSizeBeforeUpdate = whatsMessageBatchRepository.findAll().size();
        whatsMessageBatch.setId(count.incrementAndGet());

        // Create the WhatsMessageBatch
        WhatsMessageBatchDTO whatsMessageBatchDTO = whatsMessageBatchMapper.toDto(whatsMessageBatch);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWhatsMessageBatchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, whatsMessageBatchDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(whatsMessageBatchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WhatsMessageBatch in the database
        List<WhatsMessageBatch> whatsMessageBatchList = whatsMessageBatchRepository.findAll();
        assertThat(whatsMessageBatchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWhatsMessageBatch() throws Exception {
        int databaseSizeBeforeUpdate = whatsMessageBatchRepository.findAll().size();
        whatsMessageBatch.setId(count.incrementAndGet());

        // Create the WhatsMessageBatch
        WhatsMessageBatchDTO whatsMessageBatchDTO = whatsMessageBatchMapper.toDto(whatsMessageBatch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWhatsMessageBatchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(whatsMessageBatchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WhatsMessageBatch in the database
        List<WhatsMessageBatch> whatsMessageBatchList = whatsMessageBatchRepository.findAll();
        assertThat(whatsMessageBatchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWhatsMessageBatch() throws Exception {
        int databaseSizeBeforeUpdate = whatsMessageBatchRepository.findAll().size();
        whatsMessageBatch.setId(count.incrementAndGet());

        // Create the WhatsMessageBatch
        WhatsMessageBatchDTO whatsMessageBatchDTO = whatsMessageBatchMapper.toDto(whatsMessageBatch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWhatsMessageBatchMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(whatsMessageBatchDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WhatsMessageBatch in the database
        List<WhatsMessageBatch> whatsMessageBatchList = whatsMessageBatchRepository.findAll();
        assertThat(whatsMessageBatchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWhatsMessageBatchWithPatch() throws Exception {
        // Initialize the database
        whatsMessageBatchRepository.saveAndFlush(whatsMessageBatch);

        int databaseSizeBeforeUpdate = whatsMessageBatchRepository.findAll().size();

        // Update the whatsMessageBatch using partial update
        WhatsMessageBatch partialUpdatedWhatsMessageBatch = new WhatsMessageBatch();
        partialUpdatedWhatsMessageBatch.setId(whatsMessageBatch.getId());

        partialUpdatedWhatsMessageBatch.tipo(UPDATED_TIPO).waidTo(UPDATED_WAID_TO).status(UPDATED_STATUS);

        restWhatsMessageBatchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWhatsMessageBatch.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWhatsMessageBatch))
            )
            .andExpect(status().isOk());

        // Validate the WhatsMessageBatch in the database
        List<WhatsMessageBatch> whatsMessageBatchList = whatsMessageBatchRepository.findAll();
        assertThat(whatsMessageBatchList).hasSize(databaseSizeBeforeUpdate);
        WhatsMessageBatch testWhatsMessageBatch = whatsMessageBatchList.get(whatsMessageBatchList.size() - 1);
        assertThat(testWhatsMessageBatch.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testWhatsMessageBatch.getWaidTo()).isEqualTo(UPDATED_WAID_TO);
        assertThat(testWhatsMessageBatch.getPerfilID()).isEqualTo(DEFAULT_PERFIL_ID);
        assertThat(testWhatsMessageBatch.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testWhatsMessageBatch.getOfertaId()).isEqualTo(DEFAULT_OFERTA_ID);
        assertThat(testWhatsMessageBatch.getTipoOferta()).isEqualTo(DEFAULT_TIPO_OFERTA);
    }

    @Test
    @Transactional
    void fullUpdateWhatsMessageBatchWithPatch() throws Exception {
        // Initialize the database
        whatsMessageBatchRepository.saveAndFlush(whatsMessageBatch);

        int databaseSizeBeforeUpdate = whatsMessageBatchRepository.findAll().size();

        // Update the whatsMessageBatch using partial update
        WhatsMessageBatch partialUpdatedWhatsMessageBatch = new WhatsMessageBatch();
        partialUpdatedWhatsMessageBatch.setId(whatsMessageBatch.getId());

        partialUpdatedWhatsMessageBatch
            .tipo(UPDATED_TIPO)
            .waidTo(UPDATED_WAID_TO)
            .perfilID(UPDATED_PERFIL_ID)
            .status(UPDATED_STATUS)
            .ofertaId(UPDATED_OFERTA_ID)
            .tipoOferta(UPDATED_TIPO_OFERTA);

        restWhatsMessageBatchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWhatsMessageBatch.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWhatsMessageBatch))
            )
            .andExpect(status().isOk());

        // Validate the WhatsMessageBatch in the database
        List<WhatsMessageBatch> whatsMessageBatchList = whatsMessageBatchRepository.findAll();
        assertThat(whatsMessageBatchList).hasSize(databaseSizeBeforeUpdate);
        WhatsMessageBatch testWhatsMessageBatch = whatsMessageBatchList.get(whatsMessageBatchList.size() - 1);
        assertThat(testWhatsMessageBatch.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testWhatsMessageBatch.getWaidTo()).isEqualTo(UPDATED_WAID_TO);
        assertThat(testWhatsMessageBatch.getPerfilID()).isEqualTo(UPDATED_PERFIL_ID);
        assertThat(testWhatsMessageBatch.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testWhatsMessageBatch.getOfertaId()).isEqualTo(UPDATED_OFERTA_ID);
        assertThat(testWhatsMessageBatch.getTipoOferta()).isEqualTo(UPDATED_TIPO_OFERTA);
    }

    @Test
    @Transactional
    void patchNonExistingWhatsMessageBatch() throws Exception {
        int databaseSizeBeforeUpdate = whatsMessageBatchRepository.findAll().size();
        whatsMessageBatch.setId(count.incrementAndGet());

        // Create the WhatsMessageBatch
        WhatsMessageBatchDTO whatsMessageBatchDTO = whatsMessageBatchMapper.toDto(whatsMessageBatch);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWhatsMessageBatchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, whatsMessageBatchDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(whatsMessageBatchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WhatsMessageBatch in the database
        List<WhatsMessageBatch> whatsMessageBatchList = whatsMessageBatchRepository.findAll();
        assertThat(whatsMessageBatchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWhatsMessageBatch() throws Exception {
        int databaseSizeBeforeUpdate = whatsMessageBatchRepository.findAll().size();
        whatsMessageBatch.setId(count.incrementAndGet());

        // Create the WhatsMessageBatch
        WhatsMessageBatchDTO whatsMessageBatchDTO = whatsMessageBatchMapper.toDto(whatsMessageBatch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWhatsMessageBatchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(whatsMessageBatchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WhatsMessageBatch in the database
        List<WhatsMessageBatch> whatsMessageBatchList = whatsMessageBatchRepository.findAll();
        assertThat(whatsMessageBatchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWhatsMessageBatch() throws Exception {
        int databaseSizeBeforeUpdate = whatsMessageBatchRepository.findAll().size();
        whatsMessageBatch.setId(count.incrementAndGet());

        // Create the WhatsMessageBatch
        WhatsMessageBatchDTO whatsMessageBatchDTO = whatsMessageBatchMapper.toDto(whatsMessageBatch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWhatsMessageBatchMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(whatsMessageBatchDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WhatsMessageBatch in the database
        List<WhatsMessageBatch> whatsMessageBatchList = whatsMessageBatchRepository.findAll();
        assertThat(whatsMessageBatchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWhatsMessageBatch() throws Exception {
        // Initialize the database
        whatsMessageBatchRepository.saveAndFlush(whatsMessageBatch);

        int databaseSizeBeforeDelete = whatsMessageBatchRepository.findAll().size();

        // Delete the whatsMessageBatch
        restWhatsMessageBatchMockMvc
            .perform(delete(ENTITY_API_URL_ID, whatsMessageBatch.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WhatsMessageBatch> whatsMessageBatchList = whatsMessageBatchRepository.findAll();
        assertThat(whatsMessageBatchList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

package com.infocargas.freteapp.web.rest;

import static com.infocargas.freteapp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.infocargas.freteapp.IntegrationTest;
import com.infocargas.freteapp.domain.Ofertas;
import com.infocargas.freteapp.domain.Perfil;
import com.infocargas.freteapp.domain.Solicitacao;
import com.infocargas.freteapp.domain.enumeration.AnwserStatus;
import com.infocargas.freteapp.domain.enumeration.StatusSolicitacao;
import com.infocargas.freteapp.repository.SolicitacaoRepository;
import com.infocargas.freteapp.service.criteria.SolicitacaoCriteria;
import com.infocargas.freteapp.service.dto.SolicitacaoDTO;
import com.infocargas.freteapp.service.mapper.SolicitacaoMapper;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link SolicitacaoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SolicitacaoResourceIT {

    private static final ZonedDateTime DEFAULT_DATA_PROPOSTA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_PROPOSTA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATA_PROPOSTA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_DATA_MODIFICACAO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_MODIFICACAO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATA_MODIFICACAO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final AnwserStatus DEFAULT_ACEITAR = AnwserStatus.SIM;
    private static final AnwserStatus UPDATED_ACEITAR = AnwserStatus.NAO;

    private static final StatusSolicitacao DEFAULT_STATUS = StatusSolicitacao.AGUARDANDORESPOSTA;
    private static final StatusSolicitacao UPDATED_STATUS = StatusSolicitacao.AGUARDANDO;

    private static final String DEFAULT_OBS = "AAAAAAAAAA";
    private static final String UPDATED_OBS = "BBBBBBBBBB";

    private static final Double DEFAULT_VALOR_FRETE = 1D;
    private static final Double UPDATED_VALOR_FRETE = 2D;
    private static final Double SMALLER_VALOR_FRETE = 1D - 1D;

    private static final String ENTITY_API_URL = "/api/solicitacaos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SolicitacaoRepository solicitacaoRepository;

    @Autowired
    private SolicitacaoMapper solicitacaoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSolicitacaoMockMvc;

    private Solicitacao solicitacao;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Solicitacao createEntity(EntityManager em) {
        Solicitacao solicitacao = new Solicitacao()
            .dataProposta(DEFAULT_DATA_PROPOSTA)
            .dataModificacao(DEFAULT_DATA_MODIFICACAO)
            .aceitar(DEFAULT_ACEITAR)
            .status(DEFAULT_STATUS)
            .obs(DEFAULT_OBS)
            .valorFrete(DEFAULT_VALOR_FRETE);
        // Add required entity
        Ofertas ofertas;
        if (TestUtil.findAll(em, Ofertas.class).isEmpty()) {
            ofertas = OfertasResourceIT.createEntity(em);
            em.persist(ofertas);
            em.flush();
        } else {
            ofertas = TestUtil.findAll(em, Ofertas.class).get(0);
        }
        solicitacao.setOfertas(ofertas);
        // Add required entity
        Perfil perfil;
        if (TestUtil.findAll(em, Perfil.class).isEmpty()) {
            perfil = PerfilResourceIT.createEntity(em);
            em.persist(perfil);
            em.flush();
        } else {
            perfil = TestUtil.findAll(em, Perfil.class).get(0);
        }
        solicitacao.setPerfil(perfil);
        return solicitacao;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Solicitacao createUpdatedEntity(EntityManager em) {
        Solicitacao solicitacao = new Solicitacao()
            .dataProposta(UPDATED_DATA_PROPOSTA)
            .dataModificacao(UPDATED_DATA_MODIFICACAO)
            .aceitar(UPDATED_ACEITAR)
            .status(UPDATED_STATUS)
            .obs(UPDATED_OBS)
            .valorFrete(UPDATED_VALOR_FRETE);
        // Add required entity
        Ofertas ofertas;
        if (TestUtil.findAll(em, Ofertas.class).isEmpty()) {
            ofertas = OfertasResourceIT.createUpdatedEntity(em);
            em.persist(ofertas);
            em.flush();
        } else {
            ofertas = TestUtil.findAll(em, Ofertas.class).get(0);
        }
        solicitacao.setOfertas(ofertas);
        // Add required entity
        Perfil perfil;
        if (TestUtil.findAll(em, Perfil.class).isEmpty()) {
            perfil = PerfilResourceIT.createUpdatedEntity(em);
            em.persist(perfil);
            em.flush();
        } else {
            perfil = TestUtil.findAll(em, Perfil.class).get(0);
        }
        solicitacao.setPerfil(perfil);
        return solicitacao;
    }

    @BeforeEach
    public void initTest() {
        solicitacao = createEntity(em);
    }

    @Test
    @Transactional
    void createSolicitacao() throws Exception {
        int databaseSizeBeforeCreate = solicitacaoRepository.findAll().size();
        // Create the Solicitacao
        SolicitacaoDTO solicitacaoDTO = solicitacaoMapper.toDto(solicitacao);
        restSolicitacaoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(solicitacaoDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Solicitacao in the database
        List<Solicitacao> solicitacaoList = solicitacaoRepository.findAll();
        assertThat(solicitacaoList).hasSize(databaseSizeBeforeCreate + 1);
        Solicitacao testSolicitacao = solicitacaoList.get(solicitacaoList.size() - 1);
        assertThat(testSolicitacao.getDataProposta()).isEqualTo(DEFAULT_DATA_PROPOSTA);
        assertThat(testSolicitacao.getDataModificacao()).isEqualTo(DEFAULT_DATA_MODIFICACAO);
        assertThat(testSolicitacao.getAceitar()).isEqualTo(DEFAULT_ACEITAR);
        assertThat(testSolicitacao.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testSolicitacao.getObs()).isEqualTo(DEFAULT_OBS);
        assertThat(testSolicitacao.getValorFrete()).isEqualTo(DEFAULT_VALOR_FRETE);
    }

    @Test
    @Transactional
    void createSolicitacaoWithExistingId() throws Exception {
        // Create the Solicitacao with an existing ID
        solicitacao.setId(1L);
        SolicitacaoDTO solicitacaoDTO = solicitacaoMapper.toDto(solicitacao);

        int databaseSizeBeforeCreate = solicitacaoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSolicitacaoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(solicitacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Solicitacao in the database
        List<Solicitacao> solicitacaoList = solicitacaoRepository.findAll();
        assertThat(solicitacaoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDataPropostaIsRequired() throws Exception {
        int databaseSizeBeforeTest = solicitacaoRepository.findAll().size();
        // set the field null
        solicitacao.setDataProposta(null);

        // Create the Solicitacao, which fails.
        SolicitacaoDTO solicitacaoDTO = solicitacaoMapper.toDto(solicitacao);

        restSolicitacaoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(solicitacaoDTO))
            )
            .andExpect(status().isBadRequest());

        List<Solicitacao> solicitacaoList = solicitacaoRepository.findAll();
        assertThat(solicitacaoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = solicitacaoRepository.findAll().size();
        // set the field null
        solicitacao.setStatus(null);

        // Create the Solicitacao, which fails.
        SolicitacaoDTO solicitacaoDTO = solicitacaoMapper.toDto(solicitacao);

        restSolicitacaoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(solicitacaoDTO))
            )
            .andExpect(status().isBadRequest());

        List<Solicitacao> solicitacaoList = solicitacaoRepository.findAll();
        assertThat(solicitacaoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSolicitacaos() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList
        restSolicitacaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(solicitacao.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataProposta").value(hasItem(sameInstant(DEFAULT_DATA_PROPOSTA))))
            .andExpect(jsonPath("$.[*].dataModificacao").value(hasItem(sameInstant(DEFAULT_DATA_MODIFICACAO))))
            .andExpect(jsonPath("$.[*].aceitar").value(hasItem(DEFAULT_ACEITAR.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].obs").value(hasItem(DEFAULT_OBS)))
            .andExpect(jsonPath("$.[*].valorFrete").value(hasItem(DEFAULT_VALOR_FRETE.doubleValue())));
    }

    @Test
    @Transactional
    void getSolicitacao() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get the solicitacao
        restSolicitacaoMockMvc
            .perform(get(ENTITY_API_URL_ID, solicitacao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(solicitacao.getId().intValue()))
            .andExpect(jsonPath("$.dataProposta").value(sameInstant(DEFAULT_DATA_PROPOSTA)))
            .andExpect(jsonPath("$.dataModificacao").value(sameInstant(DEFAULT_DATA_MODIFICACAO)))
            .andExpect(jsonPath("$.aceitar").value(DEFAULT_ACEITAR.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.obs").value(DEFAULT_OBS))
            .andExpect(jsonPath("$.valorFrete").value(DEFAULT_VALOR_FRETE.doubleValue()));
    }

    @Test
    @Transactional
    void getSolicitacaosByIdFiltering() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        Long id = solicitacao.getId();

        defaultSolicitacaoShouldBeFound("id.equals=" + id);
        defaultSolicitacaoShouldNotBeFound("id.notEquals=" + id);

        defaultSolicitacaoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSolicitacaoShouldNotBeFound("id.greaterThan=" + id);

        defaultSolicitacaoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSolicitacaoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByDataPropostaIsEqualToSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where dataProposta equals to DEFAULT_DATA_PROPOSTA
        defaultSolicitacaoShouldBeFound("dataProposta.equals=" + DEFAULT_DATA_PROPOSTA);

        // Get all the solicitacaoList where dataProposta equals to UPDATED_DATA_PROPOSTA
        defaultSolicitacaoShouldNotBeFound("dataProposta.equals=" + UPDATED_DATA_PROPOSTA);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByDataPropostaIsInShouldWork() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where dataProposta in DEFAULT_DATA_PROPOSTA or UPDATED_DATA_PROPOSTA
        defaultSolicitacaoShouldBeFound("dataProposta.in=" + DEFAULT_DATA_PROPOSTA + "," + UPDATED_DATA_PROPOSTA);

        // Get all the solicitacaoList where dataProposta equals to UPDATED_DATA_PROPOSTA
        defaultSolicitacaoShouldNotBeFound("dataProposta.in=" + UPDATED_DATA_PROPOSTA);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByDataPropostaIsNullOrNotNull() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where dataProposta is not null
        defaultSolicitacaoShouldBeFound("dataProposta.specified=true");

        // Get all the solicitacaoList where dataProposta is null
        defaultSolicitacaoShouldNotBeFound("dataProposta.specified=false");
    }

    @Test
    @Transactional
    void getAllSolicitacaosByDataPropostaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where dataProposta is greater than or equal to DEFAULT_DATA_PROPOSTA
        defaultSolicitacaoShouldBeFound("dataProposta.greaterThanOrEqual=" + DEFAULT_DATA_PROPOSTA);

        // Get all the solicitacaoList where dataProposta is greater than or equal to UPDATED_DATA_PROPOSTA
        defaultSolicitacaoShouldNotBeFound("dataProposta.greaterThanOrEqual=" + UPDATED_DATA_PROPOSTA);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByDataPropostaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where dataProposta is less than or equal to DEFAULT_DATA_PROPOSTA
        defaultSolicitacaoShouldBeFound("dataProposta.lessThanOrEqual=" + DEFAULT_DATA_PROPOSTA);

        // Get all the solicitacaoList where dataProposta is less than or equal to SMALLER_DATA_PROPOSTA
        defaultSolicitacaoShouldNotBeFound("dataProposta.lessThanOrEqual=" + SMALLER_DATA_PROPOSTA);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByDataPropostaIsLessThanSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where dataProposta is less than DEFAULT_DATA_PROPOSTA
        defaultSolicitacaoShouldNotBeFound("dataProposta.lessThan=" + DEFAULT_DATA_PROPOSTA);

        // Get all the solicitacaoList where dataProposta is less than UPDATED_DATA_PROPOSTA
        defaultSolicitacaoShouldBeFound("dataProposta.lessThan=" + UPDATED_DATA_PROPOSTA);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByDataPropostaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where dataProposta is greater than DEFAULT_DATA_PROPOSTA
        defaultSolicitacaoShouldNotBeFound("dataProposta.greaterThan=" + DEFAULT_DATA_PROPOSTA);

        // Get all the solicitacaoList where dataProposta is greater than SMALLER_DATA_PROPOSTA
        defaultSolicitacaoShouldBeFound("dataProposta.greaterThan=" + SMALLER_DATA_PROPOSTA);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByDataModificacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where dataModificacao equals to DEFAULT_DATA_MODIFICACAO
        defaultSolicitacaoShouldBeFound("dataModificacao.equals=" + DEFAULT_DATA_MODIFICACAO);

        // Get all the solicitacaoList where dataModificacao equals to UPDATED_DATA_MODIFICACAO
        defaultSolicitacaoShouldNotBeFound("dataModificacao.equals=" + UPDATED_DATA_MODIFICACAO);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByDataModificacaoIsInShouldWork() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where dataModificacao in DEFAULT_DATA_MODIFICACAO or UPDATED_DATA_MODIFICACAO
        defaultSolicitacaoShouldBeFound("dataModificacao.in=" + DEFAULT_DATA_MODIFICACAO + "," + UPDATED_DATA_MODIFICACAO);

        // Get all the solicitacaoList where dataModificacao equals to UPDATED_DATA_MODIFICACAO
        defaultSolicitacaoShouldNotBeFound("dataModificacao.in=" + UPDATED_DATA_MODIFICACAO);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByDataModificacaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where dataModificacao is not null
        defaultSolicitacaoShouldBeFound("dataModificacao.specified=true");

        // Get all the solicitacaoList where dataModificacao is null
        defaultSolicitacaoShouldNotBeFound("dataModificacao.specified=false");
    }

    @Test
    @Transactional
    void getAllSolicitacaosByDataModificacaoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where dataModificacao is greater than or equal to DEFAULT_DATA_MODIFICACAO
        defaultSolicitacaoShouldBeFound("dataModificacao.greaterThanOrEqual=" + DEFAULT_DATA_MODIFICACAO);

        // Get all the solicitacaoList where dataModificacao is greater than or equal to UPDATED_DATA_MODIFICACAO
        defaultSolicitacaoShouldNotBeFound("dataModificacao.greaterThanOrEqual=" + UPDATED_DATA_MODIFICACAO);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByDataModificacaoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where dataModificacao is less than or equal to DEFAULT_DATA_MODIFICACAO
        defaultSolicitacaoShouldBeFound("dataModificacao.lessThanOrEqual=" + DEFAULT_DATA_MODIFICACAO);

        // Get all the solicitacaoList where dataModificacao is less than or equal to SMALLER_DATA_MODIFICACAO
        defaultSolicitacaoShouldNotBeFound("dataModificacao.lessThanOrEqual=" + SMALLER_DATA_MODIFICACAO);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByDataModificacaoIsLessThanSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where dataModificacao is less than DEFAULT_DATA_MODIFICACAO
        defaultSolicitacaoShouldNotBeFound("dataModificacao.lessThan=" + DEFAULT_DATA_MODIFICACAO);

        // Get all the solicitacaoList where dataModificacao is less than UPDATED_DATA_MODIFICACAO
        defaultSolicitacaoShouldBeFound("dataModificacao.lessThan=" + UPDATED_DATA_MODIFICACAO);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByDataModificacaoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where dataModificacao is greater than DEFAULT_DATA_MODIFICACAO
        defaultSolicitacaoShouldNotBeFound("dataModificacao.greaterThan=" + DEFAULT_DATA_MODIFICACAO);

        // Get all the solicitacaoList where dataModificacao is greater than SMALLER_DATA_MODIFICACAO
        defaultSolicitacaoShouldBeFound("dataModificacao.greaterThan=" + SMALLER_DATA_MODIFICACAO);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByAceitarIsEqualToSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where aceitar equals to DEFAULT_ACEITAR
        defaultSolicitacaoShouldBeFound("aceitar.equals=" + DEFAULT_ACEITAR);

        // Get all the solicitacaoList where aceitar equals to UPDATED_ACEITAR
        defaultSolicitacaoShouldNotBeFound("aceitar.equals=" + UPDATED_ACEITAR);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByAceitarIsInShouldWork() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where aceitar in DEFAULT_ACEITAR or UPDATED_ACEITAR
        defaultSolicitacaoShouldBeFound("aceitar.in=" + DEFAULT_ACEITAR + "," + UPDATED_ACEITAR);

        // Get all the solicitacaoList where aceitar equals to UPDATED_ACEITAR
        defaultSolicitacaoShouldNotBeFound("aceitar.in=" + UPDATED_ACEITAR);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByAceitarIsNullOrNotNull() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where aceitar is not null
        defaultSolicitacaoShouldBeFound("aceitar.specified=true");

        // Get all the solicitacaoList where aceitar is null
        defaultSolicitacaoShouldNotBeFound("aceitar.specified=false");
    }

    @Test
    @Transactional
    void getAllSolicitacaosByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where status equals to DEFAULT_STATUS
        defaultSolicitacaoShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the solicitacaoList where status equals to UPDATED_STATUS
        defaultSolicitacaoShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultSolicitacaoShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the solicitacaoList where status equals to UPDATED_STATUS
        defaultSolicitacaoShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where status is not null
        defaultSolicitacaoShouldBeFound("status.specified=true");

        // Get all the solicitacaoList where status is null
        defaultSolicitacaoShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllSolicitacaosByObsIsEqualToSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where obs equals to DEFAULT_OBS
        defaultSolicitacaoShouldBeFound("obs.equals=" + DEFAULT_OBS);

        // Get all the solicitacaoList where obs equals to UPDATED_OBS
        defaultSolicitacaoShouldNotBeFound("obs.equals=" + UPDATED_OBS);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByObsIsInShouldWork() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where obs in DEFAULT_OBS or UPDATED_OBS
        defaultSolicitacaoShouldBeFound("obs.in=" + DEFAULT_OBS + "," + UPDATED_OBS);

        // Get all the solicitacaoList where obs equals to UPDATED_OBS
        defaultSolicitacaoShouldNotBeFound("obs.in=" + UPDATED_OBS);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByObsIsNullOrNotNull() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where obs is not null
        defaultSolicitacaoShouldBeFound("obs.specified=true");

        // Get all the solicitacaoList where obs is null
        defaultSolicitacaoShouldNotBeFound("obs.specified=false");
    }

    @Test
    @Transactional
    void getAllSolicitacaosByObsContainsSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where obs contains DEFAULT_OBS
        defaultSolicitacaoShouldBeFound("obs.contains=" + DEFAULT_OBS);

        // Get all the solicitacaoList where obs contains UPDATED_OBS
        defaultSolicitacaoShouldNotBeFound("obs.contains=" + UPDATED_OBS);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByObsNotContainsSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where obs does not contain DEFAULT_OBS
        defaultSolicitacaoShouldNotBeFound("obs.doesNotContain=" + DEFAULT_OBS);

        // Get all the solicitacaoList where obs does not contain UPDATED_OBS
        defaultSolicitacaoShouldBeFound("obs.doesNotContain=" + UPDATED_OBS);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByValorFreteIsEqualToSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where valorFrete equals to DEFAULT_VALOR_FRETE
        defaultSolicitacaoShouldBeFound("valorFrete.equals=" + DEFAULT_VALOR_FRETE);

        // Get all the solicitacaoList where valorFrete equals to UPDATED_VALOR_FRETE
        defaultSolicitacaoShouldNotBeFound("valorFrete.equals=" + UPDATED_VALOR_FRETE);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByValorFreteIsInShouldWork() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where valorFrete in DEFAULT_VALOR_FRETE or UPDATED_VALOR_FRETE
        defaultSolicitacaoShouldBeFound("valorFrete.in=" + DEFAULT_VALOR_FRETE + "," + UPDATED_VALOR_FRETE);

        // Get all the solicitacaoList where valorFrete equals to UPDATED_VALOR_FRETE
        defaultSolicitacaoShouldNotBeFound("valorFrete.in=" + UPDATED_VALOR_FRETE);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByValorFreteIsNullOrNotNull() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where valorFrete is not null
        defaultSolicitacaoShouldBeFound("valorFrete.specified=true");

        // Get all the solicitacaoList where valorFrete is null
        defaultSolicitacaoShouldNotBeFound("valorFrete.specified=false");
    }

    @Test
    @Transactional
    void getAllSolicitacaosByValorFreteIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where valorFrete is greater than or equal to DEFAULT_VALOR_FRETE
        defaultSolicitacaoShouldBeFound("valorFrete.greaterThanOrEqual=" + DEFAULT_VALOR_FRETE);

        // Get all the solicitacaoList where valorFrete is greater than or equal to UPDATED_VALOR_FRETE
        defaultSolicitacaoShouldNotBeFound("valorFrete.greaterThanOrEqual=" + UPDATED_VALOR_FRETE);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByValorFreteIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where valorFrete is less than or equal to DEFAULT_VALOR_FRETE
        defaultSolicitacaoShouldBeFound("valorFrete.lessThanOrEqual=" + DEFAULT_VALOR_FRETE);

        // Get all the solicitacaoList where valorFrete is less than or equal to SMALLER_VALOR_FRETE
        defaultSolicitacaoShouldNotBeFound("valorFrete.lessThanOrEqual=" + SMALLER_VALOR_FRETE);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByValorFreteIsLessThanSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where valorFrete is less than DEFAULT_VALOR_FRETE
        defaultSolicitacaoShouldNotBeFound("valorFrete.lessThan=" + DEFAULT_VALOR_FRETE);

        // Get all the solicitacaoList where valorFrete is less than UPDATED_VALOR_FRETE
        defaultSolicitacaoShouldBeFound("valorFrete.lessThan=" + UPDATED_VALOR_FRETE);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByValorFreteIsGreaterThanSomething() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        // Get all the solicitacaoList where valorFrete is greater than DEFAULT_VALOR_FRETE
        defaultSolicitacaoShouldNotBeFound("valorFrete.greaterThan=" + DEFAULT_VALOR_FRETE);

        // Get all the solicitacaoList where valorFrete is greater than SMALLER_VALOR_FRETE
        defaultSolicitacaoShouldBeFound("valorFrete.greaterThan=" + SMALLER_VALOR_FRETE);
    }

    @Test
    @Transactional
    void getAllSolicitacaosByOfertasIsEqualToSomething() throws Exception {
        Ofertas ofertas;
        if (TestUtil.findAll(em, Ofertas.class).isEmpty()) {
            solicitacaoRepository.saveAndFlush(solicitacao);
            ofertas = OfertasResourceIT.createEntity(em);
        } else {
            ofertas = TestUtil.findAll(em, Ofertas.class).get(0);
        }
        em.persist(ofertas);
        em.flush();
        solicitacao.setOfertas(ofertas);
        solicitacaoRepository.saveAndFlush(solicitacao);
        Long ofertasId = ofertas.getId();

        // Get all the solicitacaoList where ofertas equals to ofertasId
        defaultSolicitacaoShouldBeFound("ofertasId.equals=" + ofertasId);

        // Get all the solicitacaoList where ofertas equals to (ofertasId + 1)
        defaultSolicitacaoShouldNotBeFound("ofertasId.equals=" + (ofertasId + 1));
    }

    @Test
    @Transactional
    void getAllSolicitacaosByPerfilIsEqualToSomething() throws Exception {
        Perfil perfil;
        if (TestUtil.findAll(em, Perfil.class).isEmpty()) {
            solicitacaoRepository.saveAndFlush(solicitacao);
            perfil = PerfilResourceIT.createEntity(em);
        } else {
            perfil = TestUtil.findAll(em, Perfil.class).get(0);
        }
        em.persist(perfil);
        em.flush();
        solicitacao.setPerfil(perfil);
        solicitacaoRepository.saveAndFlush(solicitacao);
        Long perfilId = perfil.getId();

        // Get all the solicitacaoList where perfil equals to perfilId
        defaultSolicitacaoShouldBeFound("perfilId.equals=" + perfilId);

        // Get all the solicitacaoList where perfil equals to (perfilId + 1)
        defaultSolicitacaoShouldNotBeFound("perfilId.equals=" + (perfilId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSolicitacaoShouldBeFound(String filter) throws Exception {
        restSolicitacaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(solicitacao.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataProposta").value(hasItem(sameInstant(DEFAULT_DATA_PROPOSTA))))
            .andExpect(jsonPath("$.[*].dataModificacao").value(hasItem(sameInstant(DEFAULT_DATA_MODIFICACAO))))
            .andExpect(jsonPath("$.[*].aceitar").value(hasItem(DEFAULT_ACEITAR.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].obs").value(hasItem(DEFAULT_OBS)))
            .andExpect(jsonPath("$.[*].valorFrete").value(hasItem(DEFAULT_VALOR_FRETE.doubleValue())));

        // Check, that the count call also returns 1
        restSolicitacaoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSolicitacaoShouldNotBeFound(String filter) throws Exception {
        restSolicitacaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSolicitacaoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSolicitacao() throws Exception {
        // Get the solicitacao
        restSolicitacaoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSolicitacao() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        int databaseSizeBeforeUpdate = solicitacaoRepository.findAll().size();

        // Update the solicitacao
        Solicitacao updatedSolicitacao = solicitacaoRepository.findById(solicitacao.getId()).get();
        // Disconnect from session so that the updates on updatedSolicitacao are not directly saved in db
        em.detach(updatedSolicitacao);
        updatedSolicitacao
            .dataProposta(UPDATED_DATA_PROPOSTA)
            .dataModificacao(UPDATED_DATA_MODIFICACAO)
            .aceitar(UPDATED_ACEITAR)
            .status(UPDATED_STATUS)
            .obs(UPDATED_OBS)
            .valorFrete(UPDATED_VALOR_FRETE);
        SolicitacaoDTO solicitacaoDTO = solicitacaoMapper.toDto(updatedSolicitacao);

        restSolicitacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, solicitacaoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(solicitacaoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Solicitacao in the database
        List<Solicitacao> solicitacaoList = solicitacaoRepository.findAll();
        assertThat(solicitacaoList).hasSize(databaseSizeBeforeUpdate);
        Solicitacao testSolicitacao = solicitacaoList.get(solicitacaoList.size() - 1);
        assertThat(testSolicitacao.getDataProposta()).isEqualTo(UPDATED_DATA_PROPOSTA);
        assertThat(testSolicitacao.getDataModificacao()).isEqualTo(UPDATED_DATA_MODIFICACAO);
        assertThat(testSolicitacao.getAceitar()).isEqualTo(UPDATED_ACEITAR);
        assertThat(testSolicitacao.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testSolicitacao.getObs()).isEqualTo(UPDATED_OBS);
        assertThat(testSolicitacao.getValorFrete()).isEqualTo(UPDATED_VALOR_FRETE);
    }

    @Test
    @Transactional
    void putNonExistingSolicitacao() throws Exception {
        int databaseSizeBeforeUpdate = solicitacaoRepository.findAll().size();
        solicitacao.setId(count.incrementAndGet());

        // Create the Solicitacao
        SolicitacaoDTO solicitacaoDTO = solicitacaoMapper.toDto(solicitacao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSolicitacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, solicitacaoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(solicitacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Solicitacao in the database
        List<Solicitacao> solicitacaoList = solicitacaoRepository.findAll();
        assertThat(solicitacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSolicitacao() throws Exception {
        int databaseSizeBeforeUpdate = solicitacaoRepository.findAll().size();
        solicitacao.setId(count.incrementAndGet());

        // Create the Solicitacao
        SolicitacaoDTO solicitacaoDTO = solicitacaoMapper.toDto(solicitacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSolicitacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(solicitacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Solicitacao in the database
        List<Solicitacao> solicitacaoList = solicitacaoRepository.findAll();
        assertThat(solicitacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSolicitacao() throws Exception {
        int databaseSizeBeforeUpdate = solicitacaoRepository.findAll().size();
        solicitacao.setId(count.incrementAndGet());

        // Create the Solicitacao
        SolicitacaoDTO solicitacaoDTO = solicitacaoMapper.toDto(solicitacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSolicitacaoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(solicitacaoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Solicitacao in the database
        List<Solicitacao> solicitacaoList = solicitacaoRepository.findAll();
        assertThat(solicitacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSolicitacaoWithPatch() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        int databaseSizeBeforeUpdate = solicitacaoRepository.findAll().size();

        // Update the solicitacao using partial update
        Solicitacao partialUpdatedSolicitacao = new Solicitacao();
        partialUpdatedSolicitacao.setId(solicitacao.getId());

        partialUpdatedSolicitacao
            .dataModificacao(UPDATED_DATA_MODIFICACAO)
            .aceitar(UPDATED_ACEITAR)
            .status(UPDATED_STATUS)
            .valorFrete(UPDATED_VALOR_FRETE);

        restSolicitacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSolicitacao.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSolicitacao))
            )
            .andExpect(status().isOk());

        // Validate the Solicitacao in the database
        List<Solicitacao> solicitacaoList = solicitacaoRepository.findAll();
        assertThat(solicitacaoList).hasSize(databaseSizeBeforeUpdate);
        Solicitacao testSolicitacao = solicitacaoList.get(solicitacaoList.size() - 1);
        assertThat(testSolicitacao.getDataProposta()).isEqualTo(DEFAULT_DATA_PROPOSTA);
        assertThat(testSolicitacao.getDataModificacao()).isEqualTo(UPDATED_DATA_MODIFICACAO);
        assertThat(testSolicitacao.getAceitar()).isEqualTo(UPDATED_ACEITAR);
        assertThat(testSolicitacao.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testSolicitacao.getObs()).isEqualTo(DEFAULT_OBS);
        assertThat(testSolicitacao.getValorFrete()).isEqualTo(UPDATED_VALOR_FRETE);
    }

    @Test
    @Transactional
    void fullUpdateSolicitacaoWithPatch() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        int databaseSizeBeforeUpdate = solicitacaoRepository.findAll().size();

        // Update the solicitacao using partial update
        Solicitacao partialUpdatedSolicitacao = new Solicitacao();
        partialUpdatedSolicitacao.setId(solicitacao.getId());

        partialUpdatedSolicitacao
            .dataProposta(UPDATED_DATA_PROPOSTA)
            .dataModificacao(UPDATED_DATA_MODIFICACAO)
            .aceitar(UPDATED_ACEITAR)
            .status(UPDATED_STATUS)
            .obs(UPDATED_OBS)
            .valorFrete(UPDATED_VALOR_FRETE);

        restSolicitacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSolicitacao.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSolicitacao))
            )
            .andExpect(status().isOk());

        // Validate the Solicitacao in the database
        List<Solicitacao> solicitacaoList = solicitacaoRepository.findAll();
        assertThat(solicitacaoList).hasSize(databaseSizeBeforeUpdate);
        Solicitacao testSolicitacao = solicitacaoList.get(solicitacaoList.size() - 1);
        assertThat(testSolicitacao.getDataProposta()).isEqualTo(UPDATED_DATA_PROPOSTA);
        assertThat(testSolicitacao.getDataModificacao()).isEqualTo(UPDATED_DATA_MODIFICACAO);
        assertThat(testSolicitacao.getAceitar()).isEqualTo(UPDATED_ACEITAR);
        assertThat(testSolicitacao.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testSolicitacao.getObs()).isEqualTo(UPDATED_OBS);
        assertThat(testSolicitacao.getValorFrete()).isEqualTo(UPDATED_VALOR_FRETE);
    }

    @Test
    @Transactional
    void patchNonExistingSolicitacao() throws Exception {
        int databaseSizeBeforeUpdate = solicitacaoRepository.findAll().size();
        solicitacao.setId(count.incrementAndGet());

        // Create the Solicitacao
        SolicitacaoDTO solicitacaoDTO = solicitacaoMapper.toDto(solicitacao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSolicitacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, solicitacaoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(solicitacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Solicitacao in the database
        List<Solicitacao> solicitacaoList = solicitacaoRepository.findAll();
        assertThat(solicitacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSolicitacao() throws Exception {
        int databaseSizeBeforeUpdate = solicitacaoRepository.findAll().size();
        solicitacao.setId(count.incrementAndGet());

        // Create the Solicitacao
        SolicitacaoDTO solicitacaoDTO = solicitacaoMapper.toDto(solicitacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSolicitacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(solicitacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Solicitacao in the database
        List<Solicitacao> solicitacaoList = solicitacaoRepository.findAll();
        assertThat(solicitacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSolicitacao() throws Exception {
        int databaseSizeBeforeUpdate = solicitacaoRepository.findAll().size();
        solicitacao.setId(count.incrementAndGet());

        // Create the Solicitacao
        SolicitacaoDTO solicitacaoDTO = solicitacaoMapper.toDto(solicitacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSolicitacaoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(solicitacaoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Solicitacao in the database
        List<Solicitacao> solicitacaoList = solicitacaoRepository.findAll();
        assertThat(solicitacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSolicitacao() throws Exception {
        // Initialize the database
        solicitacaoRepository.saveAndFlush(solicitacao);

        int databaseSizeBeforeDelete = solicitacaoRepository.findAll().size();

        // Delete the solicitacao
        restSolicitacaoMockMvc
            .perform(delete(ENTITY_API_URL_ID, solicitacao.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Solicitacao> solicitacaoList = solicitacaoRepository.findAll();
        assertThat(solicitacaoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

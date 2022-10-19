package com.infocargas.freteapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.infocargas.freteapp.IntegrationTest;
import com.infocargas.freteapp.domain.CategoriaVeiculo;
import com.infocargas.freteapp.domain.Frota;
import com.infocargas.freteapp.domain.Perfil;
import com.infocargas.freteapp.domain.enumeration.TipoCategoria;
import com.infocargas.freteapp.repository.FrotaRepository;
import com.infocargas.freteapp.service.criteria.FrotaCriteria;
import com.infocargas.freteapp.service.dto.FrotaDTO;
import com.infocargas.freteapp.service.mapper.FrotaMapper;
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
 * Integration tests for the {@link FrotaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FrotaResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_MODELO = "AAAAAAAAAA";
    private static final String UPDATED_MODELO = "BBBBBBBBBB";

    private static final String DEFAULT_MARCA = "AAAAAAAAAA";
    private static final String UPDATED_MARCA = "BBBBBBBBBB";

    private static final String DEFAULT_ANO = "AAAA";
    private static final String UPDATED_ANO = "BBBB";

    private static final TipoCategoria DEFAULT_TIPO_CATEGORIA = TipoCategoria.CEGONHA;
    private static final TipoCategoria UPDATED_TIPO_CATEGORIA = TipoCategoria.GUINCHO;

    private static final String DEFAULT_OBS_CATEGORIA_OUTRO = "AAAAAAAAAA";
    private static final String UPDATED_OBS_CATEGORIA_OUTRO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/frotas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FrotaRepository frotaRepository;

    @Autowired
    private FrotaMapper frotaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFrotaMockMvc;

    private Frota frota;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Frota createEntity(EntityManager em) {
        Frota frota = new Frota()
            .nome(DEFAULT_NOME)
            .modelo(DEFAULT_MODELO)
            .marca(DEFAULT_MARCA)
            .ano(DEFAULT_ANO)
            .tipoCategoria(DEFAULT_TIPO_CATEGORIA)
            .obsCategoriaOutro(DEFAULT_OBS_CATEGORIA_OUTRO);
        // Add required entity
        Perfil perfil;
        if (TestUtil.findAll(em, Perfil.class).isEmpty()) {
            perfil = PerfilResourceIT.createEntity(em);
            em.persist(perfil);
            em.flush();
        } else {
            perfil = TestUtil.findAll(em, Perfil.class).get(0);
        }
        frota.setPerfil(perfil);
        // Add required entity
        CategoriaVeiculo categoriaVeiculo;
        if (TestUtil.findAll(em, CategoriaVeiculo.class).isEmpty()) {
            categoriaVeiculo = CategoriaVeiculoResourceIT.createEntity(em);
            em.persist(categoriaVeiculo);
            em.flush();
        } else {
            categoriaVeiculo = TestUtil.findAll(em, CategoriaVeiculo.class).get(0);
        }
        frota.setCategoriaVeiculo(categoriaVeiculo);
        return frota;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Frota createUpdatedEntity(EntityManager em) {
        Frota frota = new Frota()
            .nome(UPDATED_NOME)
            .modelo(UPDATED_MODELO)
            .marca(UPDATED_MARCA)
            .ano(UPDATED_ANO)
            .tipoCategoria(UPDATED_TIPO_CATEGORIA)
            .obsCategoriaOutro(UPDATED_OBS_CATEGORIA_OUTRO);
        // Add required entity
        Perfil perfil;
        if (TestUtil.findAll(em, Perfil.class).isEmpty()) {
            perfil = PerfilResourceIT.createUpdatedEntity(em);
            em.persist(perfil);
            em.flush();
        } else {
            perfil = TestUtil.findAll(em, Perfil.class).get(0);
        }
        frota.setPerfil(perfil);
        // Add required entity
        CategoriaVeiculo categoriaVeiculo;
        if (TestUtil.findAll(em, CategoriaVeiculo.class).isEmpty()) {
            categoriaVeiculo = CategoriaVeiculoResourceIT.createUpdatedEntity(em);
            em.persist(categoriaVeiculo);
            em.flush();
        } else {
            categoriaVeiculo = TestUtil.findAll(em, CategoriaVeiculo.class).get(0);
        }
        frota.setCategoriaVeiculo(categoriaVeiculo);
        return frota;
    }

    @BeforeEach
    public void initTest() {
        frota = createEntity(em);
    }

    @Test
    @Transactional
    void createFrota() throws Exception {
        int databaseSizeBeforeCreate = frotaRepository.findAll().size();
        // Create the Frota
        FrotaDTO frotaDTO = frotaMapper.toDto(frota);
        restFrotaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(frotaDTO)))
            .andExpect(status().isCreated());

        // Validate the Frota in the database
        List<Frota> frotaList = frotaRepository.findAll();
        assertThat(frotaList).hasSize(databaseSizeBeforeCreate + 1);
        Frota testFrota = frotaList.get(frotaList.size() - 1);
        assertThat(testFrota.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testFrota.getModelo()).isEqualTo(DEFAULT_MODELO);
        assertThat(testFrota.getMarca()).isEqualTo(DEFAULT_MARCA);
        assertThat(testFrota.getAno()).isEqualTo(DEFAULT_ANO);
        assertThat(testFrota.getTipoCategoria()).isEqualTo(DEFAULT_TIPO_CATEGORIA);
        assertThat(testFrota.getObsCategoriaOutro()).isEqualTo(DEFAULT_OBS_CATEGORIA_OUTRO);
    }

    @Test
    @Transactional
    void createFrotaWithExistingId() throws Exception {
        // Create the Frota with an existing ID
        frota.setId(1L);
        FrotaDTO frotaDTO = frotaMapper.toDto(frota);

        int databaseSizeBeforeCreate = frotaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFrotaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(frotaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Frota in the database
        List<Frota> frotaList = frotaRepository.findAll();
        assertThat(frotaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = frotaRepository.findAll().size();
        // set the field null
        frota.setNome(null);

        // Create the Frota, which fails.
        FrotaDTO frotaDTO = frotaMapper.toDto(frota);

        restFrotaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(frotaDTO)))
            .andExpect(status().isBadRequest());

        List<Frota> frotaList = frotaRepository.findAll();
        assertThat(frotaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkModeloIsRequired() throws Exception {
        int databaseSizeBeforeTest = frotaRepository.findAll().size();
        // set the field null
        frota.setModelo(null);

        // Create the Frota, which fails.
        FrotaDTO frotaDTO = frotaMapper.toDto(frota);

        restFrotaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(frotaDTO)))
            .andExpect(status().isBadRequest());

        List<Frota> frotaList = frotaRepository.findAll();
        assertThat(frotaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAnoIsRequired() throws Exception {
        int databaseSizeBeforeTest = frotaRepository.findAll().size();
        // set the field null
        frota.setAno(null);

        // Create the Frota, which fails.
        FrotaDTO frotaDTO = frotaMapper.toDto(frota);

        restFrotaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(frotaDTO)))
            .andExpect(status().isBadRequest());

        List<Frota> frotaList = frotaRepository.findAll();
        assertThat(frotaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTipoCategoriaIsRequired() throws Exception {
        int databaseSizeBeforeTest = frotaRepository.findAll().size();
        // set the field null
        frota.setTipoCategoria(null);

        // Create the Frota, which fails.
        FrotaDTO frotaDTO = frotaMapper.toDto(frota);

        restFrotaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(frotaDTO)))
            .andExpect(status().isBadRequest());

        List<Frota> frotaList = frotaRepository.findAll();
        assertThat(frotaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFrotas() throws Exception {
        // Initialize the database
        frotaRepository.saveAndFlush(frota);

        // Get all the frotaList
        restFrotaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(frota.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].modelo").value(hasItem(DEFAULT_MODELO)))
            .andExpect(jsonPath("$.[*].marca").value(hasItem(DEFAULT_MARCA)))
            .andExpect(jsonPath("$.[*].ano").value(hasItem(DEFAULT_ANO)))
            .andExpect(jsonPath("$.[*].tipoCategoria").value(hasItem(DEFAULT_TIPO_CATEGORIA.toString())))
            .andExpect(jsonPath("$.[*].obsCategoriaOutro").value(hasItem(DEFAULT_OBS_CATEGORIA_OUTRO)));
    }

    @Test
    @Transactional
    void getFrota() throws Exception {
        // Initialize the database
        frotaRepository.saveAndFlush(frota);

        // Get the frota
        restFrotaMockMvc
            .perform(get(ENTITY_API_URL_ID, frota.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(frota.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.modelo").value(DEFAULT_MODELO))
            .andExpect(jsonPath("$.marca").value(DEFAULT_MARCA))
            .andExpect(jsonPath("$.ano").value(DEFAULT_ANO))
            .andExpect(jsonPath("$.tipoCategoria").value(DEFAULT_TIPO_CATEGORIA.toString()))
            .andExpect(jsonPath("$.obsCategoriaOutro").value(DEFAULT_OBS_CATEGORIA_OUTRO));
    }

    @Test
    @Transactional
    void getFrotasByIdFiltering() throws Exception {
        // Initialize the database
        frotaRepository.saveAndFlush(frota);

        Long id = frota.getId();

        defaultFrotaShouldBeFound("id.equals=" + id);
        defaultFrotaShouldNotBeFound("id.notEquals=" + id);

        defaultFrotaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFrotaShouldNotBeFound("id.greaterThan=" + id);

        defaultFrotaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFrotaShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFrotasByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        frotaRepository.saveAndFlush(frota);

        // Get all the frotaList where nome equals to DEFAULT_NOME
        defaultFrotaShouldBeFound("nome.equals=" + DEFAULT_NOME);

        // Get all the frotaList where nome equals to UPDATED_NOME
        defaultFrotaShouldNotBeFound("nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllFrotasByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        frotaRepository.saveAndFlush(frota);

        // Get all the frotaList where nome in DEFAULT_NOME or UPDATED_NOME
        defaultFrotaShouldBeFound("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME);

        // Get all the frotaList where nome equals to UPDATED_NOME
        defaultFrotaShouldNotBeFound("nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllFrotasByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        frotaRepository.saveAndFlush(frota);

        // Get all the frotaList where nome is not null
        defaultFrotaShouldBeFound("nome.specified=true");

        // Get all the frotaList where nome is null
        defaultFrotaShouldNotBeFound("nome.specified=false");
    }

    @Test
    @Transactional
    void getAllFrotasByNomeContainsSomething() throws Exception {
        // Initialize the database
        frotaRepository.saveAndFlush(frota);

        // Get all the frotaList where nome contains DEFAULT_NOME
        defaultFrotaShouldBeFound("nome.contains=" + DEFAULT_NOME);

        // Get all the frotaList where nome contains UPDATED_NOME
        defaultFrotaShouldNotBeFound("nome.contains=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllFrotasByNomeNotContainsSomething() throws Exception {
        // Initialize the database
        frotaRepository.saveAndFlush(frota);

        // Get all the frotaList where nome does not contain DEFAULT_NOME
        defaultFrotaShouldNotBeFound("nome.doesNotContain=" + DEFAULT_NOME);

        // Get all the frotaList where nome does not contain UPDATED_NOME
        defaultFrotaShouldBeFound("nome.doesNotContain=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllFrotasByModeloIsEqualToSomething() throws Exception {
        // Initialize the database
        frotaRepository.saveAndFlush(frota);

        // Get all the frotaList where modelo equals to DEFAULT_MODELO
        defaultFrotaShouldBeFound("modelo.equals=" + DEFAULT_MODELO);

        // Get all the frotaList where modelo equals to UPDATED_MODELO
        defaultFrotaShouldNotBeFound("modelo.equals=" + UPDATED_MODELO);
    }

    @Test
    @Transactional
    void getAllFrotasByModeloIsInShouldWork() throws Exception {
        // Initialize the database
        frotaRepository.saveAndFlush(frota);

        // Get all the frotaList where modelo in DEFAULT_MODELO or UPDATED_MODELO
        defaultFrotaShouldBeFound("modelo.in=" + DEFAULT_MODELO + "," + UPDATED_MODELO);

        // Get all the frotaList where modelo equals to UPDATED_MODELO
        defaultFrotaShouldNotBeFound("modelo.in=" + UPDATED_MODELO);
    }

    @Test
    @Transactional
    void getAllFrotasByModeloIsNullOrNotNull() throws Exception {
        // Initialize the database
        frotaRepository.saveAndFlush(frota);

        // Get all the frotaList where modelo is not null
        defaultFrotaShouldBeFound("modelo.specified=true");

        // Get all the frotaList where modelo is null
        defaultFrotaShouldNotBeFound("modelo.specified=false");
    }

    @Test
    @Transactional
    void getAllFrotasByModeloContainsSomething() throws Exception {
        // Initialize the database
        frotaRepository.saveAndFlush(frota);

        // Get all the frotaList where modelo contains DEFAULT_MODELO
        defaultFrotaShouldBeFound("modelo.contains=" + DEFAULT_MODELO);

        // Get all the frotaList where modelo contains UPDATED_MODELO
        defaultFrotaShouldNotBeFound("modelo.contains=" + UPDATED_MODELO);
    }

    @Test
    @Transactional
    void getAllFrotasByModeloNotContainsSomething() throws Exception {
        // Initialize the database
        frotaRepository.saveAndFlush(frota);

        // Get all the frotaList where modelo does not contain DEFAULT_MODELO
        defaultFrotaShouldNotBeFound("modelo.doesNotContain=" + DEFAULT_MODELO);

        // Get all the frotaList where modelo does not contain UPDATED_MODELO
        defaultFrotaShouldBeFound("modelo.doesNotContain=" + UPDATED_MODELO);
    }

    @Test
    @Transactional
    void getAllFrotasByMarcaIsEqualToSomething() throws Exception {
        // Initialize the database
        frotaRepository.saveAndFlush(frota);

        // Get all the frotaList where marca equals to DEFAULT_MARCA
        defaultFrotaShouldBeFound("marca.equals=" + DEFAULT_MARCA);

        // Get all the frotaList where marca equals to UPDATED_MARCA
        defaultFrotaShouldNotBeFound("marca.equals=" + UPDATED_MARCA);
    }

    @Test
    @Transactional
    void getAllFrotasByMarcaIsInShouldWork() throws Exception {
        // Initialize the database
        frotaRepository.saveAndFlush(frota);

        // Get all the frotaList where marca in DEFAULT_MARCA or UPDATED_MARCA
        defaultFrotaShouldBeFound("marca.in=" + DEFAULT_MARCA + "," + UPDATED_MARCA);

        // Get all the frotaList where marca equals to UPDATED_MARCA
        defaultFrotaShouldNotBeFound("marca.in=" + UPDATED_MARCA);
    }

    @Test
    @Transactional
    void getAllFrotasByMarcaIsNullOrNotNull() throws Exception {
        // Initialize the database
        frotaRepository.saveAndFlush(frota);

        // Get all the frotaList where marca is not null
        defaultFrotaShouldBeFound("marca.specified=true");

        // Get all the frotaList where marca is null
        defaultFrotaShouldNotBeFound("marca.specified=false");
    }

    @Test
    @Transactional
    void getAllFrotasByMarcaContainsSomething() throws Exception {
        // Initialize the database
        frotaRepository.saveAndFlush(frota);

        // Get all the frotaList where marca contains DEFAULT_MARCA
        defaultFrotaShouldBeFound("marca.contains=" + DEFAULT_MARCA);

        // Get all the frotaList where marca contains UPDATED_MARCA
        defaultFrotaShouldNotBeFound("marca.contains=" + UPDATED_MARCA);
    }

    @Test
    @Transactional
    void getAllFrotasByMarcaNotContainsSomething() throws Exception {
        // Initialize the database
        frotaRepository.saveAndFlush(frota);

        // Get all the frotaList where marca does not contain DEFAULT_MARCA
        defaultFrotaShouldNotBeFound("marca.doesNotContain=" + DEFAULT_MARCA);

        // Get all the frotaList where marca does not contain UPDATED_MARCA
        defaultFrotaShouldBeFound("marca.doesNotContain=" + UPDATED_MARCA);
    }

    @Test
    @Transactional
    void getAllFrotasByAnoIsEqualToSomething() throws Exception {
        // Initialize the database
        frotaRepository.saveAndFlush(frota);

        // Get all the frotaList where ano equals to DEFAULT_ANO
        defaultFrotaShouldBeFound("ano.equals=" + DEFAULT_ANO);

        // Get all the frotaList where ano equals to UPDATED_ANO
        defaultFrotaShouldNotBeFound("ano.equals=" + UPDATED_ANO);
    }

    @Test
    @Transactional
    void getAllFrotasByAnoIsInShouldWork() throws Exception {
        // Initialize the database
        frotaRepository.saveAndFlush(frota);

        // Get all the frotaList where ano in DEFAULT_ANO or UPDATED_ANO
        defaultFrotaShouldBeFound("ano.in=" + DEFAULT_ANO + "," + UPDATED_ANO);

        // Get all the frotaList where ano equals to UPDATED_ANO
        defaultFrotaShouldNotBeFound("ano.in=" + UPDATED_ANO);
    }

    @Test
    @Transactional
    void getAllFrotasByAnoIsNullOrNotNull() throws Exception {
        // Initialize the database
        frotaRepository.saveAndFlush(frota);

        // Get all the frotaList where ano is not null
        defaultFrotaShouldBeFound("ano.specified=true");

        // Get all the frotaList where ano is null
        defaultFrotaShouldNotBeFound("ano.specified=false");
    }

    @Test
    @Transactional
    void getAllFrotasByAnoContainsSomething() throws Exception {
        // Initialize the database
        frotaRepository.saveAndFlush(frota);

        // Get all the frotaList where ano contains DEFAULT_ANO
        defaultFrotaShouldBeFound("ano.contains=" + DEFAULT_ANO);

        // Get all the frotaList where ano contains UPDATED_ANO
        defaultFrotaShouldNotBeFound("ano.contains=" + UPDATED_ANO);
    }

    @Test
    @Transactional
    void getAllFrotasByAnoNotContainsSomething() throws Exception {
        // Initialize the database
        frotaRepository.saveAndFlush(frota);

        // Get all the frotaList where ano does not contain DEFAULT_ANO
        defaultFrotaShouldNotBeFound("ano.doesNotContain=" + DEFAULT_ANO);

        // Get all the frotaList where ano does not contain UPDATED_ANO
        defaultFrotaShouldBeFound("ano.doesNotContain=" + UPDATED_ANO);
    }

    @Test
    @Transactional
    void getAllFrotasByTipoCategoriaIsEqualToSomething() throws Exception {
        // Initialize the database
        frotaRepository.saveAndFlush(frota);

        // Get all the frotaList where tipoCategoria equals to DEFAULT_TIPO_CATEGORIA
        defaultFrotaShouldBeFound("tipoCategoria.equals=" + DEFAULT_TIPO_CATEGORIA);

        // Get all the frotaList where tipoCategoria equals to UPDATED_TIPO_CATEGORIA
        defaultFrotaShouldNotBeFound("tipoCategoria.equals=" + UPDATED_TIPO_CATEGORIA);
    }

    @Test
    @Transactional
    void getAllFrotasByTipoCategoriaIsInShouldWork() throws Exception {
        // Initialize the database
        frotaRepository.saveAndFlush(frota);

        // Get all the frotaList where tipoCategoria in DEFAULT_TIPO_CATEGORIA or UPDATED_TIPO_CATEGORIA
        defaultFrotaShouldBeFound("tipoCategoria.in=" + DEFAULT_TIPO_CATEGORIA + "," + UPDATED_TIPO_CATEGORIA);

        // Get all the frotaList where tipoCategoria equals to UPDATED_TIPO_CATEGORIA
        defaultFrotaShouldNotBeFound("tipoCategoria.in=" + UPDATED_TIPO_CATEGORIA);
    }

    @Test
    @Transactional
    void getAllFrotasByTipoCategoriaIsNullOrNotNull() throws Exception {
        // Initialize the database
        frotaRepository.saveAndFlush(frota);

        // Get all the frotaList where tipoCategoria is not null
        defaultFrotaShouldBeFound("tipoCategoria.specified=true");

        // Get all the frotaList where tipoCategoria is null
        defaultFrotaShouldNotBeFound("tipoCategoria.specified=false");
    }

    @Test
    @Transactional
    void getAllFrotasByObsCategoriaOutroIsEqualToSomething() throws Exception {
        // Initialize the database
        frotaRepository.saveAndFlush(frota);

        // Get all the frotaList where obsCategoriaOutro equals to DEFAULT_OBS_CATEGORIA_OUTRO
        defaultFrotaShouldBeFound("obsCategoriaOutro.equals=" + DEFAULT_OBS_CATEGORIA_OUTRO);

        // Get all the frotaList where obsCategoriaOutro equals to UPDATED_OBS_CATEGORIA_OUTRO
        defaultFrotaShouldNotBeFound("obsCategoriaOutro.equals=" + UPDATED_OBS_CATEGORIA_OUTRO);
    }

    @Test
    @Transactional
    void getAllFrotasByObsCategoriaOutroIsInShouldWork() throws Exception {
        // Initialize the database
        frotaRepository.saveAndFlush(frota);

        // Get all the frotaList where obsCategoriaOutro in DEFAULT_OBS_CATEGORIA_OUTRO or UPDATED_OBS_CATEGORIA_OUTRO
        defaultFrotaShouldBeFound("obsCategoriaOutro.in=" + DEFAULT_OBS_CATEGORIA_OUTRO + "," + UPDATED_OBS_CATEGORIA_OUTRO);

        // Get all the frotaList where obsCategoriaOutro equals to UPDATED_OBS_CATEGORIA_OUTRO
        defaultFrotaShouldNotBeFound("obsCategoriaOutro.in=" + UPDATED_OBS_CATEGORIA_OUTRO);
    }

    @Test
    @Transactional
    void getAllFrotasByObsCategoriaOutroIsNullOrNotNull() throws Exception {
        // Initialize the database
        frotaRepository.saveAndFlush(frota);

        // Get all the frotaList where obsCategoriaOutro is not null
        defaultFrotaShouldBeFound("obsCategoriaOutro.specified=true");

        // Get all the frotaList where obsCategoriaOutro is null
        defaultFrotaShouldNotBeFound("obsCategoriaOutro.specified=false");
    }

    @Test
    @Transactional
    void getAllFrotasByObsCategoriaOutroContainsSomething() throws Exception {
        // Initialize the database
        frotaRepository.saveAndFlush(frota);

        // Get all the frotaList where obsCategoriaOutro contains DEFAULT_OBS_CATEGORIA_OUTRO
        defaultFrotaShouldBeFound("obsCategoriaOutro.contains=" + DEFAULT_OBS_CATEGORIA_OUTRO);

        // Get all the frotaList where obsCategoriaOutro contains UPDATED_OBS_CATEGORIA_OUTRO
        defaultFrotaShouldNotBeFound("obsCategoriaOutro.contains=" + UPDATED_OBS_CATEGORIA_OUTRO);
    }

    @Test
    @Transactional
    void getAllFrotasByObsCategoriaOutroNotContainsSomething() throws Exception {
        // Initialize the database
        frotaRepository.saveAndFlush(frota);

        // Get all the frotaList where obsCategoriaOutro does not contain DEFAULT_OBS_CATEGORIA_OUTRO
        defaultFrotaShouldNotBeFound("obsCategoriaOutro.doesNotContain=" + DEFAULT_OBS_CATEGORIA_OUTRO);

        // Get all the frotaList where obsCategoriaOutro does not contain UPDATED_OBS_CATEGORIA_OUTRO
        defaultFrotaShouldBeFound("obsCategoriaOutro.doesNotContain=" + UPDATED_OBS_CATEGORIA_OUTRO);
    }

    @Test
    @Transactional
    void getAllFrotasByPerfilIsEqualToSomething() throws Exception {
        Perfil perfil;
        if (TestUtil.findAll(em, Perfil.class).isEmpty()) {
            frotaRepository.saveAndFlush(frota);
            perfil = PerfilResourceIT.createEntity(em);
        } else {
            perfil = TestUtil.findAll(em, Perfil.class).get(0);
        }
        em.persist(perfil);
        em.flush();
        frota.setPerfil(perfil);
        frotaRepository.saveAndFlush(frota);
        Long perfilId = perfil.getId();

        // Get all the frotaList where perfil equals to perfilId
        defaultFrotaShouldBeFound("perfilId.equals=" + perfilId);

        // Get all the frotaList where perfil equals to (perfilId + 1)
        defaultFrotaShouldNotBeFound("perfilId.equals=" + (perfilId + 1));
    }

    @Test
    @Transactional
    void getAllFrotasByCategoriaVeiculoIsEqualToSomething() throws Exception {
        CategoriaVeiculo categoriaVeiculo;
        if (TestUtil.findAll(em, CategoriaVeiculo.class).isEmpty()) {
            frotaRepository.saveAndFlush(frota);
            categoriaVeiculo = CategoriaVeiculoResourceIT.createEntity(em);
        } else {
            categoriaVeiculo = TestUtil.findAll(em, CategoriaVeiculo.class).get(0);
        }
        em.persist(categoriaVeiculo);
        em.flush();
        frota.setCategoriaVeiculo(categoriaVeiculo);
        frotaRepository.saveAndFlush(frota);
        Long categoriaVeiculoId = categoriaVeiculo.getId();

        // Get all the frotaList where categoriaVeiculo equals to categoriaVeiculoId
        defaultFrotaShouldBeFound("categoriaVeiculoId.equals=" + categoriaVeiculoId);

        // Get all the frotaList where categoriaVeiculo equals to (categoriaVeiculoId + 1)
        defaultFrotaShouldNotBeFound("categoriaVeiculoId.equals=" + (categoriaVeiculoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFrotaShouldBeFound(String filter) throws Exception {
        restFrotaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(frota.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].modelo").value(hasItem(DEFAULT_MODELO)))
            .andExpect(jsonPath("$.[*].marca").value(hasItem(DEFAULT_MARCA)))
            .andExpect(jsonPath("$.[*].ano").value(hasItem(DEFAULT_ANO)))
            .andExpect(jsonPath("$.[*].tipoCategoria").value(hasItem(DEFAULT_TIPO_CATEGORIA.toString())))
            .andExpect(jsonPath("$.[*].obsCategoriaOutro").value(hasItem(DEFAULT_OBS_CATEGORIA_OUTRO)));

        // Check, that the count call also returns 1
        restFrotaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFrotaShouldNotBeFound(String filter) throws Exception {
        restFrotaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFrotaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFrota() throws Exception {
        // Get the frota
        restFrotaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFrota() throws Exception {
        // Initialize the database
        frotaRepository.saveAndFlush(frota);

        int databaseSizeBeforeUpdate = frotaRepository.findAll().size();

        // Update the frota
        Frota updatedFrota = frotaRepository.findById(frota.getId()).get();
        // Disconnect from session so that the updates on updatedFrota are not directly saved in db
        em.detach(updatedFrota);
        updatedFrota
            .nome(UPDATED_NOME)
            .modelo(UPDATED_MODELO)
            .marca(UPDATED_MARCA)
            .ano(UPDATED_ANO)
            .tipoCategoria(UPDATED_TIPO_CATEGORIA)
            .obsCategoriaOutro(UPDATED_OBS_CATEGORIA_OUTRO);
        FrotaDTO frotaDTO = frotaMapper.toDto(updatedFrota);

        restFrotaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, frotaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(frotaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Frota in the database
        List<Frota> frotaList = frotaRepository.findAll();
        assertThat(frotaList).hasSize(databaseSizeBeforeUpdate);
        Frota testFrota = frotaList.get(frotaList.size() - 1);
        assertThat(testFrota.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testFrota.getModelo()).isEqualTo(UPDATED_MODELO);
        assertThat(testFrota.getMarca()).isEqualTo(UPDATED_MARCA);
        assertThat(testFrota.getAno()).isEqualTo(UPDATED_ANO);
        assertThat(testFrota.getTipoCategoria()).isEqualTo(UPDATED_TIPO_CATEGORIA);
        assertThat(testFrota.getObsCategoriaOutro()).isEqualTo(UPDATED_OBS_CATEGORIA_OUTRO);
    }

    @Test
    @Transactional
    void putNonExistingFrota() throws Exception {
        int databaseSizeBeforeUpdate = frotaRepository.findAll().size();
        frota.setId(count.incrementAndGet());

        // Create the Frota
        FrotaDTO frotaDTO = frotaMapper.toDto(frota);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFrotaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, frotaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(frotaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Frota in the database
        List<Frota> frotaList = frotaRepository.findAll();
        assertThat(frotaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFrota() throws Exception {
        int databaseSizeBeforeUpdate = frotaRepository.findAll().size();
        frota.setId(count.incrementAndGet());

        // Create the Frota
        FrotaDTO frotaDTO = frotaMapper.toDto(frota);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFrotaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(frotaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Frota in the database
        List<Frota> frotaList = frotaRepository.findAll();
        assertThat(frotaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFrota() throws Exception {
        int databaseSizeBeforeUpdate = frotaRepository.findAll().size();
        frota.setId(count.incrementAndGet());

        // Create the Frota
        FrotaDTO frotaDTO = frotaMapper.toDto(frota);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFrotaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(frotaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Frota in the database
        List<Frota> frotaList = frotaRepository.findAll();
        assertThat(frotaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFrotaWithPatch() throws Exception {
        // Initialize the database
        frotaRepository.saveAndFlush(frota);

        int databaseSizeBeforeUpdate = frotaRepository.findAll().size();

        // Update the frota using partial update
        Frota partialUpdatedFrota = new Frota();
        partialUpdatedFrota.setId(frota.getId());

        partialUpdatedFrota.modelo(UPDATED_MODELO).marca(UPDATED_MARCA).tipoCategoria(UPDATED_TIPO_CATEGORIA);

        restFrotaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFrota.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFrota))
            )
            .andExpect(status().isOk());

        // Validate the Frota in the database
        List<Frota> frotaList = frotaRepository.findAll();
        assertThat(frotaList).hasSize(databaseSizeBeforeUpdate);
        Frota testFrota = frotaList.get(frotaList.size() - 1);
        assertThat(testFrota.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testFrota.getModelo()).isEqualTo(UPDATED_MODELO);
        assertThat(testFrota.getMarca()).isEqualTo(UPDATED_MARCA);
        assertThat(testFrota.getAno()).isEqualTo(DEFAULT_ANO);
        assertThat(testFrota.getTipoCategoria()).isEqualTo(UPDATED_TIPO_CATEGORIA);
        assertThat(testFrota.getObsCategoriaOutro()).isEqualTo(DEFAULT_OBS_CATEGORIA_OUTRO);
    }

    @Test
    @Transactional
    void fullUpdateFrotaWithPatch() throws Exception {
        // Initialize the database
        frotaRepository.saveAndFlush(frota);

        int databaseSizeBeforeUpdate = frotaRepository.findAll().size();

        // Update the frota using partial update
        Frota partialUpdatedFrota = new Frota();
        partialUpdatedFrota.setId(frota.getId());

        partialUpdatedFrota
            .nome(UPDATED_NOME)
            .modelo(UPDATED_MODELO)
            .marca(UPDATED_MARCA)
            .ano(UPDATED_ANO)
            .tipoCategoria(UPDATED_TIPO_CATEGORIA)
            .obsCategoriaOutro(UPDATED_OBS_CATEGORIA_OUTRO);

        restFrotaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFrota.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFrota))
            )
            .andExpect(status().isOk());

        // Validate the Frota in the database
        List<Frota> frotaList = frotaRepository.findAll();
        assertThat(frotaList).hasSize(databaseSizeBeforeUpdate);
        Frota testFrota = frotaList.get(frotaList.size() - 1);
        assertThat(testFrota.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testFrota.getModelo()).isEqualTo(UPDATED_MODELO);
        assertThat(testFrota.getMarca()).isEqualTo(UPDATED_MARCA);
        assertThat(testFrota.getAno()).isEqualTo(UPDATED_ANO);
        assertThat(testFrota.getTipoCategoria()).isEqualTo(UPDATED_TIPO_CATEGORIA);
        assertThat(testFrota.getObsCategoriaOutro()).isEqualTo(UPDATED_OBS_CATEGORIA_OUTRO);
    }

    @Test
    @Transactional
    void patchNonExistingFrota() throws Exception {
        int databaseSizeBeforeUpdate = frotaRepository.findAll().size();
        frota.setId(count.incrementAndGet());

        // Create the Frota
        FrotaDTO frotaDTO = frotaMapper.toDto(frota);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFrotaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, frotaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(frotaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Frota in the database
        List<Frota> frotaList = frotaRepository.findAll();
        assertThat(frotaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFrota() throws Exception {
        int databaseSizeBeforeUpdate = frotaRepository.findAll().size();
        frota.setId(count.incrementAndGet());

        // Create the Frota
        FrotaDTO frotaDTO = frotaMapper.toDto(frota);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFrotaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(frotaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Frota in the database
        List<Frota> frotaList = frotaRepository.findAll();
        assertThat(frotaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFrota() throws Exception {
        int databaseSizeBeforeUpdate = frotaRepository.findAll().size();
        frota.setId(count.incrementAndGet());

        // Create the Frota
        FrotaDTO frotaDTO = frotaMapper.toDto(frota);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFrotaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(frotaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Frota in the database
        List<Frota> frotaList = frotaRepository.findAll();
        assertThat(frotaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFrota() throws Exception {
        // Initialize the database
        frotaRepository.saveAndFlush(frota);

        int databaseSizeBeforeDelete = frotaRepository.findAll().size();

        // Delete the frota
        restFrotaMockMvc
            .perform(delete(ENTITY_API_URL_ID, frota.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Frota> frotaList = frotaRepository.findAll();
        assertThat(frotaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

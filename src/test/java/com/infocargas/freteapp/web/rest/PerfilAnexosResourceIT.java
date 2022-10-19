package com.infocargas.freteapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.infocargas.freteapp.IntegrationTest;
import com.infocargas.freteapp.domain.Perfil;
import com.infocargas.freteapp.domain.PerfilAnexos;
import com.infocargas.freteapp.domain.enumeration.TipoPerfilDocumento;
import com.infocargas.freteapp.repository.PerfilAnexosRepository;
import com.infocargas.freteapp.service.criteria.PerfilAnexosCriteria;
import com.infocargas.freteapp.service.dto.PerfilAnexosDTO;
import com.infocargas.freteapp.service.mapper.PerfilAnexosMapper;
import java.time.LocalDate;
import java.time.ZoneId;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link PerfilAnexosResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PerfilAnexosResourceIT {

    private static final LocalDate DEFAULT_DATA_POSTAGEM = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_POSTAGEM = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATA_POSTAGEM = LocalDate.ofEpochDay(-1L);

    private static final TipoPerfilDocumento DEFAULT_TIPO_DOCUMENTO = TipoPerfilDocumento.ANTT;
    private static final TipoPerfilDocumento UPDATED_TIPO_DOCUMENTO = TipoPerfilDocumento.CNH;

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final byte[] DEFAULT_URL_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_URL_FILE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_URL_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_URL_FILE_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/perfil-anexos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PerfilAnexosRepository perfilAnexosRepository;

    @Autowired
    private PerfilAnexosMapper perfilAnexosMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPerfilAnexosMockMvc;

    private PerfilAnexos perfilAnexos;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PerfilAnexos createEntity(EntityManager em) {
        PerfilAnexos perfilAnexos = new PerfilAnexos()
            .dataPostagem(DEFAULT_DATA_POSTAGEM)
            .tipoDocumento(DEFAULT_TIPO_DOCUMENTO)
            .descricao(DEFAULT_DESCRICAO)
            .urlFile(DEFAULT_URL_FILE)
            .urlFileContentType(DEFAULT_URL_FILE_CONTENT_TYPE);
        // Add required entity
        Perfil perfil;
        if (TestUtil.findAll(em, Perfil.class).isEmpty()) {
            perfil = PerfilResourceIT.createEntity(em);
            em.persist(perfil);
            em.flush();
        } else {
            perfil = TestUtil.findAll(em, Perfil.class).get(0);
        }
        perfilAnexos.setPerfil(perfil);
        return perfilAnexos;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PerfilAnexos createUpdatedEntity(EntityManager em) {
        PerfilAnexos perfilAnexos = new PerfilAnexos()
            .dataPostagem(UPDATED_DATA_POSTAGEM)
            .tipoDocumento(UPDATED_TIPO_DOCUMENTO)
            .descricao(UPDATED_DESCRICAO)
            .urlFile(UPDATED_URL_FILE)
            .urlFileContentType(UPDATED_URL_FILE_CONTENT_TYPE);
        // Add required entity
        Perfil perfil;
        if (TestUtil.findAll(em, Perfil.class).isEmpty()) {
            perfil = PerfilResourceIT.createUpdatedEntity(em);
            em.persist(perfil);
            em.flush();
        } else {
            perfil = TestUtil.findAll(em, Perfil.class).get(0);
        }
        perfilAnexos.setPerfil(perfil);
        return perfilAnexos;
    }

    @BeforeEach
    public void initTest() {
        perfilAnexos = createEntity(em);
    }

    @Test
    @Transactional
    void createPerfilAnexos() throws Exception {
        int databaseSizeBeforeCreate = perfilAnexosRepository.findAll().size();
        // Create the PerfilAnexos
        PerfilAnexosDTO perfilAnexosDTO = perfilAnexosMapper.toDto(perfilAnexos);
        restPerfilAnexosMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(perfilAnexosDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PerfilAnexos in the database
        List<PerfilAnexos> perfilAnexosList = perfilAnexosRepository.findAll();
        assertThat(perfilAnexosList).hasSize(databaseSizeBeforeCreate + 1);
        PerfilAnexos testPerfilAnexos = perfilAnexosList.get(perfilAnexosList.size() - 1);
        assertThat(testPerfilAnexos.getDataPostagem()).isEqualTo(DEFAULT_DATA_POSTAGEM);
        assertThat(testPerfilAnexos.getTipoDocumento()).isEqualTo(DEFAULT_TIPO_DOCUMENTO);
        assertThat(testPerfilAnexos.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testPerfilAnexos.getUrlFile()).isEqualTo(DEFAULT_URL_FILE);
        assertThat(testPerfilAnexos.getUrlFileContentType()).isEqualTo(DEFAULT_URL_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createPerfilAnexosWithExistingId() throws Exception {
        // Create the PerfilAnexos with an existing ID
        perfilAnexos.setId(1L);
        PerfilAnexosDTO perfilAnexosDTO = perfilAnexosMapper.toDto(perfilAnexos);

        int databaseSizeBeforeCreate = perfilAnexosRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPerfilAnexosMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(perfilAnexosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PerfilAnexos in the database
        List<PerfilAnexos> perfilAnexosList = perfilAnexosRepository.findAll();
        assertThat(perfilAnexosList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDataPostagemIsRequired() throws Exception {
        int databaseSizeBeforeTest = perfilAnexosRepository.findAll().size();
        // set the field null
        perfilAnexos.setDataPostagem(null);

        // Create the PerfilAnexos, which fails.
        PerfilAnexosDTO perfilAnexosDTO = perfilAnexosMapper.toDto(perfilAnexos);

        restPerfilAnexosMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(perfilAnexosDTO))
            )
            .andExpect(status().isBadRequest());

        List<PerfilAnexos> perfilAnexosList = perfilAnexosRepository.findAll();
        assertThat(perfilAnexosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTipoDocumentoIsRequired() throws Exception {
        int databaseSizeBeforeTest = perfilAnexosRepository.findAll().size();
        // set the field null
        perfilAnexos.setTipoDocumento(null);

        // Create the PerfilAnexos, which fails.
        PerfilAnexosDTO perfilAnexosDTO = perfilAnexosMapper.toDto(perfilAnexos);

        restPerfilAnexosMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(perfilAnexosDTO))
            )
            .andExpect(status().isBadRequest());

        List<PerfilAnexos> perfilAnexosList = perfilAnexosRepository.findAll();
        assertThat(perfilAnexosList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPerfilAnexos() throws Exception {
        // Initialize the database
        perfilAnexosRepository.saveAndFlush(perfilAnexos);

        // Get all the perfilAnexosList
        restPerfilAnexosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(perfilAnexos.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataPostagem").value(hasItem(DEFAULT_DATA_POSTAGEM.toString())))
            .andExpect(jsonPath("$.[*].tipoDocumento").value(hasItem(DEFAULT_TIPO_DOCUMENTO.toString())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].urlFileContentType").value(hasItem(DEFAULT_URL_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].urlFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_URL_FILE))));
    }

    @Test
    @Transactional
    void getPerfilAnexos() throws Exception {
        // Initialize the database
        perfilAnexosRepository.saveAndFlush(perfilAnexos);

        // Get the perfilAnexos
        restPerfilAnexosMockMvc
            .perform(get(ENTITY_API_URL_ID, perfilAnexos.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(perfilAnexos.getId().intValue()))
            .andExpect(jsonPath("$.dataPostagem").value(DEFAULT_DATA_POSTAGEM.toString()))
            .andExpect(jsonPath("$.tipoDocumento").value(DEFAULT_TIPO_DOCUMENTO.toString()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO))
            .andExpect(jsonPath("$.urlFileContentType").value(DEFAULT_URL_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.urlFile").value(Base64Utils.encodeToString(DEFAULT_URL_FILE)));
    }

    @Test
    @Transactional
    void getPerfilAnexosByIdFiltering() throws Exception {
        // Initialize the database
        perfilAnexosRepository.saveAndFlush(perfilAnexos);

        Long id = perfilAnexos.getId();

        defaultPerfilAnexosShouldBeFound("id.equals=" + id);
        defaultPerfilAnexosShouldNotBeFound("id.notEquals=" + id);

        defaultPerfilAnexosShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPerfilAnexosShouldNotBeFound("id.greaterThan=" + id);

        defaultPerfilAnexosShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPerfilAnexosShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPerfilAnexosByDataPostagemIsEqualToSomething() throws Exception {
        // Initialize the database
        perfilAnexosRepository.saveAndFlush(perfilAnexos);

        // Get all the perfilAnexosList where dataPostagem equals to DEFAULT_DATA_POSTAGEM
        defaultPerfilAnexosShouldBeFound("dataPostagem.equals=" + DEFAULT_DATA_POSTAGEM);

        // Get all the perfilAnexosList where dataPostagem equals to UPDATED_DATA_POSTAGEM
        defaultPerfilAnexosShouldNotBeFound("dataPostagem.equals=" + UPDATED_DATA_POSTAGEM);
    }

    @Test
    @Transactional
    void getAllPerfilAnexosByDataPostagemIsInShouldWork() throws Exception {
        // Initialize the database
        perfilAnexosRepository.saveAndFlush(perfilAnexos);

        // Get all the perfilAnexosList where dataPostagem in DEFAULT_DATA_POSTAGEM or UPDATED_DATA_POSTAGEM
        defaultPerfilAnexosShouldBeFound("dataPostagem.in=" + DEFAULT_DATA_POSTAGEM + "," + UPDATED_DATA_POSTAGEM);

        // Get all the perfilAnexosList where dataPostagem equals to UPDATED_DATA_POSTAGEM
        defaultPerfilAnexosShouldNotBeFound("dataPostagem.in=" + UPDATED_DATA_POSTAGEM);
    }

    @Test
    @Transactional
    void getAllPerfilAnexosByDataPostagemIsNullOrNotNull() throws Exception {
        // Initialize the database
        perfilAnexosRepository.saveAndFlush(perfilAnexos);

        // Get all the perfilAnexosList where dataPostagem is not null
        defaultPerfilAnexosShouldBeFound("dataPostagem.specified=true");

        // Get all the perfilAnexosList where dataPostagem is null
        defaultPerfilAnexosShouldNotBeFound("dataPostagem.specified=false");
    }

    @Test
    @Transactional
    void getAllPerfilAnexosByDataPostagemIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        perfilAnexosRepository.saveAndFlush(perfilAnexos);

        // Get all the perfilAnexosList where dataPostagem is greater than or equal to DEFAULT_DATA_POSTAGEM
        defaultPerfilAnexosShouldBeFound("dataPostagem.greaterThanOrEqual=" + DEFAULT_DATA_POSTAGEM);

        // Get all the perfilAnexosList where dataPostagem is greater than or equal to UPDATED_DATA_POSTAGEM
        defaultPerfilAnexosShouldNotBeFound("dataPostagem.greaterThanOrEqual=" + UPDATED_DATA_POSTAGEM);
    }

    @Test
    @Transactional
    void getAllPerfilAnexosByDataPostagemIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        perfilAnexosRepository.saveAndFlush(perfilAnexos);

        // Get all the perfilAnexosList where dataPostagem is less than or equal to DEFAULT_DATA_POSTAGEM
        defaultPerfilAnexosShouldBeFound("dataPostagem.lessThanOrEqual=" + DEFAULT_DATA_POSTAGEM);

        // Get all the perfilAnexosList where dataPostagem is less than or equal to SMALLER_DATA_POSTAGEM
        defaultPerfilAnexosShouldNotBeFound("dataPostagem.lessThanOrEqual=" + SMALLER_DATA_POSTAGEM);
    }

    @Test
    @Transactional
    void getAllPerfilAnexosByDataPostagemIsLessThanSomething() throws Exception {
        // Initialize the database
        perfilAnexosRepository.saveAndFlush(perfilAnexos);

        // Get all the perfilAnexosList where dataPostagem is less than DEFAULT_DATA_POSTAGEM
        defaultPerfilAnexosShouldNotBeFound("dataPostagem.lessThan=" + DEFAULT_DATA_POSTAGEM);

        // Get all the perfilAnexosList where dataPostagem is less than UPDATED_DATA_POSTAGEM
        defaultPerfilAnexosShouldBeFound("dataPostagem.lessThan=" + UPDATED_DATA_POSTAGEM);
    }

    @Test
    @Transactional
    void getAllPerfilAnexosByDataPostagemIsGreaterThanSomething() throws Exception {
        // Initialize the database
        perfilAnexosRepository.saveAndFlush(perfilAnexos);

        // Get all the perfilAnexosList where dataPostagem is greater than DEFAULT_DATA_POSTAGEM
        defaultPerfilAnexosShouldNotBeFound("dataPostagem.greaterThan=" + DEFAULT_DATA_POSTAGEM);

        // Get all the perfilAnexosList where dataPostagem is greater than SMALLER_DATA_POSTAGEM
        defaultPerfilAnexosShouldBeFound("dataPostagem.greaterThan=" + SMALLER_DATA_POSTAGEM);
    }

    @Test
    @Transactional
    void getAllPerfilAnexosByTipoDocumentoIsEqualToSomething() throws Exception {
        // Initialize the database
        perfilAnexosRepository.saveAndFlush(perfilAnexos);

        // Get all the perfilAnexosList where tipoDocumento equals to DEFAULT_TIPO_DOCUMENTO
        defaultPerfilAnexosShouldBeFound("tipoDocumento.equals=" + DEFAULT_TIPO_DOCUMENTO);

        // Get all the perfilAnexosList where tipoDocumento equals to UPDATED_TIPO_DOCUMENTO
        defaultPerfilAnexosShouldNotBeFound("tipoDocumento.equals=" + UPDATED_TIPO_DOCUMENTO);
    }

    @Test
    @Transactional
    void getAllPerfilAnexosByTipoDocumentoIsInShouldWork() throws Exception {
        // Initialize the database
        perfilAnexosRepository.saveAndFlush(perfilAnexos);

        // Get all the perfilAnexosList where tipoDocumento in DEFAULT_TIPO_DOCUMENTO or UPDATED_TIPO_DOCUMENTO
        defaultPerfilAnexosShouldBeFound("tipoDocumento.in=" + DEFAULT_TIPO_DOCUMENTO + "," + UPDATED_TIPO_DOCUMENTO);

        // Get all the perfilAnexosList where tipoDocumento equals to UPDATED_TIPO_DOCUMENTO
        defaultPerfilAnexosShouldNotBeFound("tipoDocumento.in=" + UPDATED_TIPO_DOCUMENTO);
    }

    @Test
    @Transactional
    void getAllPerfilAnexosByTipoDocumentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        perfilAnexosRepository.saveAndFlush(perfilAnexos);

        // Get all the perfilAnexosList where tipoDocumento is not null
        defaultPerfilAnexosShouldBeFound("tipoDocumento.specified=true");

        // Get all the perfilAnexosList where tipoDocumento is null
        defaultPerfilAnexosShouldNotBeFound("tipoDocumento.specified=false");
    }

    @Test
    @Transactional
    void getAllPerfilAnexosByDescricaoIsEqualToSomething() throws Exception {
        // Initialize the database
        perfilAnexosRepository.saveAndFlush(perfilAnexos);

        // Get all the perfilAnexosList where descricao equals to DEFAULT_DESCRICAO
        defaultPerfilAnexosShouldBeFound("descricao.equals=" + DEFAULT_DESCRICAO);

        // Get all the perfilAnexosList where descricao equals to UPDATED_DESCRICAO
        defaultPerfilAnexosShouldNotBeFound("descricao.equals=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllPerfilAnexosByDescricaoIsInShouldWork() throws Exception {
        // Initialize the database
        perfilAnexosRepository.saveAndFlush(perfilAnexos);

        // Get all the perfilAnexosList where descricao in DEFAULT_DESCRICAO or UPDATED_DESCRICAO
        defaultPerfilAnexosShouldBeFound("descricao.in=" + DEFAULT_DESCRICAO + "," + UPDATED_DESCRICAO);

        // Get all the perfilAnexosList where descricao equals to UPDATED_DESCRICAO
        defaultPerfilAnexosShouldNotBeFound("descricao.in=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllPerfilAnexosByDescricaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        perfilAnexosRepository.saveAndFlush(perfilAnexos);

        // Get all the perfilAnexosList where descricao is not null
        defaultPerfilAnexosShouldBeFound("descricao.specified=true");

        // Get all the perfilAnexosList where descricao is null
        defaultPerfilAnexosShouldNotBeFound("descricao.specified=false");
    }

    @Test
    @Transactional
    void getAllPerfilAnexosByDescricaoContainsSomething() throws Exception {
        // Initialize the database
        perfilAnexosRepository.saveAndFlush(perfilAnexos);

        // Get all the perfilAnexosList where descricao contains DEFAULT_DESCRICAO
        defaultPerfilAnexosShouldBeFound("descricao.contains=" + DEFAULT_DESCRICAO);

        // Get all the perfilAnexosList where descricao contains UPDATED_DESCRICAO
        defaultPerfilAnexosShouldNotBeFound("descricao.contains=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllPerfilAnexosByDescricaoNotContainsSomething() throws Exception {
        // Initialize the database
        perfilAnexosRepository.saveAndFlush(perfilAnexos);

        // Get all the perfilAnexosList where descricao does not contain DEFAULT_DESCRICAO
        defaultPerfilAnexosShouldNotBeFound("descricao.doesNotContain=" + DEFAULT_DESCRICAO);

        // Get all the perfilAnexosList where descricao does not contain UPDATED_DESCRICAO
        defaultPerfilAnexosShouldBeFound("descricao.doesNotContain=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllPerfilAnexosByPerfilIsEqualToSomething() throws Exception {
        Perfil perfil;
        if (TestUtil.findAll(em, Perfil.class).isEmpty()) {
            perfilAnexosRepository.saveAndFlush(perfilAnexos);
            perfil = PerfilResourceIT.createEntity(em);
        } else {
            perfil = TestUtil.findAll(em, Perfil.class).get(0);
        }
        em.persist(perfil);
        em.flush();
        perfilAnexos.setPerfil(perfil);
        perfilAnexosRepository.saveAndFlush(perfilAnexos);
        Long perfilId = perfil.getId();

        // Get all the perfilAnexosList where perfil equals to perfilId
        defaultPerfilAnexosShouldBeFound("perfilId.equals=" + perfilId);

        // Get all the perfilAnexosList where perfil equals to (perfilId + 1)
        defaultPerfilAnexosShouldNotBeFound("perfilId.equals=" + (perfilId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPerfilAnexosShouldBeFound(String filter) throws Exception {
        restPerfilAnexosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(perfilAnexos.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataPostagem").value(hasItem(DEFAULT_DATA_POSTAGEM.toString())))
            .andExpect(jsonPath("$.[*].tipoDocumento").value(hasItem(DEFAULT_TIPO_DOCUMENTO.toString())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].urlFileContentType").value(hasItem(DEFAULT_URL_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].urlFile").value(hasItem(Base64Utils.encodeToString(DEFAULT_URL_FILE))));

        // Check, that the count call also returns 1
        restPerfilAnexosMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPerfilAnexosShouldNotBeFound(String filter) throws Exception {
        restPerfilAnexosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPerfilAnexosMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPerfilAnexos() throws Exception {
        // Get the perfilAnexos
        restPerfilAnexosMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPerfilAnexos() throws Exception {
        // Initialize the database
        perfilAnexosRepository.saveAndFlush(perfilAnexos);

        int databaseSizeBeforeUpdate = perfilAnexosRepository.findAll().size();

        // Update the perfilAnexos
        PerfilAnexos updatedPerfilAnexos = perfilAnexosRepository.findById(perfilAnexos.getId()).get();
        // Disconnect from session so that the updates on updatedPerfilAnexos are not directly saved in db
        em.detach(updatedPerfilAnexos);
        updatedPerfilAnexos
            .dataPostagem(UPDATED_DATA_POSTAGEM)
            .tipoDocumento(UPDATED_TIPO_DOCUMENTO)
            .descricao(UPDATED_DESCRICAO)
            .urlFile(UPDATED_URL_FILE)
            .urlFileContentType(UPDATED_URL_FILE_CONTENT_TYPE);
        PerfilAnexosDTO perfilAnexosDTO = perfilAnexosMapper.toDto(updatedPerfilAnexos);

        restPerfilAnexosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, perfilAnexosDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(perfilAnexosDTO))
            )
            .andExpect(status().isOk());

        // Validate the PerfilAnexos in the database
        List<PerfilAnexos> perfilAnexosList = perfilAnexosRepository.findAll();
        assertThat(perfilAnexosList).hasSize(databaseSizeBeforeUpdate);
        PerfilAnexos testPerfilAnexos = perfilAnexosList.get(perfilAnexosList.size() - 1);
        assertThat(testPerfilAnexos.getDataPostagem()).isEqualTo(UPDATED_DATA_POSTAGEM);
        assertThat(testPerfilAnexos.getTipoDocumento()).isEqualTo(UPDATED_TIPO_DOCUMENTO);
        assertThat(testPerfilAnexos.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testPerfilAnexos.getUrlFile()).isEqualTo(UPDATED_URL_FILE);
        assertThat(testPerfilAnexos.getUrlFileContentType()).isEqualTo(UPDATED_URL_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingPerfilAnexos() throws Exception {
        int databaseSizeBeforeUpdate = perfilAnexosRepository.findAll().size();
        perfilAnexos.setId(count.incrementAndGet());

        // Create the PerfilAnexos
        PerfilAnexosDTO perfilAnexosDTO = perfilAnexosMapper.toDto(perfilAnexos);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPerfilAnexosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, perfilAnexosDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(perfilAnexosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PerfilAnexos in the database
        List<PerfilAnexos> perfilAnexosList = perfilAnexosRepository.findAll();
        assertThat(perfilAnexosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPerfilAnexos() throws Exception {
        int databaseSizeBeforeUpdate = perfilAnexosRepository.findAll().size();
        perfilAnexos.setId(count.incrementAndGet());

        // Create the PerfilAnexos
        PerfilAnexosDTO perfilAnexosDTO = perfilAnexosMapper.toDto(perfilAnexos);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPerfilAnexosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(perfilAnexosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PerfilAnexos in the database
        List<PerfilAnexos> perfilAnexosList = perfilAnexosRepository.findAll();
        assertThat(perfilAnexosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPerfilAnexos() throws Exception {
        int databaseSizeBeforeUpdate = perfilAnexosRepository.findAll().size();
        perfilAnexos.setId(count.incrementAndGet());

        // Create the PerfilAnexos
        PerfilAnexosDTO perfilAnexosDTO = perfilAnexosMapper.toDto(perfilAnexos);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPerfilAnexosMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(perfilAnexosDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PerfilAnexos in the database
        List<PerfilAnexos> perfilAnexosList = perfilAnexosRepository.findAll();
        assertThat(perfilAnexosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePerfilAnexosWithPatch() throws Exception {
        // Initialize the database
        perfilAnexosRepository.saveAndFlush(perfilAnexos);

        int databaseSizeBeforeUpdate = perfilAnexosRepository.findAll().size();

        // Update the perfilAnexos using partial update
        PerfilAnexos partialUpdatedPerfilAnexos = new PerfilAnexos();
        partialUpdatedPerfilAnexos.setId(perfilAnexos.getId());

        partialUpdatedPerfilAnexos.tipoDocumento(UPDATED_TIPO_DOCUMENTO);

        restPerfilAnexosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPerfilAnexos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPerfilAnexos))
            )
            .andExpect(status().isOk());

        // Validate the PerfilAnexos in the database
        List<PerfilAnexos> perfilAnexosList = perfilAnexosRepository.findAll();
        assertThat(perfilAnexosList).hasSize(databaseSizeBeforeUpdate);
        PerfilAnexos testPerfilAnexos = perfilAnexosList.get(perfilAnexosList.size() - 1);
        assertThat(testPerfilAnexos.getDataPostagem()).isEqualTo(DEFAULT_DATA_POSTAGEM);
        assertThat(testPerfilAnexos.getTipoDocumento()).isEqualTo(UPDATED_TIPO_DOCUMENTO);
        assertThat(testPerfilAnexos.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testPerfilAnexos.getUrlFile()).isEqualTo(DEFAULT_URL_FILE);
        assertThat(testPerfilAnexos.getUrlFileContentType()).isEqualTo(DEFAULT_URL_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdatePerfilAnexosWithPatch() throws Exception {
        // Initialize the database
        perfilAnexosRepository.saveAndFlush(perfilAnexos);

        int databaseSizeBeforeUpdate = perfilAnexosRepository.findAll().size();

        // Update the perfilAnexos using partial update
        PerfilAnexos partialUpdatedPerfilAnexos = new PerfilAnexos();
        partialUpdatedPerfilAnexos.setId(perfilAnexos.getId());

        partialUpdatedPerfilAnexos
            .dataPostagem(UPDATED_DATA_POSTAGEM)
            .tipoDocumento(UPDATED_TIPO_DOCUMENTO)
            .descricao(UPDATED_DESCRICAO)
            .urlFile(UPDATED_URL_FILE)
            .urlFileContentType(UPDATED_URL_FILE_CONTENT_TYPE);

        restPerfilAnexosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPerfilAnexos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPerfilAnexos))
            )
            .andExpect(status().isOk());

        // Validate the PerfilAnexos in the database
        List<PerfilAnexos> perfilAnexosList = perfilAnexosRepository.findAll();
        assertThat(perfilAnexosList).hasSize(databaseSizeBeforeUpdate);
        PerfilAnexos testPerfilAnexos = perfilAnexosList.get(perfilAnexosList.size() - 1);
        assertThat(testPerfilAnexos.getDataPostagem()).isEqualTo(UPDATED_DATA_POSTAGEM);
        assertThat(testPerfilAnexos.getTipoDocumento()).isEqualTo(UPDATED_TIPO_DOCUMENTO);
        assertThat(testPerfilAnexos.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testPerfilAnexos.getUrlFile()).isEqualTo(UPDATED_URL_FILE);
        assertThat(testPerfilAnexos.getUrlFileContentType()).isEqualTo(UPDATED_URL_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingPerfilAnexos() throws Exception {
        int databaseSizeBeforeUpdate = perfilAnexosRepository.findAll().size();
        perfilAnexos.setId(count.incrementAndGet());

        // Create the PerfilAnexos
        PerfilAnexosDTO perfilAnexosDTO = perfilAnexosMapper.toDto(perfilAnexos);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPerfilAnexosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, perfilAnexosDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(perfilAnexosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PerfilAnexos in the database
        List<PerfilAnexos> perfilAnexosList = perfilAnexosRepository.findAll();
        assertThat(perfilAnexosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPerfilAnexos() throws Exception {
        int databaseSizeBeforeUpdate = perfilAnexosRepository.findAll().size();
        perfilAnexos.setId(count.incrementAndGet());

        // Create the PerfilAnexos
        PerfilAnexosDTO perfilAnexosDTO = perfilAnexosMapper.toDto(perfilAnexos);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPerfilAnexosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(perfilAnexosDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PerfilAnexos in the database
        List<PerfilAnexos> perfilAnexosList = perfilAnexosRepository.findAll();
        assertThat(perfilAnexosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPerfilAnexos() throws Exception {
        int databaseSizeBeforeUpdate = perfilAnexosRepository.findAll().size();
        perfilAnexos.setId(count.incrementAndGet());

        // Create the PerfilAnexos
        PerfilAnexosDTO perfilAnexosDTO = perfilAnexosMapper.toDto(perfilAnexos);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPerfilAnexosMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(perfilAnexosDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PerfilAnexos in the database
        List<PerfilAnexos> perfilAnexosList = perfilAnexosRepository.findAll();
        assertThat(perfilAnexosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePerfilAnexos() throws Exception {
        // Initialize the database
        perfilAnexosRepository.saveAndFlush(perfilAnexos);

        int databaseSizeBeforeDelete = perfilAnexosRepository.findAll().size();

        // Delete the perfilAnexos
        restPerfilAnexosMockMvc
            .perform(delete(ENTITY_API_URL_ID, perfilAnexos.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PerfilAnexos> perfilAnexosList = perfilAnexosRepository.findAll();
        assertThat(perfilAnexosList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

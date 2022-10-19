package com.infocargas.freteapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.infocargas.freteapp.IntegrationTest;
import com.infocargas.freteapp.domain.Perfil;
import com.infocargas.freteapp.domain.User;
import com.infocargas.freteapp.domain.enumeration.TipoConta;
import com.infocargas.freteapp.repository.PerfilRepository;
import com.infocargas.freteapp.service.PerfilService;
import com.infocargas.freteapp.service.criteria.PerfilCriteria;
import com.infocargas.freteapp.service.dto.PerfilDTO;
import com.infocargas.freteapp.service.mapper.PerfilMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PerfilResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PerfilResourceIT {

    private static final TipoConta DEFAULT_TIPO_CONTA = TipoConta.MOTORISTA;
    private static final TipoConta UPDATED_TIPO_CONTA = TipoConta.TRANSPORTADORA;

    private static final String DEFAULT_CPF = "AAAAAAAAAAA";
    private static final String UPDATED_CPF = "BBBBBBBBBBB";

    private static final String DEFAULT_CNPJ = "AAAAAAAAAAAAAA";
    private static final String UPDATED_CNPJ = "BBBBBBBBBBBBBB";

    private static final String DEFAULT_RUA = "AAAAAAAAAA";
    private static final String UPDATED_RUA = "BBBBBBBBBB";

    private static final String DEFAULT_NUMERO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO = "BBBBBBBBBB";

    private static final String DEFAULT_BAIRRO = "AAAAAAAAAA";
    private static final String UPDATED_BAIRRO = "BBBBBBBBBB";

    private static final String DEFAULT_CIDADE = "AAAAAAAAAA";
    private static final String UPDATED_CIDADE = "BBBBBBBBBB";

    private static final String DEFAULT_ESTADO = "AAAAAAAAAA";
    private static final String UPDATED_ESTADO = "BBBBBBBBBB";

    private static final String DEFAULT_CEP = "AAAAAAAAAA";
    private static final String UPDATED_CEP = "BBBBBBBBBB";

    private static final String DEFAULT_PAIS = "AAAAAAAAAA";
    private static final String UPDATED_PAIS = "BBBBBBBBBB";

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_RAZAOSOCIAL = "AAAAAAAAAA";
    private static final String UPDATED_RAZAOSOCIAL = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONE_COMERCIAL = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONE_COMERCIAL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/perfils";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PerfilRepository perfilRepository;

    @Mock
    private PerfilRepository perfilRepositoryMock;

    @Autowired
    private PerfilMapper perfilMapper;

    @Mock
    private PerfilService perfilServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPerfilMockMvc;

    private Perfil perfil;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Perfil createEntity(EntityManager em) {
        Perfil perfil = new Perfil()
            .tipoConta(DEFAULT_TIPO_CONTA)
            .cpf(DEFAULT_CPF)
            .cnpj(DEFAULT_CNPJ)
            .rua(DEFAULT_RUA)
            .numero(DEFAULT_NUMERO)
            .bairro(DEFAULT_BAIRRO)
            .cidade(DEFAULT_CIDADE)
            .estado(DEFAULT_ESTADO)
            .cep(DEFAULT_CEP)
            .pais(DEFAULT_PAIS)
            .nome(DEFAULT_NOME)
            .razaosocial(DEFAULT_RAZAOSOCIAL)
            .telefoneComercial(DEFAULT_TELEFONE_COMERCIAL);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        perfil.setUser(user);
        return perfil;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Perfil createUpdatedEntity(EntityManager em) {
        Perfil perfil = new Perfil()
            .tipoConta(UPDATED_TIPO_CONTA)
            .cpf(UPDATED_CPF)
            .cnpj(UPDATED_CNPJ)
            .rua(UPDATED_RUA)
            .numero(UPDATED_NUMERO)
            .bairro(UPDATED_BAIRRO)
            .cidade(UPDATED_CIDADE)
            .estado(UPDATED_ESTADO)
            .cep(UPDATED_CEP)
            .pais(UPDATED_PAIS)
            .nome(UPDATED_NOME)
            .razaosocial(UPDATED_RAZAOSOCIAL)
            .telefoneComercial(UPDATED_TELEFONE_COMERCIAL);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        perfil.setUser(user);
        return perfil;
    }

    @BeforeEach
    public void initTest() {
        perfil = createEntity(em);
    }

    @Test
    @Transactional
    void createPerfil() throws Exception {
        int databaseSizeBeforeCreate = perfilRepository.findAll().size();
        // Create the Perfil
        PerfilDTO perfilDTO = perfilMapper.toDto(perfil);
        restPerfilMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(perfilDTO)))
            .andExpect(status().isCreated());

        // Validate the Perfil in the database
        List<Perfil> perfilList = perfilRepository.findAll();
        assertThat(perfilList).hasSize(databaseSizeBeforeCreate + 1);
        Perfil testPerfil = perfilList.get(perfilList.size() - 1);
        assertThat(testPerfil.getTipoConta()).isEqualTo(DEFAULT_TIPO_CONTA);
        assertThat(testPerfil.getCpf()).isEqualTo(DEFAULT_CPF);
        assertThat(testPerfil.getCnpj()).isEqualTo(DEFAULT_CNPJ);
        assertThat(testPerfil.getRua()).isEqualTo(DEFAULT_RUA);
        assertThat(testPerfil.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testPerfil.getBairro()).isEqualTo(DEFAULT_BAIRRO);
        assertThat(testPerfil.getCidade()).isEqualTo(DEFAULT_CIDADE);
        assertThat(testPerfil.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testPerfil.getCep()).isEqualTo(DEFAULT_CEP);
        assertThat(testPerfil.getPais()).isEqualTo(DEFAULT_PAIS);
        assertThat(testPerfil.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testPerfil.getRazaosocial()).isEqualTo(DEFAULT_RAZAOSOCIAL);
        assertThat(testPerfil.getTelefoneComercial()).isEqualTo(DEFAULT_TELEFONE_COMERCIAL);
    }

    @Test
    @Transactional
    void createPerfilWithExistingId() throws Exception {
        // Create the Perfil with an existing ID
        perfil.setId(1L);
        PerfilDTO perfilDTO = perfilMapper.toDto(perfil);

        int databaseSizeBeforeCreate = perfilRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPerfilMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(perfilDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Perfil in the database
        List<Perfil> perfilList = perfilRepository.findAll();
        assertThat(perfilList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTipoContaIsRequired() throws Exception {
        int databaseSizeBeforeTest = perfilRepository.findAll().size();
        // set the field null
        perfil.setTipoConta(null);

        // Create the Perfil, which fails.
        PerfilDTO perfilDTO = perfilMapper.toDto(perfil);

        restPerfilMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(perfilDTO)))
            .andExpect(status().isBadRequest());

        List<Perfil> perfilList = perfilRepository.findAll();
        assertThat(perfilList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = perfilRepository.findAll().size();
        // set the field null
        perfil.setNome(null);

        // Create the Perfil, which fails.
        PerfilDTO perfilDTO = perfilMapper.toDto(perfil);

        restPerfilMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(perfilDTO)))
            .andExpect(status().isBadRequest());

        List<Perfil> perfilList = perfilRepository.findAll();
        assertThat(perfilList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPerfils() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        // Get all the perfilList
        restPerfilMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(perfil.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipoConta").value(hasItem(DEFAULT_TIPO_CONTA.toString())))
            .andExpect(jsonPath("$.[*].cpf").value(hasItem(DEFAULT_CPF)))
            .andExpect(jsonPath("$.[*].cnpj").value(hasItem(DEFAULT_CNPJ)))
            .andExpect(jsonPath("$.[*].rua").value(hasItem(DEFAULT_RUA)))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].bairro").value(hasItem(DEFAULT_BAIRRO)))
            .andExpect(jsonPath("$.[*].cidade").value(hasItem(DEFAULT_CIDADE)))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)))
            .andExpect(jsonPath("$.[*].cep").value(hasItem(DEFAULT_CEP)))
            .andExpect(jsonPath("$.[*].pais").value(hasItem(DEFAULT_PAIS)))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].razaosocial").value(hasItem(DEFAULT_RAZAOSOCIAL)))
            .andExpect(jsonPath("$.[*].telefoneComercial").value(hasItem(DEFAULT_TELEFONE_COMERCIAL)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPerfilsWithEagerRelationshipsIsEnabled() throws Exception {
        when(perfilServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPerfilMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(perfilServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPerfilsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(perfilServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPerfilMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(perfilRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPerfil() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        // Get the perfil
        restPerfilMockMvc
            .perform(get(ENTITY_API_URL_ID, perfil.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(perfil.getId().intValue()))
            .andExpect(jsonPath("$.tipoConta").value(DEFAULT_TIPO_CONTA.toString()))
            .andExpect(jsonPath("$.cpf").value(DEFAULT_CPF))
            .andExpect(jsonPath("$.cnpj").value(DEFAULT_CNPJ))
            .andExpect(jsonPath("$.rua").value(DEFAULT_RUA))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO))
            .andExpect(jsonPath("$.bairro").value(DEFAULT_BAIRRO))
            .andExpect(jsonPath("$.cidade").value(DEFAULT_CIDADE))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO))
            .andExpect(jsonPath("$.cep").value(DEFAULT_CEP))
            .andExpect(jsonPath("$.pais").value(DEFAULT_PAIS))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.razaosocial").value(DEFAULT_RAZAOSOCIAL))
            .andExpect(jsonPath("$.telefoneComercial").value(DEFAULT_TELEFONE_COMERCIAL));
    }

    @Test
    @Transactional
    void getPerfilsByIdFiltering() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        Long id = perfil.getId();

        defaultPerfilShouldBeFound("id.equals=" + id);
        defaultPerfilShouldNotBeFound("id.notEquals=" + id);

        defaultPerfilShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPerfilShouldNotBeFound("id.greaterThan=" + id);

        defaultPerfilShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPerfilShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPerfilsByTipoContaIsEqualToSomething() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        // Get all the perfilList where tipoConta equals to DEFAULT_TIPO_CONTA
        defaultPerfilShouldBeFound("tipoConta.equals=" + DEFAULT_TIPO_CONTA);

        // Get all the perfilList where tipoConta equals to UPDATED_TIPO_CONTA
        defaultPerfilShouldNotBeFound("tipoConta.equals=" + UPDATED_TIPO_CONTA);
    }

    @Test
    @Transactional
    void getAllPerfilsByTipoContaIsInShouldWork() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        // Get all the perfilList where tipoConta in DEFAULT_TIPO_CONTA or UPDATED_TIPO_CONTA
        defaultPerfilShouldBeFound("tipoConta.in=" + DEFAULT_TIPO_CONTA + "," + UPDATED_TIPO_CONTA);

        // Get all the perfilList where tipoConta equals to UPDATED_TIPO_CONTA
        defaultPerfilShouldNotBeFound("tipoConta.in=" + UPDATED_TIPO_CONTA);
    }

    @Test
    @Transactional
    void getAllPerfilsByTipoContaIsNullOrNotNull() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        // Get all the perfilList where tipoConta is not null
        defaultPerfilShouldBeFound("tipoConta.specified=true");

        // Get all the perfilList where tipoConta is null
        defaultPerfilShouldNotBeFound("tipoConta.specified=false");
    }

    @Test
    @Transactional
    void getAllPerfilsByCpfIsEqualToSomething() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        // Get all the perfilList where cpf equals to DEFAULT_CPF
        defaultPerfilShouldBeFound("cpf.equals=" + DEFAULT_CPF);

        // Get all the perfilList where cpf equals to UPDATED_CPF
        defaultPerfilShouldNotBeFound("cpf.equals=" + UPDATED_CPF);
    }

    @Test
    @Transactional
    void getAllPerfilsByCpfIsInShouldWork() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        // Get all the perfilList where cpf in DEFAULT_CPF or UPDATED_CPF
        defaultPerfilShouldBeFound("cpf.in=" + DEFAULT_CPF + "," + UPDATED_CPF);

        // Get all the perfilList where cpf equals to UPDATED_CPF
        defaultPerfilShouldNotBeFound("cpf.in=" + UPDATED_CPF);
    }

    @Test
    @Transactional
    void getAllPerfilsByCpfIsNullOrNotNull() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        // Get all the perfilList where cpf is not null
        defaultPerfilShouldBeFound("cpf.specified=true");

        // Get all the perfilList where cpf is null
        defaultPerfilShouldNotBeFound("cpf.specified=false");
    }

    @Test
    @Transactional
    void getAllPerfilsByCpfContainsSomething() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        // Get all the perfilList where cpf contains DEFAULT_CPF
        defaultPerfilShouldBeFound("cpf.contains=" + DEFAULT_CPF);

        // Get all the perfilList where cpf contains UPDATED_CPF
        defaultPerfilShouldNotBeFound("cpf.contains=" + UPDATED_CPF);
    }

    @Test
    @Transactional
    void getAllPerfilsByCpfNotContainsSomething() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        // Get all the perfilList where cpf does not contain DEFAULT_CPF
        defaultPerfilShouldNotBeFound("cpf.doesNotContain=" + DEFAULT_CPF);

        // Get all the perfilList where cpf does not contain UPDATED_CPF
        defaultPerfilShouldBeFound("cpf.doesNotContain=" + UPDATED_CPF);
    }

    @Test
    @Transactional
    void getAllPerfilsByCnpjIsEqualToSomething() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        // Get all the perfilList where cnpj equals to DEFAULT_CNPJ
        defaultPerfilShouldBeFound("cnpj.equals=" + DEFAULT_CNPJ);

        // Get all the perfilList where cnpj equals to UPDATED_CNPJ
        defaultPerfilShouldNotBeFound("cnpj.equals=" + UPDATED_CNPJ);
    }

    @Test
    @Transactional
    void getAllPerfilsByCnpjIsInShouldWork() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        // Get all the perfilList where cnpj in DEFAULT_CNPJ or UPDATED_CNPJ
        defaultPerfilShouldBeFound("cnpj.in=" + DEFAULT_CNPJ + "," + UPDATED_CNPJ);

        // Get all the perfilList where cnpj equals to UPDATED_CNPJ
        defaultPerfilShouldNotBeFound("cnpj.in=" + UPDATED_CNPJ);
    }

    @Test
    @Transactional
    void getAllPerfilsByCnpjIsNullOrNotNull() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        // Get all the perfilList where cnpj is not null
        defaultPerfilShouldBeFound("cnpj.specified=true");

        // Get all the perfilList where cnpj is null
        defaultPerfilShouldNotBeFound("cnpj.specified=false");
    }

    @Test
    @Transactional
    void getAllPerfilsByCnpjContainsSomething() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        // Get all the perfilList where cnpj contains DEFAULT_CNPJ
        defaultPerfilShouldBeFound("cnpj.contains=" + DEFAULT_CNPJ);

        // Get all the perfilList where cnpj contains UPDATED_CNPJ
        defaultPerfilShouldNotBeFound("cnpj.contains=" + UPDATED_CNPJ);
    }

    @Test
    @Transactional
    void getAllPerfilsByCnpjNotContainsSomething() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        // Get all the perfilList where cnpj does not contain DEFAULT_CNPJ
        defaultPerfilShouldNotBeFound("cnpj.doesNotContain=" + DEFAULT_CNPJ);

        // Get all the perfilList where cnpj does not contain UPDATED_CNPJ
        defaultPerfilShouldBeFound("cnpj.doesNotContain=" + UPDATED_CNPJ);
    }

    @Test
    @Transactional
    void getAllPerfilsByRuaIsEqualToSomething() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        // Get all the perfilList where rua equals to DEFAULT_RUA
        defaultPerfilShouldBeFound("rua.equals=" + DEFAULT_RUA);

        // Get all the perfilList where rua equals to UPDATED_RUA
        defaultPerfilShouldNotBeFound("rua.equals=" + UPDATED_RUA);
    }

    @Test
    @Transactional
    void getAllPerfilsByRuaIsInShouldWork() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        // Get all the perfilList where rua in DEFAULT_RUA or UPDATED_RUA
        defaultPerfilShouldBeFound("rua.in=" + DEFAULT_RUA + "," + UPDATED_RUA);

        // Get all the perfilList where rua equals to UPDATED_RUA
        defaultPerfilShouldNotBeFound("rua.in=" + UPDATED_RUA);
    }

    @Test
    @Transactional
    void getAllPerfilsByRuaIsNullOrNotNull() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        // Get all the perfilList where rua is not null
        defaultPerfilShouldBeFound("rua.specified=true");

        // Get all the perfilList where rua is null
        defaultPerfilShouldNotBeFound("rua.specified=false");
    }

    @Test
    @Transactional
    void getAllPerfilsByRuaContainsSomething() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        // Get all the perfilList where rua contains DEFAULT_RUA
        defaultPerfilShouldBeFound("rua.contains=" + DEFAULT_RUA);

        // Get all the perfilList where rua contains UPDATED_RUA
        defaultPerfilShouldNotBeFound("rua.contains=" + UPDATED_RUA);
    }

    @Test
    @Transactional
    void getAllPerfilsByRuaNotContainsSomething() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        // Get all the perfilList where rua does not contain DEFAULT_RUA
        defaultPerfilShouldNotBeFound("rua.doesNotContain=" + DEFAULT_RUA);

        // Get all the perfilList where rua does not contain UPDATED_RUA
        defaultPerfilShouldBeFound("rua.doesNotContain=" + UPDATED_RUA);
    }

    @Test
    @Transactional
    void getAllPerfilsByNumeroIsEqualToSomething() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        // Get all the perfilList where numero equals to DEFAULT_NUMERO
        defaultPerfilShouldBeFound("numero.equals=" + DEFAULT_NUMERO);

        // Get all the perfilList where numero equals to UPDATED_NUMERO
        defaultPerfilShouldNotBeFound("numero.equals=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void getAllPerfilsByNumeroIsInShouldWork() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        // Get all the perfilList where numero in DEFAULT_NUMERO or UPDATED_NUMERO
        defaultPerfilShouldBeFound("numero.in=" + DEFAULT_NUMERO + "," + UPDATED_NUMERO);

        // Get all the perfilList where numero equals to UPDATED_NUMERO
        defaultPerfilShouldNotBeFound("numero.in=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void getAllPerfilsByNumeroIsNullOrNotNull() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        // Get all the perfilList where numero is not null
        defaultPerfilShouldBeFound("numero.specified=true");

        // Get all the perfilList where numero is null
        defaultPerfilShouldNotBeFound("numero.specified=false");
    }

    @Test
    @Transactional
    void getAllPerfilsByNumeroContainsSomething() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        // Get all the perfilList where numero contains DEFAULT_NUMERO
        defaultPerfilShouldBeFound("numero.contains=" + DEFAULT_NUMERO);

        // Get all the perfilList where numero contains UPDATED_NUMERO
        defaultPerfilShouldNotBeFound("numero.contains=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void getAllPerfilsByNumeroNotContainsSomething() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        // Get all the perfilList where numero does not contain DEFAULT_NUMERO
        defaultPerfilShouldNotBeFound("numero.doesNotContain=" + DEFAULT_NUMERO);

        // Get all the perfilList where numero does not contain UPDATED_NUMERO
        defaultPerfilShouldBeFound("numero.doesNotContain=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void getAllPerfilsByBairroIsEqualToSomething() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        // Get all the perfilList where bairro equals to DEFAULT_BAIRRO
        defaultPerfilShouldBeFound("bairro.equals=" + DEFAULT_BAIRRO);

        // Get all the perfilList where bairro equals to UPDATED_BAIRRO
        defaultPerfilShouldNotBeFound("bairro.equals=" + UPDATED_BAIRRO);
    }

    @Test
    @Transactional
    void getAllPerfilsByBairroIsInShouldWork() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        // Get all the perfilList where bairro in DEFAULT_BAIRRO or UPDATED_BAIRRO
        defaultPerfilShouldBeFound("bairro.in=" + DEFAULT_BAIRRO + "," + UPDATED_BAIRRO);

        // Get all the perfilList where bairro equals to UPDATED_BAIRRO
        defaultPerfilShouldNotBeFound("bairro.in=" + UPDATED_BAIRRO);
    }

    @Test
    @Transactional
    void getAllPerfilsByBairroIsNullOrNotNull() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        // Get all the perfilList where bairro is not null
        defaultPerfilShouldBeFound("bairro.specified=true");

        // Get all the perfilList where bairro is null
        defaultPerfilShouldNotBeFound("bairro.specified=false");
    }

    @Test
    @Transactional
    void getAllPerfilsByBairroContainsSomething() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        // Get all the perfilList where bairro contains DEFAULT_BAIRRO
        defaultPerfilShouldBeFound("bairro.contains=" + DEFAULT_BAIRRO);

        // Get all the perfilList where bairro contains UPDATED_BAIRRO
        defaultPerfilShouldNotBeFound("bairro.contains=" + UPDATED_BAIRRO);
    }

    @Test
    @Transactional
    void getAllPerfilsByBairroNotContainsSomething() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        // Get all the perfilList where bairro does not contain DEFAULT_BAIRRO
        defaultPerfilShouldNotBeFound("bairro.doesNotContain=" + DEFAULT_BAIRRO);

        // Get all the perfilList where bairro does not contain UPDATED_BAIRRO
        defaultPerfilShouldBeFound("bairro.doesNotContain=" + UPDATED_BAIRRO);
    }

    @Test
    @Transactional
    void getAllPerfilsByCidadeIsEqualToSomething() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        // Get all the perfilList where cidade equals to DEFAULT_CIDADE
        defaultPerfilShouldBeFound("cidade.equals=" + DEFAULT_CIDADE);

        // Get all the perfilList where cidade equals to UPDATED_CIDADE
        defaultPerfilShouldNotBeFound("cidade.equals=" + UPDATED_CIDADE);
    }

    @Test
    @Transactional
    void getAllPerfilsByCidadeIsInShouldWork() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        // Get all the perfilList where cidade in DEFAULT_CIDADE or UPDATED_CIDADE
        defaultPerfilShouldBeFound("cidade.in=" + DEFAULT_CIDADE + "," + UPDATED_CIDADE);

        // Get all the perfilList where cidade equals to UPDATED_CIDADE
        defaultPerfilShouldNotBeFound("cidade.in=" + UPDATED_CIDADE);
    }

    @Test
    @Transactional
    void getAllPerfilsByCidadeIsNullOrNotNull() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        // Get all the perfilList where cidade is not null
        defaultPerfilShouldBeFound("cidade.specified=true");

        // Get all the perfilList where cidade is null
        defaultPerfilShouldNotBeFound("cidade.specified=false");
    }

    @Test
    @Transactional
    void getAllPerfilsByCidadeContainsSomething() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        // Get all the perfilList where cidade contains DEFAULT_CIDADE
        defaultPerfilShouldBeFound("cidade.contains=" + DEFAULT_CIDADE);

        // Get all the perfilList where cidade contains UPDATED_CIDADE
        defaultPerfilShouldNotBeFound("cidade.contains=" + UPDATED_CIDADE);
    }

    @Test
    @Transactional
    void getAllPerfilsByCidadeNotContainsSomething() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        // Get all the perfilList where cidade does not contain DEFAULT_CIDADE
        defaultPerfilShouldNotBeFound("cidade.doesNotContain=" + DEFAULT_CIDADE);

        // Get all the perfilList where cidade does not contain UPDATED_CIDADE
        defaultPerfilShouldBeFound("cidade.doesNotContain=" + UPDATED_CIDADE);
    }

    @Test
    @Transactional
    void getAllPerfilsByEstadoIsEqualToSomething() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        // Get all the perfilList where estado equals to DEFAULT_ESTADO
        defaultPerfilShouldBeFound("estado.equals=" + DEFAULT_ESTADO);

        // Get all the perfilList where estado equals to UPDATED_ESTADO
        defaultPerfilShouldNotBeFound("estado.equals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllPerfilsByEstadoIsInShouldWork() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        // Get all the perfilList where estado in DEFAULT_ESTADO or UPDATED_ESTADO
        defaultPerfilShouldBeFound("estado.in=" + DEFAULT_ESTADO + "," + UPDATED_ESTADO);

        // Get all the perfilList where estado equals to UPDATED_ESTADO
        defaultPerfilShouldNotBeFound("estado.in=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllPerfilsByEstadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        // Get all the perfilList where estado is not null
        defaultPerfilShouldBeFound("estado.specified=true");

        // Get all the perfilList where estado is null
        defaultPerfilShouldNotBeFound("estado.specified=false");
    }

    @Test
    @Transactional
    void getAllPerfilsByEstadoContainsSomething() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        // Get all the perfilList where estado contains DEFAULT_ESTADO
        defaultPerfilShouldBeFound("estado.contains=" + DEFAULT_ESTADO);

        // Get all the perfilList where estado contains UPDATED_ESTADO
        defaultPerfilShouldNotBeFound("estado.contains=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllPerfilsByEstadoNotContainsSomething() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        // Get all the perfilList where estado does not contain DEFAULT_ESTADO
        defaultPerfilShouldNotBeFound("estado.doesNotContain=" + DEFAULT_ESTADO);

        // Get all the perfilList where estado does not contain UPDATED_ESTADO
        defaultPerfilShouldBeFound("estado.doesNotContain=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllPerfilsByCepIsEqualToSomething() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        // Get all the perfilList where cep equals to DEFAULT_CEP
        defaultPerfilShouldBeFound("cep.equals=" + DEFAULT_CEP);

        // Get all the perfilList where cep equals to UPDATED_CEP
        defaultPerfilShouldNotBeFound("cep.equals=" + UPDATED_CEP);
    }

    @Test
    @Transactional
    void getAllPerfilsByCepIsInShouldWork() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        // Get all the perfilList where cep in DEFAULT_CEP or UPDATED_CEP
        defaultPerfilShouldBeFound("cep.in=" + DEFAULT_CEP + "," + UPDATED_CEP);

        // Get all the perfilList where cep equals to UPDATED_CEP
        defaultPerfilShouldNotBeFound("cep.in=" + UPDATED_CEP);
    }

    @Test
    @Transactional
    void getAllPerfilsByCepIsNullOrNotNull() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        // Get all the perfilList where cep is not null
        defaultPerfilShouldBeFound("cep.specified=true");

        // Get all the perfilList where cep is null
        defaultPerfilShouldNotBeFound("cep.specified=false");
    }

    @Test
    @Transactional
    void getAllPerfilsByCepContainsSomething() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        // Get all the perfilList where cep contains DEFAULT_CEP
        defaultPerfilShouldBeFound("cep.contains=" + DEFAULT_CEP);

        // Get all the perfilList where cep contains UPDATED_CEP
        defaultPerfilShouldNotBeFound("cep.contains=" + UPDATED_CEP);
    }

    @Test
    @Transactional
    void getAllPerfilsByCepNotContainsSomething() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        // Get all the perfilList where cep does not contain DEFAULT_CEP
        defaultPerfilShouldNotBeFound("cep.doesNotContain=" + DEFAULT_CEP);

        // Get all the perfilList where cep does not contain UPDATED_CEP
        defaultPerfilShouldBeFound("cep.doesNotContain=" + UPDATED_CEP);
    }

    @Test
    @Transactional
    void getAllPerfilsByPaisIsEqualToSomething() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        // Get all the perfilList where pais equals to DEFAULT_PAIS
        defaultPerfilShouldBeFound("pais.equals=" + DEFAULT_PAIS);

        // Get all the perfilList where pais equals to UPDATED_PAIS
        defaultPerfilShouldNotBeFound("pais.equals=" + UPDATED_PAIS);
    }

    @Test
    @Transactional
    void getAllPerfilsByPaisIsInShouldWork() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        // Get all the perfilList where pais in DEFAULT_PAIS or UPDATED_PAIS
        defaultPerfilShouldBeFound("pais.in=" + DEFAULT_PAIS + "," + UPDATED_PAIS);

        // Get all the perfilList where pais equals to UPDATED_PAIS
        defaultPerfilShouldNotBeFound("pais.in=" + UPDATED_PAIS);
    }

    @Test
    @Transactional
    void getAllPerfilsByPaisIsNullOrNotNull() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        // Get all the perfilList where pais is not null
        defaultPerfilShouldBeFound("pais.specified=true");

        // Get all the perfilList where pais is null
        defaultPerfilShouldNotBeFound("pais.specified=false");
    }

    @Test
    @Transactional
    void getAllPerfilsByPaisContainsSomething() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        // Get all the perfilList where pais contains DEFAULT_PAIS
        defaultPerfilShouldBeFound("pais.contains=" + DEFAULT_PAIS);

        // Get all the perfilList where pais contains UPDATED_PAIS
        defaultPerfilShouldNotBeFound("pais.contains=" + UPDATED_PAIS);
    }

    @Test
    @Transactional
    void getAllPerfilsByPaisNotContainsSomething() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        // Get all the perfilList where pais does not contain DEFAULT_PAIS
        defaultPerfilShouldNotBeFound("pais.doesNotContain=" + DEFAULT_PAIS);

        // Get all the perfilList where pais does not contain UPDATED_PAIS
        defaultPerfilShouldBeFound("pais.doesNotContain=" + UPDATED_PAIS);
    }

    @Test
    @Transactional
    void getAllPerfilsByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        // Get all the perfilList where nome equals to DEFAULT_NOME
        defaultPerfilShouldBeFound("nome.equals=" + DEFAULT_NOME);

        // Get all the perfilList where nome equals to UPDATED_NOME
        defaultPerfilShouldNotBeFound("nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllPerfilsByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        // Get all the perfilList where nome in DEFAULT_NOME or UPDATED_NOME
        defaultPerfilShouldBeFound("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME);

        // Get all the perfilList where nome equals to UPDATED_NOME
        defaultPerfilShouldNotBeFound("nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllPerfilsByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        // Get all the perfilList where nome is not null
        defaultPerfilShouldBeFound("nome.specified=true");

        // Get all the perfilList where nome is null
        defaultPerfilShouldNotBeFound("nome.specified=false");
    }

    @Test
    @Transactional
    void getAllPerfilsByNomeContainsSomething() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        // Get all the perfilList where nome contains DEFAULT_NOME
        defaultPerfilShouldBeFound("nome.contains=" + DEFAULT_NOME);

        // Get all the perfilList where nome contains UPDATED_NOME
        defaultPerfilShouldNotBeFound("nome.contains=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllPerfilsByNomeNotContainsSomething() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        // Get all the perfilList where nome does not contain DEFAULT_NOME
        defaultPerfilShouldNotBeFound("nome.doesNotContain=" + DEFAULT_NOME);

        // Get all the perfilList where nome does not contain UPDATED_NOME
        defaultPerfilShouldBeFound("nome.doesNotContain=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllPerfilsByRazaosocialIsEqualToSomething() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        // Get all the perfilList where razaosocial equals to DEFAULT_RAZAOSOCIAL
        defaultPerfilShouldBeFound("razaosocial.equals=" + DEFAULT_RAZAOSOCIAL);

        // Get all the perfilList where razaosocial equals to UPDATED_RAZAOSOCIAL
        defaultPerfilShouldNotBeFound("razaosocial.equals=" + UPDATED_RAZAOSOCIAL);
    }

    @Test
    @Transactional
    void getAllPerfilsByRazaosocialIsInShouldWork() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        // Get all the perfilList where razaosocial in DEFAULT_RAZAOSOCIAL or UPDATED_RAZAOSOCIAL
        defaultPerfilShouldBeFound("razaosocial.in=" + DEFAULT_RAZAOSOCIAL + "," + UPDATED_RAZAOSOCIAL);

        // Get all the perfilList where razaosocial equals to UPDATED_RAZAOSOCIAL
        defaultPerfilShouldNotBeFound("razaosocial.in=" + UPDATED_RAZAOSOCIAL);
    }

    @Test
    @Transactional
    void getAllPerfilsByRazaosocialIsNullOrNotNull() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        // Get all the perfilList where razaosocial is not null
        defaultPerfilShouldBeFound("razaosocial.specified=true");

        // Get all the perfilList where razaosocial is null
        defaultPerfilShouldNotBeFound("razaosocial.specified=false");
    }

    @Test
    @Transactional
    void getAllPerfilsByRazaosocialContainsSomething() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        // Get all the perfilList where razaosocial contains DEFAULT_RAZAOSOCIAL
        defaultPerfilShouldBeFound("razaosocial.contains=" + DEFAULT_RAZAOSOCIAL);

        // Get all the perfilList where razaosocial contains UPDATED_RAZAOSOCIAL
        defaultPerfilShouldNotBeFound("razaosocial.contains=" + UPDATED_RAZAOSOCIAL);
    }

    @Test
    @Transactional
    void getAllPerfilsByRazaosocialNotContainsSomething() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        // Get all the perfilList where razaosocial does not contain DEFAULT_RAZAOSOCIAL
        defaultPerfilShouldNotBeFound("razaosocial.doesNotContain=" + DEFAULT_RAZAOSOCIAL);

        // Get all the perfilList where razaosocial does not contain UPDATED_RAZAOSOCIAL
        defaultPerfilShouldBeFound("razaosocial.doesNotContain=" + UPDATED_RAZAOSOCIAL);
    }

    @Test
    @Transactional
    void getAllPerfilsByTelefoneComercialIsEqualToSomething() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        // Get all the perfilList where telefoneComercial equals to DEFAULT_TELEFONE_COMERCIAL
        defaultPerfilShouldBeFound("telefoneComercial.equals=" + DEFAULT_TELEFONE_COMERCIAL);

        // Get all the perfilList where telefoneComercial equals to UPDATED_TELEFONE_COMERCIAL
        defaultPerfilShouldNotBeFound("telefoneComercial.equals=" + UPDATED_TELEFONE_COMERCIAL);
    }

    @Test
    @Transactional
    void getAllPerfilsByTelefoneComercialIsInShouldWork() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        // Get all the perfilList where telefoneComercial in DEFAULT_TELEFONE_COMERCIAL or UPDATED_TELEFONE_COMERCIAL
        defaultPerfilShouldBeFound("telefoneComercial.in=" + DEFAULT_TELEFONE_COMERCIAL + "," + UPDATED_TELEFONE_COMERCIAL);

        // Get all the perfilList where telefoneComercial equals to UPDATED_TELEFONE_COMERCIAL
        defaultPerfilShouldNotBeFound("telefoneComercial.in=" + UPDATED_TELEFONE_COMERCIAL);
    }

    @Test
    @Transactional
    void getAllPerfilsByTelefoneComercialIsNullOrNotNull() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        // Get all the perfilList where telefoneComercial is not null
        defaultPerfilShouldBeFound("telefoneComercial.specified=true");

        // Get all the perfilList where telefoneComercial is null
        defaultPerfilShouldNotBeFound("telefoneComercial.specified=false");
    }

    @Test
    @Transactional
    void getAllPerfilsByTelefoneComercialContainsSomething() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        // Get all the perfilList where telefoneComercial contains DEFAULT_TELEFONE_COMERCIAL
        defaultPerfilShouldBeFound("telefoneComercial.contains=" + DEFAULT_TELEFONE_COMERCIAL);

        // Get all the perfilList where telefoneComercial contains UPDATED_TELEFONE_COMERCIAL
        defaultPerfilShouldNotBeFound("telefoneComercial.contains=" + UPDATED_TELEFONE_COMERCIAL);
    }

    @Test
    @Transactional
    void getAllPerfilsByTelefoneComercialNotContainsSomething() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        // Get all the perfilList where telefoneComercial does not contain DEFAULT_TELEFONE_COMERCIAL
        defaultPerfilShouldNotBeFound("telefoneComercial.doesNotContain=" + DEFAULT_TELEFONE_COMERCIAL);

        // Get all the perfilList where telefoneComercial does not contain UPDATED_TELEFONE_COMERCIAL
        defaultPerfilShouldBeFound("telefoneComercial.doesNotContain=" + UPDATED_TELEFONE_COMERCIAL);
    }

    @Test
    @Transactional
    void getAllPerfilsByUserIsEqualToSomething() throws Exception {
        // Get already existing entity
        User user = perfil.getUser();
        perfilRepository.saveAndFlush(perfil);
        Long userId = user.getId();

        // Get all the perfilList where user equals to userId
        defaultPerfilShouldBeFound("userId.equals=" + userId);

        // Get all the perfilList where user equals to (userId + 1)
        defaultPerfilShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPerfilShouldBeFound(String filter) throws Exception {
        restPerfilMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(perfil.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipoConta").value(hasItem(DEFAULT_TIPO_CONTA.toString())))
            .andExpect(jsonPath("$.[*].cpf").value(hasItem(DEFAULT_CPF)))
            .andExpect(jsonPath("$.[*].cnpj").value(hasItem(DEFAULT_CNPJ)))
            .andExpect(jsonPath("$.[*].rua").value(hasItem(DEFAULT_RUA)))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].bairro").value(hasItem(DEFAULT_BAIRRO)))
            .andExpect(jsonPath("$.[*].cidade").value(hasItem(DEFAULT_CIDADE)))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)))
            .andExpect(jsonPath("$.[*].cep").value(hasItem(DEFAULT_CEP)))
            .andExpect(jsonPath("$.[*].pais").value(hasItem(DEFAULT_PAIS)))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].razaosocial").value(hasItem(DEFAULT_RAZAOSOCIAL)))
            .andExpect(jsonPath("$.[*].telefoneComercial").value(hasItem(DEFAULT_TELEFONE_COMERCIAL)));

        // Check, that the count call also returns 1
        restPerfilMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPerfilShouldNotBeFound(String filter) throws Exception {
        restPerfilMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPerfilMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPerfil() throws Exception {
        // Get the perfil
        restPerfilMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPerfil() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        int databaseSizeBeforeUpdate = perfilRepository.findAll().size();

        // Update the perfil
        Perfil updatedPerfil = perfilRepository.findById(perfil.getId()).get();
        // Disconnect from session so that the updates on updatedPerfil are not directly saved in db
        em.detach(updatedPerfil);
        updatedPerfil
            .tipoConta(UPDATED_TIPO_CONTA)
            .cpf(UPDATED_CPF)
            .cnpj(UPDATED_CNPJ)
            .rua(UPDATED_RUA)
            .numero(UPDATED_NUMERO)
            .bairro(UPDATED_BAIRRO)
            .cidade(UPDATED_CIDADE)
            .estado(UPDATED_ESTADO)
            .cep(UPDATED_CEP)
            .pais(UPDATED_PAIS)
            .nome(UPDATED_NOME)
            .razaosocial(UPDATED_RAZAOSOCIAL)
            .telefoneComercial(UPDATED_TELEFONE_COMERCIAL);
        PerfilDTO perfilDTO = perfilMapper.toDto(updatedPerfil);

        restPerfilMockMvc
            .perform(
                put(ENTITY_API_URL_ID, perfilDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(perfilDTO))
            )
            .andExpect(status().isOk());

        // Validate the Perfil in the database
        List<Perfil> perfilList = perfilRepository.findAll();
        assertThat(perfilList).hasSize(databaseSizeBeforeUpdate);
        Perfil testPerfil = perfilList.get(perfilList.size() - 1);
        assertThat(testPerfil.getTipoConta()).isEqualTo(UPDATED_TIPO_CONTA);
        assertThat(testPerfil.getCpf()).isEqualTo(UPDATED_CPF);
        assertThat(testPerfil.getCnpj()).isEqualTo(UPDATED_CNPJ);
        assertThat(testPerfil.getRua()).isEqualTo(UPDATED_RUA);
        assertThat(testPerfil.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testPerfil.getBairro()).isEqualTo(UPDATED_BAIRRO);
        assertThat(testPerfil.getCidade()).isEqualTo(UPDATED_CIDADE);
        assertThat(testPerfil.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testPerfil.getCep()).isEqualTo(UPDATED_CEP);
        assertThat(testPerfil.getPais()).isEqualTo(UPDATED_PAIS);
        assertThat(testPerfil.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testPerfil.getRazaosocial()).isEqualTo(UPDATED_RAZAOSOCIAL);
        assertThat(testPerfil.getTelefoneComercial()).isEqualTo(UPDATED_TELEFONE_COMERCIAL);
    }

    @Test
    @Transactional
    void putNonExistingPerfil() throws Exception {
        int databaseSizeBeforeUpdate = perfilRepository.findAll().size();
        perfil.setId(count.incrementAndGet());

        // Create the Perfil
        PerfilDTO perfilDTO = perfilMapper.toDto(perfil);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPerfilMockMvc
            .perform(
                put(ENTITY_API_URL_ID, perfilDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(perfilDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Perfil in the database
        List<Perfil> perfilList = perfilRepository.findAll();
        assertThat(perfilList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPerfil() throws Exception {
        int databaseSizeBeforeUpdate = perfilRepository.findAll().size();
        perfil.setId(count.incrementAndGet());

        // Create the Perfil
        PerfilDTO perfilDTO = perfilMapper.toDto(perfil);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPerfilMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(perfilDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Perfil in the database
        List<Perfil> perfilList = perfilRepository.findAll();
        assertThat(perfilList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPerfil() throws Exception {
        int databaseSizeBeforeUpdate = perfilRepository.findAll().size();
        perfil.setId(count.incrementAndGet());

        // Create the Perfil
        PerfilDTO perfilDTO = perfilMapper.toDto(perfil);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPerfilMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(perfilDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Perfil in the database
        List<Perfil> perfilList = perfilRepository.findAll();
        assertThat(perfilList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePerfilWithPatch() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        int databaseSizeBeforeUpdate = perfilRepository.findAll().size();

        // Update the perfil using partial update
        Perfil partialUpdatedPerfil = new Perfil();
        partialUpdatedPerfil.setId(perfil.getId());

        partialUpdatedPerfil
            .rua(UPDATED_RUA)
            .numero(UPDATED_NUMERO)
            .bairro(UPDATED_BAIRRO)
            .cidade(UPDATED_CIDADE)
            .pais(UPDATED_PAIS)
            .razaosocial(UPDATED_RAZAOSOCIAL)
            .telefoneComercial(UPDATED_TELEFONE_COMERCIAL);

        restPerfilMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPerfil.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPerfil))
            )
            .andExpect(status().isOk());

        // Validate the Perfil in the database
        List<Perfil> perfilList = perfilRepository.findAll();
        assertThat(perfilList).hasSize(databaseSizeBeforeUpdate);
        Perfil testPerfil = perfilList.get(perfilList.size() - 1);
        assertThat(testPerfil.getTipoConta()).isEqualTo(DEFAULT_TIPO_CONTA);
        assertThat(testPerfil.getCpf()).isEqualTo(DEFAULT_CPF);
        assertThat(testPerfil.getCnpj()).isEqualTo(DEFAULT_CNPJ);
        assertThat(testPerfil.getRua()).isEqualTo(UPDATED_RUA);
        assertThat(testPerfil.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testPerfil.getBairro()).isEqualTo(UPDATED_BAIRRO);
        assertThat(testPerfil.getCidade()).isEqualTo(UPDATED_CIDADE);
        assertThat(testPerfil.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testPerfil.getCep()).isEqualTo(DEFAULT_CEP);
        assertThat(testPerfil.getPais()).isEqualTo(UPDATED_PAIS);
        assertThat(testPerfil.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testPerfil.getRazaosocial()).isEqualTo(UPDATED_RAZAOSOCIAL);
        assertThat(testPerfil.getTelefoneComercial()).isEqualTo(UPDATED_TELEFONE_COMERCIAL);
    }

    @Test
    @Transactional
    void fullUpdatePerfilWithPatch() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        int databaseSizeBeforeUpdate = perfilRepository.findAll().size();

        // Update the perfil using partial update
        Perfil partialUpdatedPerfil = new Perfil();
        partialUpdatedPerfil.setId(perfil.getId());

        partialUpdatedPerfil
            .tipoConta(UPDATED_TIPO_CONTA)
            .cpf(UPDATED_CPF)
            .cnpj(UPDATED_CNPJ)
            .rua(UPDATED_RUA)
            .numero(UPDATED_NUMERO)
            .bairro(UPDATED_BAIRRO)
            .cidade(UPDATED_CIDADE)
            .estado(UPDATED_ESTADO)
            .cep(UPDATED_CEP)
            .pais(UPDATED_PAIS)
            .nome(UPDATED_NOME)
            .razaosocial(UPDATED_RAZAOSOCIAL)
            .telefoneComercial(UPDATED_TELEFONE_COMERCIAL);

        restPerfilMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPerfil.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPerfil))
            )
            .andExpect(status().isOk());

        // Validate the Perfil in the database
        List<Perfil> perfilList = perfilRepository.findAll();
        assertThat(perfilList).hasSize(databaseSizeBeforeUpdate);
        Perfil testPerfil = perfilList.get(perfilList.size() - 1);
        assertThat(testPerfil.getTipoConta()).isEqualTo(UPDATED_TIPO_CONTA);
        assertThat(testPerfil.getCpf()).isEqualTo(UPDATED_CPF);
        assertThat(testPerfil.getCnpj()).isEqualTo(UPDATED_CNPJ);
        assertThat(testPerfil.getRua()).isEqualTo(UPDATED_RUA);
        assertThat(testPerfil.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testPerfil.getBairro()).isEqualTo(UPDATED_BAIRRO);
        assertThat(testPerfil.getCidade()).isEqualTo(UPDATED_CIDADE);
        assertThat(testPerfil.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testPerfil.getCep()).isEqualTo(UPDATED_CEP);
        assertThat(testPerfil.getPais()).isEqualTo(UPDATED_PAIS);
        assertThat(testPerfil.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testPerfil.getRazaosocial()).isEqualTo(UPDATED_RAZAOSOCIAL);
        assertThat(testPerfil.getTelefoneComercial()).isEqualTo(UPDATED_TELEFONE_COMERCIAL);
    }

    @Test
    @Transactional
    void patchNonExistingPerfil() throws Exception {
        int databaseSizeBeforeUpdate = perfilRepository.findAll().size();
        perfil.setId(count.incrementAndGet());

        // Create the Perfil
        PerfilDTO perfilDTO = perfilMapper.toDto(perfil);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPerfilMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, perfilDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(perfilDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Perfil in the database
        List<Perfil> perfilList = perfilRepository.findAll();
        assertThat(perfilList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPerfil() throws Exception {
        int databaseSizeBeforeUpdate = perfilRepository.findAll().size();
        perfil.setId(count.incrementAndGet());

        // Create the Perfil
        PerfilDTO perfilDTO = perfilMapper.toDto(perfil);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPerfilMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(perfilDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Perfil in the database
        List<Perfil> perfilList = perfilRepository.findAll();
        assertThat(perfilList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPerfil() throws Exception {
        int databaseSizeBeforeUpdate = perfilRepository.findAll().size();
        perfil.setId(count.incrementAndGet());

        // Create the Perfil
        PerfilDTO perfilDTO = perfilMapper.toDto(perfil);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPerfilMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(perfilDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Perfil in the database
        List<Perfil> perfilList = perfilRepository.findAll();
        assertThat(perfilList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePerfil() throws Exception {
        // Initialize the database
        perfilRepository.saveAndFlush(perfil);

        int databaseSizeBeforeDelete = perfilRepository.findAll().size();

        // Delete the perfil
        restPerfilMockMvc
            .perform(delete(ENTITY_API_URL_ID, perfil.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Perfil> perfilList = perfilRepository.findAll();
        assertThat(perfilList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

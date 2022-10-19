package com.infocargas.freteapp.web.rest;

import static com.infocargas.freteapp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.infocargas.freteapp.IntegrationTest;
import com.infocargas.freteapp.domain.Ofertas;
import com.infocargas.freteapp.domain.Perfil;
import com.infocargas.freteapp.domain.enumeration.StatusOferta;
import com.infocargas.freteapp.domain.enumeration.TipoCarga;
import com.infocargas.freteapp.domain.enumeration.TipoOferta;
import com.infocargas.freteapp.domain.enumeration.TipoTransporteOferta;
import com.infocargas.freteapp.repository.OfertasRepository;
import com.infocargas.freteapp.service.criteria.OfertasCriteria;
import com.infocargas.freteapp.service.dto.OfertasDTO;
import com.infocargas.freteapp.service.mapper.OfertasMapper;
import java.time.Instant;
import java.time.LocalDate;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link OfertasResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OfertasResourceIT {

    private static final ZonedDateTime DEFAULT_DATA_POSTAGEM = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_POSTAGEM = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATA_POSTAGEM = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Integer DEFAULT_QUANTIDADE = 1;
    private static final Integer UPDATED_QUANTIDADE = 2;
    private static final Integer SMALLER_QUANTIDADE = 1 - 1;

    private static final TipoCarga DEFAULT_TIPO_CARGA = TipoCarga.MOTO;
    private static final TipoCarga UPDATED_TIPO_CARGA = TipoCarga.CARRO;

    private static final String DEFAULT_LOCALIZACAO_ORIGEM = "AAAAAAAAAA";
    private static final String UPDATED_LOCALIZACAO_ORIGEM = "BBBBBBBBBB";

    private static final String DEFAULT_LOCALIZACAO_DESTINO = "AAAAAAAAAA";
    private static final String UPDATED_LOCALIZACAO_DESTINO = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATA_COLETA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_COLETA = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATA_COLETA = LocalDate.ofEpochDay(-1L);

    private static final ZonedDateTime DEFAULT_DATA_ENTREGA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_ENTREGA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATA_ENTREGA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_DATA_MODIFICACAO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_MODIFICACAO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATA_MODIFICACAO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_DATA_FECHAMENTO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA_FECHAMENTO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATA_FECHAMENTO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final StatusOferta DEFAULT_STATUS = StatusOferta.AGUARDANDO_PROPOSTA;
    private static final StatusOferta UPDATED_STATUS = StatusOferta.ATENDIDA_INFOCARGAS;

    private static final TipoOferta DEFAULT_TIPO_OFERTA = TipoOferta.CARGA;
    private static final TipoOferta UPDATED_TIPO_OFERTA = TipoOferta.VAGAS;

    private static final TipoTransporteOferta DEFAULT_TIPO_TRANSPORTE = TipoTransporteOferta.CEGONHA;
    private static final TipoTransporteOferta UPDATED_TIPO_TRANSPORTE = TipoTransporteOferta.RAMPA;

    private static final String DEFAULT_DESTINO = "AAAAAAAAAA";
    private static final String UPDATED_DESTINO = "BBBBBBBBBB";

    private static final String DEFAULT_ORIGEM = "AAAAAAAAAA";
    private static final String UPDATED_ORIGEM = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ofertas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OfertasRepository ofertasRepository;

    @Autowired
    private OfertasMapper ofertasMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOfertasMockMvc;

    private Ofertas ofertas;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ofertas createEntity(EntityManager em) {
        Ofertas ofertas = new Ofertas()
            .dataPostagem(DEFAULT_DATA_POSTAGEM)
            .quantidade(DEFAULT_QUANTIDADE)
            .tipoCarga(DEFAULT_TIPO_CARGA)
            .localizacaoOrigem(DEFAULT_LOCALIZACAO_ORIGEM)
            .localizacaoDestino(DEFAULT_LOCALIZACAO_DESTINO)
            .dataColeta(DEFAULT_DATA_COLETA)
            .dataEntrega(DEFAULT_DATA_ENTREGA)
            .dataModificacao(DEFAULT_DATA_MODIFICACAO)
            .dataFechamento(DEFAULT_DATA_FECHAMENTO)
            .status(DEFAULT_STATUS)
            .tipoOferta(DEFAULT_TIPO_OFERTA)
            .tipoTransporte(DEFAULT_TIPO_TRANSPORTE)
            .destino(DEFAULT_DESTINO)
            .origem(DEFAULT_ORIGEM);
        // Add required entity
        Perfil perfil;
        if (TestUtil.findAll(em, Perfil.class).isEmpty()) {
            perfil = PerfilResourceIT.createEntity(em);
            em.persist(perfil);
            em.flush();
        } else {
            perfil = TestUtil.findAll(em, Perfil.class).get(0);
        }
        ofertas.setPerfil(perfil);
        return ofertas;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ofertas createUpdatedEntity(EntityManager em) {
        Ofertas ofertas = new Ofertas()
            .dataPostagem(UPDATED_DATA_POSTAGEM)
            .quantidade(UPDATED_QUANTIDADE)
            .tipoCarga(UPDATED_TIPO_CARGA)
            .localizacaoOrigem(UPDATED_LOCALIZACAO_ORIGEM)
            .localizacaoDestino(UPDATED_LOCALIZACAO_DESTINO)
            .dataColeta(UPDATED_DATA_COLETA)
            .dataEntrega(UPDATED_DATA_ENTREGA)
            .dataModificacao(UPDATED_DATA_MODIFICACAO)
            .dataFechamento(UPDATED_DATA_FECHAMENTO)
            .status(UPDATED_STATUS)
            .tipoOferta(UPDATED_TIPO_OFERTA)
            .tipoTransporte(UPDATED_TIPO_TRANSPORTE)
            .destino(UPDATED_DESTINO)
            .origem(UPDATED_ORIGEM);
        // Add required entity
        Perfil perfil;
        if (TestUtil.findAll(em, Perfil.class).isEmpty()) {
            perfil = PerfilResourceIT.createUpdatedEntity(em);
            em.persist(perfil);
            em.flush();
        } else {
            perfil = TestUtil.findAll(em, Perfil.class).get(0);
        }
        ofertas.setPerfil(perfil);
        return ofertas;
    }

    @BeforeEach
    public void initTest() {
        ofertas = createEntity(em);
    }

    @Test
    @Transactional
    void createOfertas() throws Exception {
        int databaseSizeBeforeCreate = ofertasRepository.findAll().size();
        // Create the Ofertas
        OfertasDTO ofertasDTO = ofertasMapper.toDto(ofertas);
        restOfertasMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ofertasDTO)))
            .andExpect(status().isCreated());

        // Validate the Ofertas in the database
        List<Ofertas> ofertasList = ofertasRepository.findAll();
        assertThat(ofertasList).hasSize(databaseSizeBeforeCreate + 1);
        Ofertas testOfertas = ofertasList.get(ofertasList.size() - 1);
        assertThat(testOfertas.getDataPostagem()).isEqualTo(DEFAULT_DATA_POSTAGEM);
        assertThat(testOfertas.getQuantidade()).isEqualTo(DEFAULT_QUANTIDADE);
        assertThat(testOfertas.getTipoCarga()).isEqualTo(DEFAULT_TIPO_CARGA);
        assertThat(testOfertas.getLocalizacaoOrigem()).isEqualTo(DEFAULT_LOCALIZACAO_ORIGEM);
        assertThat(testOfertas.getLocalizacaoDestino()).isEqualTo(DEFAULT_LOCALIZACAO_DESTINO);
        assertThat(testOfertas.getDataColeta()).isEqualTo(DEFAULT_DATA_COLETA);
        assertThat(testOfertas.getDataEntrega()).isEqualTo(DEFAULT_DATA_ENTREGA);
        assertThat(testOfertas.getDataModificacao()).isEqualTo(DEFAULT_DATA_MODIFICACAO);
        assertThat(testOfertas.getDataFechamento()).isEqualTo(DEFAULT_DATA_FECHAMENTO);
        assertThat(testOfertas.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testOfertas.getTipoOferta()).isEqualTo(DEFAULT_TIPO_OFERTA);
        assertThat(testOfertas.getTipoTransporte()).isEqualTo(DEFAULT_TIPO_TRANSPORTE);
        assertThat(testOfertas.getDestino()).isEqualTo(DEFAULT_DESTINO);
        assertThat(testOfertas.getOrigem()).isEqualTo(DEFAULT_ORIGEM);
    }

    @Test
    @Transactional
    void createOfertasWithExistingId() throws Exception {
        // Create the Ofertas with an existing ID
        ofertas.setId(1L);
        OfertasDTO ofertasDTO = ofertasMapper.toDto(ofertas);

        int databaseSizeBeforeCreate = ofertasRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOfertasMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ofertasDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Ofertas in the database
        List<Ofertas> ofertasList = ofertasRepository.findAll();
        assertThat(ofertasList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDataPostagemIsRequired() throws Exception {
        int databaseSizeBeforeTest = ofertasRepository.findAll().size();
        // set the field null
        ofertas.setDataPostagem(null);

        // Create the Ofertas, which fails.
        OfertasDTO ofertasDTO = ofertasMapper.toDto(ofertas);

        restOfertasMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ofertasDTO)))
            .andExpect(status().isBadRequest());

        List<Ofertas> ofertasList = ofertasRepository.findAll();
        assertThat(ofertasList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkQuantidadeIsRequired() throws Exception {
        int databaseSizeBeforeTest = ofertasRepository.findAll().size();
        // set the field null
        ofertas.setQuantidade(null);

        // Create the Ofertas, which fails.
        OfertasDTO ofertasDTO = ofertasMapper.toDto(ofertas);

        restOfertasMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ofertasDTO)))
            .andExpect(status().isBadRequest());

        List<Ofertas> ofertasList = ofertasRepository.findAll();
        assertThat(ofertasList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTipoCargaIsRequired() throws Exception {
        int databaseSizeBeforeTest = ofertasRepository.findAll().size();
        // set the field null
        ofertas.setTipoCarga(null);

        // Create the Ofertas, which fails.
        OfertasDTO ofertasDTO = ofertasMapper.toDto(ofertas);

        restOfertasMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ofertasDTO)))
            .andExpect(status().isBadRequest());

        List<Ofertas> ofertasList = ofertasRepository.findAll();
        assertThat(ofertasList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = ofertasRepository.findAll().size();
        // set the field null
        ofertas.setStatus(null);

        // Create the Ofertas, which fails.
        OfertasDTO ofertasDTO = ofertasMapper.toDto(ofertas);

        restOfertasMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ofertasDTO)))
            .andExpect(status().isBadRequest());

        List<Ofertas> ofertasList = ofertasRepository.findAll();
        assertThat(ofertasList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTipoOfertaIsRequired() throws Exception {
        int databaseSizeBeforeTest = ofertasRepository.findAll().size();
        // set the field null
        ofertas.setTipoOferta(null);

        // Create the Ofertas, which fails.
        OfertasDTO ofertasDTO = ofertasMapper.toDto(ofertas);

        restOfertasMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ofertasDTO)))
            .andExpect(status().isBadRequest());

        List<Ofertas> ofertasList = ofertasRepository.findAll();
        assertThat(ofertasList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDestinoIsRequired() throws Exception {
        int databaseSizeBeforeTest = ofertasRepository.findAll().size();
        // set the field null
        ofertas.setDestino(null);

        // Create the Ofertas, which fails.
        OfertasDTO ofertasDTO = ofertasMapper.toDto(ofertas);

        restOfertasMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ofertasDTO)))
            .andExpect(status().isBadRequest());

        List<Ofertas> ofertasList = ofertasRepository.findAll();
        assertThat(ofertasList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOrigemIsRequired() throws Exception {
        int databaseSizeBeforeTest = ofertasRepository.findAll().size();
        // set the field null
        ofertas.setOrigem(null);

        // Create the Ofertas, which fails.
        OfertasDTO ofertasDTO = ofertasMapper.toDto(ofertas);

        restOfertasMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ofertasDTO)))
            .andExpect(status().isBadRequest());

        List<Ofertas> ofertasList = ofertasRepository.findAll();
        assertThat(ofertasList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOfertas() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        // Get all the ofertasList
        restOfertasMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ofertas.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataPostagem").value(hasItem(sameInstant(DEFAULT_DATA_POSTAGEM))))
            .andExpect(jsonPath("$.[*].quantidade").value(hasItem(DEFAULT_QUANTIDADE)))
            .andExpect(jsonPath("$.[*].tipoCarga").value(hasItem(DEFAULT_TIPO_CARGA.toString())))
            .andExpect(jsonPath("$.[*].localizacaoOrigem").value(hasItem(DEFAULT_LOCALIZACAO_ORIGEM.toString())))
            .andExpect(jsonPath("$.[*].localizacaoDestino").value(hasItem(DEFAULT_LOCALIZACAO_DESTINO.toString())))
            .andExpect(jsonPath("$.[*].dataColeta").value(hasItem(DEFAULT_DATA_COLETA.toString())))
            .andExpect(jsonPath("$.[*].dataEntrega").value(hasItem(sameInstant(DEFAULT_DATA_ENTREGA))))
            .andExpect(jsonPath("$.[*].dataModificacao").value(hasItem(sameInstant(DEFAULT_DATA_MODIFICACAO))))
            .andExpect(jsonPath("$.[*].dataFechamento").value(hasItem(sameInstant(DEFAULT_DATA_FECHAMENTO))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].tipoOferta").value(hasItem(DEFAULT_TIPO_OFERTA.toString())))
            .andExpect(jsonPath("$.[*].tipoTransporte").value(hasItem(DEFAULT_TIPO_TRANSPORTE.toString())))
            .andExpect(jsonPath("$.[*].destino").value(hasItem(DEFAULT_DESTINO)))
            .andExpect(jsonPath("$.[*].origem").value(hasItem(DEFAULT_ORIGEM)));
    }

    @Test
    @Transactional
    void getOfertas() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        // Get the ofertas
        restOfertasMockMvc
            .perform(get(ENTITY_API_URL_ID, ofertas.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ofertas.getId().intValue()))
            .andExpect(jsonPath("$.dataPostagem").value(sameInstant(DEFAULT_DATA_POSTAGEM)))
            .andExpect(jsonPath("$.quantidade").value(DEFAULT_QUANTIDADE))
            .andExpect(jsonPath("$.tipoCarga").value(DEFAULT_TIPO_CARGA.toString()))
            .andExpect(jsonPath("$.localizacaoOrigem").value(DEFAULT_LOCALIZACAO_ORIGEM.toString()))
            .andExpect(jsonPath("$.localizacaoDestino").value(DEFAULT_LOCALIZACAO_DESTINO.toString()))
            .andExpect(jsonPath("$.dataColeta").value(DEFAULT_DATA_COLETA.toString()))
            .andExpect(jsonPath("$.dataEntrega").value(sameInstant(DEFAULT_DATA_ENTREGA)))
            .andExpect(jsonPath("$.dataModificacao").value(sameInstant(DEFAULT_DATA_MODIFICACAO)))
            .andExpect(jsonPath("$.dataFechamento").value(sameInstant(DEFAULT_DATA_FECHAMENTO)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.tipoOferta").value(DEFAULT_TIPO_OFERTA.toString()))
            .andExpect(jsonPath("$.tipoTransporte").value(DEFAULT_TIPO_TRANSPORTE.toString()))
            .andExpect(jsonPath("$.destino").value(DEFAULT_DESTINO))
            .andExpect(jsonPath("$.origem").value(DEFAULT_ORIGEM));
    }

    @Test
    @Transactional
    void getOfertasByIdFiltering() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        Long id = ofertas.getId();

        defaultOfertasShouldBeFound("id.equals=" + id);
        defaultOfertasShouldNotBeFound("id.notEquals=" + id);

        defaultOfertasShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOfertasShouldNotBeFound("id.greaterThan=" + id);

        defaultOfertasShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOfertasShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOfertasByDataPostagemIsEqualToSomething() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        // Get all the ofertasList where dataPostagem equals to DEFAULT_DATA_POSTAGEM
        defaultOfertasShouldBeFound("dataPostagem.equals=" + DEFAULT_DATA_POSTAGEM);

        // Get all the ofertasList where dataPostagem equals to UPDATED_DATA_POSTAGEM
        defaultOfertasShouldNotBeFound("dataPostagem.equals=" + UPDATED_DATA_POSTAGEM);
    }

    @Test
    @Transactional
    void getAllOfertasByDataPostagemIsInShouldWork() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        // Get all the ofertasList where dataPostagem in DEFAULT_DATA_POSTAGEM or UPDATED_DATA_POSTAGEM
        defaultOfertasShouldBeFound("dataPostagem.in=" + DEFAULT_DATA_POSTAGEM + "," + UPDATED_DATA_POSTAGEM);

        // Get all the ofertasList where dataPostagem equals to UPDATED_DATA_POSTAGEM
        defaultOfertasShouldNotBeFound("dataPostagem.in=" + UPDATED_DATA_POSTAGEM);
    }

    @Test
    @Transactional
    void getAllOfertasByDataPostagemIsNullOrNotNull() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        // Get all the ofertasList where dataPostagem is not null
        defaultOfertasShouldBeFound("dataPostagem.specified=true");

        // Get all the ofertasList where dataPostagem is null
        defaultOfertasShouldNotBeFound("dataPostagem.specified=false");
    }

    @Test
    @Transactional
    void getAllOfertasByDataPostagemIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        // Get all the ofertasList where dataPostagem is greater than or equal to DEFAULT_DATA_POSTAGEM
        defaultOfertasShouldBeFound("dataPostagem.greaterThanOrEqual=" + DEFAULT_DATA_POSTAGEM);

        // Get all the ofertasList where dataPostagem is greater than or equal to UPDATED_DATA_POSTAGEM
        defaultOfertasShouldNotBeFound("dataPostagem.greaterThanOrEqual=" + UPDATED_DATA_POSTAGEM);
    }

    @Test
    @Transactional
    void getAllOfertasByDataPostagemIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        // Get all the ofertasList where dataPostagem is less than or equal to DEFAULT_DATA_POSTAGEM
        defaultOfertasShouldBeFound("dataPostagem.lessThanOrEqual=" + DEFAULT_DATA_POSTAGEM);

        // Get all the ofertasList where dataPostagem is less than or equal to SMALLER_DATA_POSTAGEM
        defaultOfertasShouldNotBeFound("dataPostagem.lessThanOrEqual=" + SMALLER_DATA_POSTAGEM);
    }

    @Test
    @Transactional
    void getAllOfertasByDataPostagemIsLessThanSomething() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        // Get all the ofertasList where dataPostagem is less than DEFAULT_DATA_POSTAGEM
        defaultOfertasShouldNotBeFound("dataPostagem.lessThan=" + DEFAULT_DATA_POSTAGEM);

        // Get all the ofertasList where dataPostagem is less than UPDATED_DATA_POSTAGEM
        defaultOfertasShouldBeFound("dataPostagem.lessThan=" + UPDATED_DATA_POSTAGEM);
    }

    @Test
    @Transactional
    void getAllOfertasByDataPostagemIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        // Get all the ofertasList where dataPostagem is greater than DEFAULT_DATA_POSTAGEM
        defaultOfertasShouldNotBeFound("dataPostagem.greaterThan=" + DEFAULT_DATA_POSTAGEM);

        // Get all the ofertasList where dataPostagem is greater than SMALLER_DATA_POSTAGEM
        defaultOfertasShouldBeFound("dataPostagem.greaterThan=" + SMALLER_DATA_POSTAGEM);
    }

    @Test
    @Transactional
    void getAllOfertasByQuantidadeIsEqualToSomething() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        // Get all the ofertasList where quantidade equals to DEFAULT_QUANTIDADE
        defaultOfertasShouldBeFound("quantidade.equals=" + DEFAULT_QUANTIDADE);

        // Get all the ofertasList where quantidade equals to UPDATED_QUANTIDADE
        defaultOfertasShouldNotBeFound("quantidade.equals=" + UPDATED_QUANTIDADE);
    }

    @Test
    @Transactional
    void getAllOfertasByQuantidadeIsInShouldWork() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        // Get all the ofertasList where quantidade in DEFAULT_QUANTIDADE or UPDATED_QUANTIDADE
        defaultOfertasShouldBeFound("quantidade.in=" + DEFAULT_QUANTIDADE + "," + UPDATED_QUANTIDADE);

        // Get all the ofertasList where quantidade equals to UPDATED_QUANTIDADE
        defaultOfertasShouldNotBeFound("quantidade.in=" + UPDATED_QUANTIDADE);
    }

    @Test
    @Transactional
    void getAllOfertasByQuantidadeIsNullOrNotNull() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        // Get all the ofertasList where quantidade is not null
        defaultOfertasShouldBeFound("quantidade.specified=true");

        // Get all the ofertasList where quantidade is null
        defaultOfertasShouldNotBeFound("quantidade.specified=false");
    }

    @Test
    @Transactional
    void getAllOfertasByQuantidadeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        // Get all the ofertasList where quantidade is greater than or equal to DEFAULT_QUANTIDADE
        defaultOfertasShouldBeFound("quantidade.greaterThanOrEqual=" + DEFAULT_QUANTIDADE);

        // Get all the ofertasList where quantidade is greater than or equal to UPDATED_QUANTIDADE
        defaultOfertasShouldNotBeFound("quantidade.greaterThanOrEqual=" + UPDATED_QUANTIDADE);
    }

    @Test
    @Transactional
    void getAllOfertasByQuantidadeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        // Get all the ofertasList where quantidade is less than or equal to DEFAULT_QUANTIDADE
        defaultOfertasShouldBeFound("quantidade.lessThanOrEqual=" + DEFAULT_QUANTIDADE);

        // Get all the ofertasList where quantidade is less than or equal to SMALLER_QUANTIDADE
        defaultOfertasShouldNotBeFound("quantidade.lessThanOrEqual=" + SMALLER_QUANTIDADE);
    }

    @Test
    @Transactional
    void getAllOfertasByQuantidadeIsLessThanSomething() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        // Get all the ofertasList where quantidade is less than DEFAULT_QUANTIDADE
        defaultOfertasShouldNotBeFound("quantidade.lessThan=" + DEFAULT_QUANTIDADE);

        // Get all the ofertasList where quantidade is less than UPDATED_QUANTIDADE
        defaultOfertasShouldBeFound("quantidade.lessThan=" + UPDATED_QUANTIDADE);
    }

    @Test
    @Transactional
    void getAllOfertasByQuantidadeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        // Get all the ofertasList where quantidade is greater than DEFAULT_QUANTIDADE
        defaultOfertasShouldNotBeFound("quantidade.greaterThan=" + DEFAULT_QUANTIDADE);

        // Get all the ofertasList where quantidade is greater than SMALLER_QUANTIDADE
        defaultOfertasShouldBeFound("quantidade.greaterThan=" + SMALLER_QUANTIDADE);
    }

    @Test
    @Transactional
    void getAllOfertasByTipoCargaIsEqualToSomething() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        // Get all the ofertasList where tipoCarga equals to DEFAULT_TIPO_CARGA
        defaultOfertasShouldBeFound("tipoCarga.equals=" + DEFAULT_TIPO_CARGA);

        // Get all the ofertasList where tipoCarga equals to UPDATED_TIPO_CARGA
        defaultOfertasShouldNotBeFound("tipoCarga.equals=" + UPDATED_TIPO_CARGA);
    }

    @Test
    @Transactional
    void getAllOfertasByTipoCargaIsInShouldWork() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        // Get all the ofertasList where tipoCarga in DEFAULT_TIPO_CARGA or UPDATED_TIPO_CARGA
        defaultOfertasShouldBeFound("tipoCarga.in=" + DEFAULT_TIPO_CARGA + "," + UPDATED_TIPO_CARGA);

        // Get all the ofertasList where tipoCarga equals to UPDATED_TIPO_CARGA
        defaultOfertasShouldNotBeFound("tipoCarga.in=" + UPDATED_TIPO_CARGA);
    }

    @Test
    @Transactional
    void getAllOfertasByTipoCargaIsNullOrNotNull() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        // Get all the ofertasList where tipoCarga is not null
        defaultOfertasShouldBeFound("tipoCarga.specified=true");

        // Get all the ofertasList where tipoCarga is null
        defaultOfertasShouldNotBeFound("tipoCarga.specified=false");
    }

    @Test
    @Transactional
    void getAllOfertasByDataColetaIsEqualToSomething() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        // Get all the ofertasList where dataColeta equals to DEFAULT_DATA_COLETA
        defaultOfertasShouldBeFound("dataColeta.equals=" + DEFAULT_DATA_COLETA);

        // Get all the ofertasList where dataColeta equals to UPDATED_DATA_COLETA
        defaultOfertasShouldNotBeFound("dataColeta.equals=" + UPDATED_DATA_COLETA);
    }

    @Test
    @Transactional
    void getAllOfertasByDataColetaIsInShouldWork() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        // Get all the ofertasList where dataColeta in DEFAULT_DATA_COLETA or UPDATED_DATA_COLETA
        defaultOfertasShouldBeFound("dataColeta.in=" + DEFAULT_DATA_COLETA + "," + UPDATED_DATA_COLETA);

        // Get all the ofertasList where dataColeta equals to UPDATED_DATA_COLETA
        defaultOfertasShouldNotBeFound("dataColeta.in=" + UPDATED_DATA_COLETA);
    }

    @Test
    @Transactional
    void getAllOfertasByDataColetaIsNullOrNotNull() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        // Get all the ofertasList where dataColeta is not null
        defaultOfertasShouldBeFound("dataColeta.specified=true");

        // Get all the ofertasList where dataColeta is null
        defaultOfertasShouldNotBeFound("dataColeta.specified=false");
    }

    @Test
    @Transactional
    void getAllOfertasByDataColetaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        // Get all the ofertasList where dataColeta is greater than or equal to DEFAULT_DATA_COLETA
        defaultOfertasShouldBeFound("dataColeta.greaterThanOrEqual=" + DEFAULT_DATA_COLETA);

        // Get all the ofertasList where dataColeta is greater than or equal to UPDATED_DATA_COLETA
        defaultOfertasShouldNotBeFound("dataColeta.greaterThanOrEqual=" + UPDATED_DATA_COLETA);
    }

    @Test
    @Transactional
    void getAllOfertasByDataColetaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        // Get all the ofertasList where dataColeta is less than or equal to DEFAULT_DATA_COLETA
        defaultOfertasShouldBeFound("dataColeta.lessThanOrEqual=" + DEFAULT_DATA_COLETA);

        // Get all the ofertasList where dataColeta is less than or equal to SMALLER_DATA_COLETA
        defaultOfertasShouldNotBeFound("dataColeta.lessThanOrEqual=" + SMALLER_DATA_COLETA);
    }

    @Test
    @Transactional
    void getAllOfertasByDataColetaIsLessThanSomething() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        // Get all the ofertasList where dataColeta is less than DEFAULT_DATA_COLETA
        defaultOfertasShouldNotBeFound("dataColeta.lessThan=" + DEFAULT_DATA_COLETA);

        // Get all the ofertasList where dataColeta is less than UPDATED_DATA_COLETA
        defaultOfertasShouldBeFound("dataColeta.lessThan=" + UPDATED_DATA_COLETA);
    }

    @Test
    @Transactional
    void getAllOfertasByDataColetaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        // Get all the ofertasList where dataColeta is greater than DEFAULT_DATA_COLETA
        defaultOfertasShouldNotBeFound("dataColeta.greaterThan=" + DEFAULT_DATA_COLETA);

        // Get all the ofertasList where dataColeta is greater than SMALLER_DATA_COLETA
        defaultOfertasShouldBeFound("dataColeta.greaterThan=" + SMALLER_DATA_COLETA);
    }

    @Test
    @Transactional
    void getAllOfertasByDataEntregaIsEqualToSomething() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        // Get all the ofertasList where dataEntrega equals to DEFAULT_DATA_ENTREGA
        defaultOfertasShouldBeFound("dataEntrega.equals=" + DEFAULT_DATA_ENTREGA);

        // Get all the ofertasList where dataEntrega equals to UPDATED_DATA_ENTREGA
        defaultOfertasShouldNotBeFound("dataEntrega.equals=" + UPDATED_DATA_ENTREGA);
    }

    @Test
    @Transactional
    void getAllOfertasByDataEntregaIsInShouldWork() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        // Get all the ofertasList where dataEntrega in DEFAULT_DATA_ENTREGA or UPDATED_DATA_ENTREGA
        defaultOfertasShouldBeFound("dataEntrega.in=" + DEFAULT_DATA_ENTREGA + "," + UPDATED_DATA_ENTREGA);

        // Get all the ofertasList where dataEntrega equals to UPDATED_DATA_ENTREGA
        defaultOfertasShouldNotBeFound("dataEntrega.in=" + UPDATED_DATA_ENTREGA);
    }

    @Test
    @Transactional
    void getAllOfertasByDataEntregaIsNullOrNotNull() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        // Get all the ofertasList where dataEntrega is not null
        defaultOfertasShouldBeFound("dataEntrega.specified=true");

        // Get all the ofertasList where dataEntrega is null
        defaultOfertasShouldNotBeFound("dataEntrega.specified=false");
    }

    @Test
    @Transactional
    void getAllOfertasByDataEntregaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        // Get all the ofertasList where dataEntrega is greater than or equal to DEFAULT_DATA_ENTREGA
        defaultOfertasShouldBeFound("dataEntrega.greaterThanOrEqual=" + DEFAULT_DATA_ENTREGA);

        // Get all the ofertasList where dataEntrega is greater than or equal to UPDATED_DATA_ENTREGA
        defaultOfertasShouldNotBeFound("dataEntrega.greaterThanOrEqual=" + UPDATED_DATA_ENTREGA);
    }

    @Test
    @Transactional
    void getAllOfertasByDataEntregaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        // Get all the ofertasList where dataEntrega is less than or equal to DEFAULT_DATA_ENTREGA
        defaultOfertasShouldBeFound("dataEntrega.lessThanOrEqual=" + DEFAULT_DATA_ENTREGA);

        // Get all the ofertasList where dataEntrega is less than or equal to SMALLER_DATA_ENTREGA
        defaultOfertasShouldNotBeFound("dataEntrega.lessThanOrEqual=" + SMALLER_DATA_ENTREGA);
    }

    @Test
    @Transactional
    void getAllOfertasByDataEntregaIsLessThanSomething() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        // Get all the ofertasList where dataEntrega is less than DEFAULT_DATA_ENTREGA
        defaultOfertasShouldNotBeFound("dataEntrega.lessThan=" + DEFAULT_DATA_ENTREGA);

        // Get all the ofertasList where dataEntrega is less than UPDATED_DATA_ENTREGA
        defaultOfertasShouldBeFound("dataEntrega.lessThan=" + UPDATED_DATA_ENTREGA);
    }

    @Test
    @Transactional
    void getAllOfertasByDataEntregaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        // Get all the ofertasList where dataEntrega is greater than DEFAULT_DATA_ENTREGA
        defaultOfertasShouldNotBeFound("dataEntrega.greaterThan=" + DEFAULT_DATA_ENTREGA);

        // Get all the ofertasList where dataEntrega is greater than SMALLER_DATA_ENTREGA
        defaultOfertasShouldBeFound("dataEntrega.greaterThan=" + SMALLER_DATA_ENTREGA);
    }

    @Test
    @Transactional
    void getAllOfertasByDataModificacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        // Get all the ofertasList where dataModificacao equals to DEFAULT_DATA_MODIFICACAO
        defaultOfertasShouldBeFound("dataModificacao.equals=" + DEFAULT_DATA_MODIFICACAO);

        // Get all the ofertasList where dataModificacao equals to UPDATED_DATA_MODIFICACAO
        defaultOfertasShouldNotBeFound("dataModificacao.equals=" + UPDATED_DATA_MODIFICACAO);
    }

    @Test
    @Transactional
    void getAllOfertasByDataModificacaoIsInShouldWork() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        // Get all the ofertasList where dataModificacao in DEFAULT_DATA_MODIFICACAO or UPDATED_DATA_MODIFICACAO
        defaultOfertasShouldBeFound("dataModificacao.in=" + DEFAULT_DATA_MODIFICACAO + "," + UPDATED_DATA_MODIFICACAO);

        // Get all the ofertasList where dataModificacao equals to UPDATED_DATA_MODIFICACAO
        defaultOfertasShouldNotBeFound("dataModificacao.in=" + UPDATED_DATA_MODIFICACAO);
    }

    @Test
    @Transactional
    void getAllOfertasByDataModificacaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        // Get all the ofertasList where dataModificacao is not null
        defaultOfertasShouldBeFound("dataModificacao.specified=true");

        // Get all the ofertasList where dataModificacao is null
        defaultOfertasShouldNotBeFound("dataModificacao.specified=false");
    }

    @Test
    @Transactional
    void getAllOfertasByDataModificacaoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        // Get all the ofertasList where dataModificacao is greater than or equal to DEFAULT_DATA_MODIFICACAO
        defaultOfertasShouldBeFound("dataModificacao.greaterThanOrEqual=" + DEFAULT_DATA_MODIFICACAO);

        // Get all the ofertasList where dataModificacao is greater than or equal to UPDATED_DATA_MODIFICACAO
        defaultOfertasShouldNotBeFound("dataModificacao.greaterThanOrEqual=" + UPDATED_DATA_MODIFICACAO);
    }

    @Test
    @Transactional
    void getAllOfertasByDataModificacaoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        // Get all the ofertasList where dataModificacao is less than or equal to DEFAULT_DATA_MODIFICACAO
        defaultOfertasShouldBeFound("dataModificacao.lessThanOrEqual=" + DEFAULT_DATA_MODIFICACAO);

        // Get all the ofertasList where dataModificacao is less than or equal to SMALLER_DATA_MODIFICACAO
        defaultOfertasShouldNotBeFound("dataModificacao.lessThanOrEqual=" + SMALLER_DATA_MODIFICACAO);
    }

    @Test
    @Transactional
    void getAllOfertasByDataModificacaoIsLessThanSomething() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        // Get all the ofertasList where dataModificacao is less than DEFAULT_DATA_MODIFICACAO
        defaultOfertasShouldNotBeFound("dataModificacao.lessThan=" + DEFAULT_DATA_MODIFICACAO);

        // Get all the ofertasList where dataModificacao is less than UPDATED_DATA_MODIFICACAO
        defaultOfertasShouldBeFound("dataModificacao.lessThan=" + UPDATED_DATA_MODIFICACAO);
    }

    @Test
    @Transactional
    void getAllOfertasByDataModificacaoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        // Get all the ofertasList where dataModificacao is greater than DEFAULT_DATA_MODIFICACAO
        defaultOfertasShouldNotBeFound("dataModificacao.greaterThan=" + DEFAULT_DATA_MODIFICACAO);

        // Get all the ofertasList where dataModificacao is greater than SMALLER_DATA_MODIFICACAO
        defaultOfertasShouldBeFound("dataModificacao.greaterThan=" + SMALLER_DATA_MODIFICACAO);
    }

    @Test
    @Transactional
    void getAllOfertasByDataFechamentoIsEqualToSomething() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        // Get all the ofertasList where dataFechamento equals to DEFAULT_DATA_FECHAMENTO
        defaultOfertasShouldBeFound("dataFechamento.equals=" + DEFAULT_DATA_FECHAMENTO);

        // Get all the ofertasList where dataFechamento equals to UPDATED_DATA_FECHAMENTO
        defaultOfertasShouldNotBeFound("dataFechamento.equals=" + UPDATED_DATA_FECHAMENTO);
    }

    @Test
    @Transactional
    void getAllOfertasByDataFechamentoIsInShouldWork() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        // Get all the ofertasList where dataFechamento in DEFAULT_DATA_FECHAMENTO or UPDATED_DATA_FECHAMENTO
        defaultOfertasShouldBeFound("dataFechamento.in=" + DEFAULT_DATA_FECHAMENTO + "," + UPDATED_DATA_FECHAMENTO);

        // Get all the ofertasList where dataFechamento equals to UPDATED_DATA_FECHAMENTO
        defaultOfertasShouldNotBeFound("dataFechamento.in=" + UPDATED_DATA_FECHAMENTO);
    }

    @Test
    @Transactional
    void getAllOfertasByDataFechamentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        // Get all the ofertasList where dataFechamento is not null
        defaultOfertasShouldBeFound("dataFechamento.specified=true");

        // Get all the ofertasList where dataFechamento is null
        defaultOfertasShouldNotBeFound("dataFechamento.specified=false");
    }

    @Test
    @Transactional
    void getAllOfertasByDataFechamentoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        // Get all the ofertasList where dataFechamento is greater than or equal to DEFAULT_DATA_FECHAMENTO
        defaultOfertasShouldBeFound("dataFechamento.greaterThanOrEqual=" + DEFAULT_DATA_FECHAMENTO);

        // Get all the ofertasList where dataFechamento is greater than or equal to UPDATED_DATA_FECHAMENTO
        defaultOfertasShouldNotBeFound("dataFechamento.greaterThanOrEqual=" + UPDATED_DATA_FECHAMENTO);
    }

    @Test
    @Transactional
    void getAllOfertasByDataFechamentoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        // Get all the ofertasList where dataFechamento is less than or equal to DEFAULT_DATA_FECHAMENTO
        defaultOfertasShouldBeFound("dataFechamento.lessThanOrEqual=" + DEFAULT_DATA_FECHAMENTO);

        // Get all the ofertasList where dataFechamento is less than or equal to SMALLER_DATA_FECHAMENTO
        defaultOfertasShouldNotBeFound("dataFechamento.lessThanOrEqual=" + SMALLER_DATA_FECHAMENTO);
    }

    @Test
    @Transactional
    void getAllOfertasByDataFechamentoIsLessThanSomething() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        // Get all the ofertasList where dataFechamento is less than DEFAULT_DATA_FECHAMENTO
        defaultOfertasShouldNotBeFound("dataFechamento.lessThan=" + DEFAULT_DATA_FECHAMENTO);

        // Get all the ofertasList where dataFechamento is less than UPDATED_DATA_FECHAMENTO
        defaultOfertasShouldBeFound("dataFechamento.lessThan=" + UPDATED_DATA_FECHAMENTO);
    }

    @Test
    @Transactional
    void getAllOfertasByDataFechamentoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        // Get all the ofertasList where dataFechamento is greater than DEFAULT_DATA_FECHAMENTO
        defaultOfertasShouldNotBeFound("dataFechamento.greaterThan=" + DEFAULT_DATA_FECHAMENTO);

        // Get all the ofertasList where dataFechamento is greater than SMALLER_DATA_FECHAMENTO
        defaultOfertasShouldBeFound("dataFechamento.greaterThan=" + SMALLER_DATA_FECHAMENTO);
    }

    @Test
    @Transactional
    void getAllOfertasByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        // Get all the ofertasList where status equals to DEFAULT_STATUS
        defaultOfertasShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the ofertasList where status equals to UPDATED_STATUS
        defaultOfertasShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllOfertasByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        // Get all the ofertasList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultOfertasShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the ofertasList where status equals to UPDATED_STATUS
        defaultOfertasShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllOfertasByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        // Get all the ofertasList where status is not null
        defaultOfertasShouldBeFound("status.specified=true");

        // Get all the ofertasList where status is null
        defaultOfertasShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllOfertasByTipoOfertaIsEqualToSomething() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        // Get all the ofertasList where tipoOferta equals to DEFAULT_TIPO_OFERTA
        defaultOfertasShouldBeFound("tipoOferta.equals=" + DEFAULT_TIPO_OFERTA);

        // Get all the ofertasList where tipoOferta equals to UPDATED_TIPO_OFERTA
        defaultOfertasShouldNotBeFound("tipoOferta.equals=" + UPDATED_TIPO_OFERTA);
    }

    @Test
    @Transactional
    void getAllOfertasByTipoOfertaIsInShouldWork() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        // Get all the ofertasList where tipoOferta in DEFAULT_TIPO_OFERTA or UPDATED_TIPO_OFERTA
        defaultOfertasShouldBeFound("tipoOferta.in=" + DEFAULT_TIPO_OFERTA + "," + UPDATED_TIPO_OFERTA);

        // Get all the ofertasList where tipoOferta equals to UPDATED_TIPO_OFERTA
        defaultOfertasShouldNotBeFound("tipoOferta.in=" + UPDATED_TIPO_OFERTA);
    }

    @Test
    @Transactional
    void getAllOfertasByTipoOfertaIsNullOrNotNull() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        // Get all the ofertasList where tipoOferta is not null
        defaultOfertasShouldBeFound("tipoOferta.specified=true");

        // Get all the ofertasList where tipoOferta is null
        defaultOfertasShouldNotBeFound("tipoOferta.specified=false");
    }

    @Test
    @Transactional
    void getAllOfertasByTipoTransporteIsEqualToSomething() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        // Get all the ofertasList where tipoTransporte equals to DEFAULT_TIPO_TRANSPORTE
        defaultOfertasShouldBeFound("tipoTransporte.equals=" + DEFAULT_TIPO_TRANSPORTE);

        // Get all the ofertasList where tipoTransporte equals to UPDATED_TIPO_TRANSPORTE
        defaultOfertasShouldNotBeFound("tipoTransporte.equals=" + UPDATED_TIPO_TRANSPORTE);
    }

    @Test
    @Transactional
    void getAllOfertasByTipoTransporteIsInShouldWork() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        // Get all the ofertasList where tipoTransporte in DEFAULT_TIPO_TRANSPORTE or UPDATED_TIPO_TRANSPORTE
        defaultOfertasShouldBeFound("tipoTransporte.in=" + DEFAULT_TIPO_TRANSPORTE + "," + UPDATED_TIPO_TRANSPORTE);

        // Get all the ofertasList where tipoTransporte equals to UPDATED_TIPO_TRANSPORTE
        defaultOfertasShouldNotBeFound("tipoTransporte.in=" + UPDATED_TIPO_TRANSPORTE);
    }

    @Test
    @Transactional
    void getAllOfertasByTipoTransporteIsNullOrNotNull() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        // Get all the ofertasList where tipoTransporte is not null
        defaultOfertasShouldBeFound("tipoTransporte.specified=true");

        // Get all the ofertasList where tipoTransporte is null
        defaultOfertasShouldNotBeFound("tipoTransporte.specified=false");
    }

    @Test
    @Transactional
    void getAllOfertasByDestinoIsEqualToSomething() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        // Get all the ofertasList where destino equals to DEFAULT_DESTINO
        defaultOfertasShouldBeFound("destino.equals=" + DEFAULT_DESTINO);

        // Get all the ofertasList where destino equals to UPDATED_DESTINO
        defaultOfertasShouldNotBeFound("destino.equals=" + UPDATED_DESTINO);
    }

    @Test
    @Transactional
    void getAllOfertasByDestinoIsInShouldWork() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        // Get all the ofertasList where destino in DEFAULT_DESTINO or UPDATED_DESTINO
        defaultOfertasShouldBeFound("destino.in=" + DEFAULT_DESTINO + "," + UPDATED_DESTINO);

        // Get all the ofertasList where destino equals to UPDATED_DESTINO
        defaultOfertasShouldNotBeFound("destino.in=" + UPDATED_DESTINO);
    }

    @Test
    @Transactional
    void getAllOfertasByDestinoIsNullOrNotNull() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        // Get all the ofertasList where destino is not null
        defaultOfertasShouldBeFound("destino.specified=true");

        // Get all the ofertasList where destino is null
        defaultOfertasShouldNotBeFound("destino.specified=false");
    }

    @Test
    @Transactional
    void getAllOfertasByDestinoContainsSomething() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        // Get all the ofertasList where destino contains DEFAULT_DESTINO
        defaultOfertasShouldBeFound("destino.contains=" + DEFAULT_DESTINO);

        // Get all the ofertasList where destino contains UPDATED_DESTINO
        defaultOfertasShouldNotBeFound("destino.contains=" + UPDATED_DESTINO);
    }

    @Test
    @Transactional
    void getAllOfertasByDestinoNotContainsSomething() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        // Get all the ofertasList where destino does not contain DEFAULT_DESTINO
        defaultOfertasShouldNotBeFound("destino.doesNotContain=" + DEFAULT_DESTINO);

        // Get all the ofertasList where destino does not contain UPDATED_DESTINO
        defaultOfertasShouldBeFound("destino.doesNotContain=" + UPDATED_DESTINO);
    }

    @Test
    @Transactional
    void getAllOfertasByOrigemIsEqualToSomething() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        // Get all the ofertasList where origem equals to DEFAULT_ORIGEM
        defaultOfertasShouldBeFound("origem.equals=" + DEFAULT_ORIGEM);

        // Get all the ofertasList where origem equals to UPDATED_ORIGEM
        defaultOfertasShouldNotBeFound("origem.equals=" + UPDATED_ORIGEM);
    }

    @Test
    @Transactional
    void getAllOfertasByOrigemIsInShouldWork() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        // Get all the ofertasList where origem in DEFAULT_ORIGEM or UPDATED_ORIGEM
        defaultOfertasShouldBeFound("origem.in=" + DEFAULT_ORIGEM + "," + UPDATED_ORIGEM);

        // Get all the ofertasList where origem equals to UPDATED_ORIGEM
        defaultOfertasShouldNotBeFound("origem.in=" + UPDATED_ORIGEM);
    }

    @Test
    @Transactional
    void getAllOfertasByOrigemIsNullOrNotNull() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        // Get all the ofertasList where origem is not null
        defaultOfertasShouldBeFound("origem.specified=true");

        // Get all the ofertasList where origem is null
        defaultOfertasShouldNotBeFound("origem.specified=false");
    }

    @Test
    @Transactional
    void getAllOfertasByOrigemContainsSomething() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        // Get all the ofertasList where origem contains DEFAULT_ORIGEM
        defaultOfertasShouldBeFound("origem.contains=" + DEFAULT_ORIGEM);

        // Get all the ofertasList where origem contains UPDATED_ORIGEM
        defaultOfertasShouldNotBeFound("origem.contains=" + UPDATED_ORIGEM);
    }

    @Test
    @Transactional
    void getAllOfertasByOrigemNotContainsSomething() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        // Get all the ofertasList where origem does not contain DEFAULT_ORIGEM
        defaultOfertasShouldNotBeFound("origem.doesNotContain=" + DEFAULT_ORIGEM);

        // Get all the ofertasList where origem does not contain UPDATED_ORIGEM
        defaultOfertasShouldBeFound("origem.doesNotContain=" + UPDATED_ORIGEM);
    }

    @Test
    @Transactional
    void getAllOfertasByPerfilIsEqualToSomething() throws Exception {
        Perfil perfil;
        if (TestUtil.findAll(em, Perfil.class).isEmpty()) {
            ofertasRepository.saveAndFlush(ofertas);
            perfil = PerfilResourceIT.createEntity(em);
        } else {
            perfil = TestUtil.findAll(em, Perfil.class).get(0);
        }
        em.persist(perfil);
        em.flush();
        ofertas.setPerfil(perfil);
        ofertasRepository.saveAndFlush(ofertas);
        Long perfilId = perfil.getId();

        // Get all the ofertasList where perfil equals to perfilId
        defaultOfertasShouldBeFound("perfilId.equals=" + perfilId);

        // Get all the ofertasList where perfil equals to (perfilId + 1)
        defaultOfertasShouldNotBeFound("perfilId.equals=" + (perfilId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOfertasShouldBeFound(String filter) throws Exception {
        restOfertasMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ofertas.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataPostagem").value(hasItem(sameInstant(DEFAULT_DATA_POSTAGEM))))
            .andExpect(jsonPath("$.[*].quantidade").value(hasItem(DEFAULT_QUANTIDADE)))
            .andExpect(jsonPath("$.[*].tipoCarga").value(hasItem(DEFAULT_TIPO_CARGA.toString())))
            .andExpect(jsonPath("$.[*].localizacaoOrigem").value(hasItem(DEFAULT_LOCALIZACAO_ORIGEM.toString())))
            .andExpect(jsonPath("$.[*].localizacaoDestino").value(hasItem(DEFAULT_LOCALIZACAO_DESTINO.toString())))
            .andExpect(jsonPath("$.[*].dataColeta").value(hasItem(DEFAULT_DATA_COLETA.toString())))
            .andExpect(jsonPath("$.[*].dataEntrega").value(hasItem(sameInstant(DEFAULT_DATA_ENTREGA))))
            .andExpect(jsonPath("$.[*].dataModificacao").value(hasItem(sameInstant(DEFAULT_DATA_MODIFICACAO))))
            .andExpect(jsonPath("$.[*].dataFechamento").value(hasItem(sameInstant(DEFAULT_DATA_FECHAMENTO))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].tipoOferta").value(hasItem(DEFAULT_TIPO_OFERTA.toString())))
            .andExpect(jsonPath("$.[*].tipoTransporte").value(hasItem(DEFAULT_TIPO_TRANSPORTE.toString())))
            .andExpect(jsonPath("$.[*].destino").value(hasItem(DEFAULT_DESTINO)))
            .andExpect(jsonPath("$.[*].origem").value(hasItem(DEFAULT_ORIGEM)));

        // Check, that the count call also returns 1
        restOfertasMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOfertasShouldNotBeFound(String filter) throws Exception {
        restOfertasMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOfertasMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOfertas() throws Exception {
        // Get the ofertas
        restOfertasMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOfertas() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        int databaseSizeBeforeUpdate = ofertasRepository.findAll().size();

        // Update the ofertas
        Ofertas updatedOfertas = ofertasRepository.findById(ofertas.getId()).get();
        // Disconnect from session so that the updates on updatedOfertas are not directly saved in db
        em.detach(updatedOfertas);
        updatedOfertas
            .dataPostagem(UPDATED_DATA_POSTAGEM)
            .quantidade(UPDATED_QUANTIDADE)
            .tipoCarga(UPDATED_TIPO_CARGA)
            .localizacaoOrigem(UPDATED_LOCALIZACAO_ORIGEM)
            .localizacaoDestino(UPDATED_LOCALIZACAO_DESTINO)
            .dataColeta(UPDATED_DATA_COLETA)
            .dataEntrega(UPDATED_DATA_ENTREGA)
            .dataModificacao(UPDATED_DATA_MODIFICACAO)
            .dataFechamento(UPDATED_DATA_FECHAMENTO)
            .status(UPDATED_STATUS)
            .tipoOferta(UPDATED_TIPO_OFERTA)
            .tipoTransporte(UPDATED_TIPO_TRANSPORTE)
            .destino(UPDATED_DESTINO)
            .origem(UPDATED_ORIGEM);
        OfertasDTO ofertasDTO = ofertasMapper.toDto(updatedOfertas);

        restOfertasMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ofertasDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ofertasDTO))
            )
            .andExpect(status().isOk());

        // Validate the Ofertas in the database
        List<Ofertas> ofertasList = ofertasRepository.findAll();
        assertThat(ofertasList).hasSize(databaseSizeBeforeUpdate);
        Ofertas testOfertas = ofertasList.get(ofertasList.size() - 1);
        assertThat(testOfertas.getDataPostagem()).isEqualTo(UPDATED_DATA_POSTAGEM);
        assertThat(testOfertas.getQuantidade()).isEqualTo(UPDATED_QUANTIDADE);
        assertThat(testOfertas.getTipoCarga()).isEqualTo(UPDATED_TIPO_CARGA);
        assertThat(testOfertas.getLocalizacaoOrigem()).isEqualTo(UPDATED_LOCALIZACAO_ORIGEM);
        assertThat(testOfertas.getLocalizacaoDestino()).isEqualTo(UPDATED_LOCALIZACAO_DESTINO);
        assertThat(testOfertas.getDataColeta()).isEqualTo(UPDATED_DATA_COLETA);
        assertThat(testOfertas.getDataEntrega()).isEqualTo(UPDATED_DATA_ENTREGA);
        assertThat(testOfertas.getDataModificacao()).isEqualTo(UPDATED_DATA_MODIFICACAO);
        assertThat(testOfertas.getDataFechamento()).isEqualTo(UPDATED_DATA_FECHAMENTO);
        assertThat(testOfertas.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testOfertas.getTipoOferta()).isEqualTo(UPDATED_TIPO_OFERTA);
        assertThat(testOfertas.getTipoTransporte()).isEqualTo(UPDATED_TIPO_TRANSPORTE);
        assertThat(testOfertas.getDestino()).isEqualTo(UPDATED_DESTINO);
        assertThat(testOfertas.getOrigem()).isEqualTo(UPDATED_ORIGEM);
    }

    @Test
    @Transactional
    void putNonExistingOfertas() throws Exception {
        int databaseSizeBeforeUpdate = ofertasRepository.findAll().size();
        ofertas.setId(count.incrementAndGet());

        // Create the Ofertas
        OfertasDTO ofertasDTO = ofertasMapper.toDto(ofertas);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOfertasMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ofertasDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ofertasDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ofertas in the database
        List<Ofertas> ofertasList = ofertasRepository.findAll();
        assertThat(ofertasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOfertas() throws Exception {
        int databaseSizeBeforeUpdate = ofertasRepository.findAll().size();
        ofertas.setId(count.incrementAndGet());

        // Create the Ofertas
        OfertasDTO ofertasDTO = ofertasMapper.toDto(ofertas);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOfertasMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ofertasDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ofertas in the database
        List<Ofertas> ofertasList = ofertasRepository.findAll();
        assertThat(ofertasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOfertas() throws Exception {
        int databaseSizeBeforeUpdate = ofertasRepository.findAll().size();
        ofertas.setId(count.incrementAndGet());

        // Create the Ofertas
        OfertasDTO ofertasDTO = ofertasMapper.toDto(ofertas);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOfertasMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ofertasDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ofertas in the database
        List<Ofertas> ofertasList = ofertasRepository.findAll();
        assertThat(ofertasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOfertasWithPatch() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        int databaseSizeBeforeUpdate = ofertasRepository.findAll().size();

        // Update the ofertas using partial update
        Ofertas partialUpdatedOfertas = new Ofertas();
        partialUpdatedOfertas.setId(ofertas.getId());

        partialUpdatedOfertas
            .dataPostagem(UPDATED_DATA_POSTAGEM)
            .quantidade(UPDATED_QUANTIDADE)
            .dataColeta(UPDATED_DATA_COLETA)
            .dataFechamento(UPDATED_DATA_FECHAMENTO)
            .tipoOferta(UPDATED_TIPO_OFERTA)
            .destino(UPDATED_DESTINO)
            .origem(UPDATED_ORIGEM);

        restOfertasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOfertas.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOfertas))
            )
            .andExpect(status().isOk());

        // Validate the Ofertas in the database
        List<Ofertas> ofertasList = ofertasRepository.findAll();
        assertThat(ofertasList).hasSize(databaseSizeBeforeUpdate);
        Ofertas testOfertas = ofertasList.get(ofertasList.size() - 1);
        assertThat(testOfertas.getDataPostagem()).isEqualTo(UPDATED_DATA_POSTAGEM);
        assertThat(testOfertas.getQuantidade()).isEqualTo(UPDATED_QUANTIDADE);
        assertThat(testOfertas.getTipoCarga()).isEqualTo(DEFAULT_TIPO_CARGA);
        assertThat(testOfertas.getLocalizacaoOrigem()).isEqualTo(DEFAULT_LOCALIZACAO_ORIGEM);
        assertThat(testOfertas.getLocalizacaoDestino()).isEqualTo(DEFAULT_LOCALIZACAO_DESTINO);
        assertThat(testOfertas.getDataColeta()).isEqualTo(UPDATED_DATA_COLETA);
        assertThat(testOfertas.getDataEntrega()).isEqualTo(DEFAULT_DATA_ENTREGA);
        assertThat(testOfertas.getDataModificacao()).isEqualTo(DEFAULT_DATA_MODIFICACAO);
        assertThat(testOfertas.getDataFechamento()).isEqualTo(UPDATED_DATA_FECHAMENTO);
        assertThat(testOfertas.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testOfertas.getTipoOferta()).isEqualTo(UPDATED_TIPO_OFERTA);
        assertThat(testOfertas.getTipoTransporte()).isEqualTo(DEFAULT_TIPO_TRANSPORTE);
        assertThat(testOfertas.getDestino()).isEqualTo(UPDATED_DESTINO);
        assertThat(testOfertas.getOrigem()).isEqualTo(UPDATED_ORIGEM);
    }

    @Test
    @Transactional
    void fullUpdateOfertasWithPatch() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        int databaseSizeBeforeUpdate = ofertasRepository.findAll().size();

        // Update the ofertas using partial update
        Ofertas partialUpdatedOfertas = new Ofertas();
        partialUpdatedOfertas.setId(ofertas.getId());

        partialUpdatedOfertas
            .dataPostagem(UPDATED_DATA_POSTAGEM)
            .quantidade(UPDATED_QUANTIDADE)
            .tipoCarga(UPDATED_TIPO_CARGA)
            .localizacaoOrigem(UPDATED_LOCALIZACAO_ORIGEM)
            .localizacaoDestino(UPDATED_LOCALIZACAO_DESTINO)
            .dataColeta(UPDATED_DATA_COLETA)
            .dataEntrega(UPDATED_DATA_ENTREGA)
            .dataModificacao(UPDATED_DATA_MODIFICACAO)
            .dataFechamento(UPDATED_DATA_FECHAMENTO)
            .status(UPDATED_STATUS)
            .tipoOferta(UPDATED_TIPO_OFERTA)
            .tipoTransporte(UPDATED_TIPO_TRANSPORTE)
            .destino(UPDATED_DESTINO)
            .origem(UPDATED_ORIGEM);

        restOfertasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOfertas.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOfertas))
            )
            .andExpect(status().isOk());

        // Validate the Ofertas in the database
        List<Ofertas> ofertasList = ofertasRepository.findAll();
        assertThat(ofertasList).hasSize(databaseSizeBeforeUpdate);
        Ofertas testOfertas = ofertasList.get(ofertasList.size() - 1);
        assertThat(testOfertas.getDataPostagem()).isEqualTo(UPDATED_DATA_POSTAGEM);
        assertThat(testOfertas.getQuantidade()).isEqualTo(UPDATED_QUANTIDADE);
        assertThat(testOfertas.getTipoCarga()).isEqualTo(UPDATED_TIPO_CARGA);
        assertThat(testOfertas.getLocalizacaoOrigem()).isEqualTo(UPDATED_LOCALIZACAO_ORIGEM);
        assertThat(testOfertas.getLocalizacaoDestino()).isEqualTo(UPDATED_LOCALIZACAO_DESTINO);
        assertThat(testOfertas.getDataColeta()).isEqualTo(UPDATED_DATA_COLETA);
        assertThat(testOfertas.getDataEntrega()).isEqualTo(UPDATED_DATA_ENTREGA);
        assertThat(testOfertas.getDataModificacao()).isEqualTo(UPDATED_DATA_MODIFICACAO);
        assertThat(testOfertas.getDataFechamento()).isEqualTo(UPDATED_DATA_FECHAMENTO);
        assertThat(testOfertas.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testOfertas.getTipoOferta()).isEqualTo(UPDATED_TIPO_OFERTA);
        assertThat(testOfertas.getTipoTransporte()).isEqualTo(UPDATED_TIPO_TRANSPORTE);
        assertThat(testOfertas.getDestino()).isEqualTo(UPDATED_DESTINO);
        assertThat(testOfertas.getOrigem()).isEqualTo(UPDATED_ORIGEM);
    }

    @Test
    @Transactional
    void patchNonExistingOfertas() throws Exception {
        int databaseSizeBeforeUpdate = ofertasRepository.findAll().size();
        ofertas.setId(count.incrementAndGet());

        // Create the Ofertas
        OfertasDTO ofertasDTO = ofertasMapper.toDto(ofertas);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOfertasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ofertasDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ofertasDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ofertas in the database
        List<Ofertas> ofertasList = ofertasRepository.findAll();
        assertThat(ofertasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOfertas() throws Exception {
        int databaseSizeBeforeUpdate = ofertasRepository.findAll().size();
        ofertas.setId(count.incrementAndGet());

        // Create the Ofertas
        OfertasDTO ofertasDTO = ofertasMapper.toDto(ofertas);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOfertasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ofertasDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ofertas in the database
        List<Ofertas> ofertasList = ofertasRepository.findAll();
        assertThat(ofertasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOfertas() throws Exception {
        int databaseSizeBeforeUpdate = ofertasRepository.findAll().size();
        ofertas.setId(count.incrementAndGet());

        // Create the Ofertas
        OfertasDTO ofertasDTO = ofertasMapper.toDto(ofertas);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOfertasMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ofertasDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ofertas in the database
        List<Ofertas> ofertasList = ofertasRepository.findAll();
        assertThat(ofertasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOfertas() throws Exception {
        // Initialize the database
        ofertasRepository.saveAndFlush(ofertas);

        int databaseSizeBeforeDelete = ofertasRepository.findAll().size();

        // Delete the ofertas
        restOfertasMockMvc
            .perform(delete(ENTITY_API_URL_ID, ofertas.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ofertas> ofertasList = ofertasRepository.findAll();
        assertThat(ofertasList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

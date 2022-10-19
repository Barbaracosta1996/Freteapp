package com.infocargas.freteapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.infocargas.freteapp.IntegrationTest;
import com.infocargas.freteapp.domain.CategoriaVeiculo;
import com.infocargas.freteapp.repository.CategoriaVeiculoRepository;
import com.infocargas.freteapp.service.criteria.CategoriaVeiculoCriteria;
import com.infocargas.freteapp.service.dto.CategoriaVeiculoDTO;
import com.infocargas.freteapp.service.mapper.CategoriaVeiculoMapper;
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
 * Integration tests for the {@link CategoriaVeiculoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CategoriaVeiculoResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/categoria-veiculos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CategoriaVeiculoRepository categoriaVeiculoRepository;

    @Autowired
    private CategoriaVeiculoMapper categoriaVeiculoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCategoriaVeiculoMockMvc;

    private CategoriaVeiculo categoriaVeiculo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CategoriaVeiculo createEntity(EntityManager em) {
        CategoriaVeiculo categoriaVeiculo = new CategoriaVeiculo().nome(DEFAULT_NOME);
        return categoriaVeiculo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CategoriaVeiculo createUpdatedEntity(EntityManager em) {
        CategoriaVeiculo categoriaVeiculo = new CategoriaVeiculo().nome(UPDATED_NOME);
        return categoriaVeiculo;
    }

    @BeforeEach
    public void initTest() {
        categoriaVeiculo = createEntity(em);
    }

    @Test
    @Transactional
    void createCategoriaVeiculo() throws Exception {
        int databaseSizeBeforeCreate = categoriaVeiculoRepository.findAll().size();
        // Create the CategoriaVeiculo
        CategoriaVeiculoDTO categoriaVeiculoDTO = categoriaVeiculoMapper.toDto(categoriaVeiculo);
        restCategoriaVeiculoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(categoriaVeiculoDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CategoriaVeiculo in the database
        List<CategoriaVeiculo> categoriaVeiculoList = categoriaVeiculoRepository.findAll();
        assertThat(categoriaVeiculoList).hasSize(databaseSizeBeforeCreate + 1);
        CategoriaVeiculo testCategoriaVeiculo = categoriaVeiculoList.get(categoriaVeiculoList.size() - 1);
        assertThat(testCategoriaVeiculo.getNome()).isEqualTo(DEFAULT_NOME);
    }

    @Test
    @Transactional
    void createCategoriaVeiculoWithExistingId() throws Exception {
        // Create the CategoriaVeiculo with an existing ID
        categoriaVeiculo.setId(1L);
        CategoriaVeiculoDTO categoriaVeiculoDTO = categoriaVeiculoMapper.toDto(categoriaVeiculo);

        int databaseSizeBeforeCreate = categoriaVeiculoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCategoriaVeiculoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(categoriaVeiculoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoriaVeiculo in the database
        List<CategoriaVeiculo> categoriaVeiculoList = categoriaVeiculoRepository.findAll();
        assertThat(categoriaVeiculoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = categoriaVeiculoRepository.findAll().size();
        // set the field null
        categoriaVeiculo.setNome(null);

        // Create the CategoriaVeiculo, which fails.
        CategoriaVeiculoDTO categoriaVeiculoDTO = categoriaVeiculoMapper.toDto(categoriaVeiculo);

        restCategoriaVeiculoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(categoriaVeiculoDTO))
            )
            .andExpect(status().isBadRequest());

        List<CategoriaVeiculo> categoriaVeiculoList = categoriaVeiculoRepository.findAll();
        assertThat(categoriaVeiculoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCategoriaVeiculos() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        // Get all the categoriaVeiculoList
        restCategoriaVeiculoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categoriaVeiculo.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)));
    }

    @Test
    @Transactional
    void getCategoriaVeiculo() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        // Get the categoriaVeiculo
        restCategoriaVeiculoMockMvc
            .perform(get(ENTITY_API_URL_ID, categoriaVeiculo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(categoriaVeiculo.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME));
    }

    @Test
    @Transactional
    void getCategoriaVeiculosByIdFiltering() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        Long id = categoriaVeiculo.getId();

        defaultCategoriaVeiculoShouldBeFound("id.equals=" + id);
        defaultCategoriaVeiculoShouldNotBeFound("id.notEquals=" + id);

        defaultCategoriaVeiculoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCategoriaVeiculoShouldNotBeFound("id.greaterThan=" + id);

        defaultCategoriaVeiculoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCategoriaVeiculoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCategoriaVeiculosByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        // Get all the categoriaVeiculoList where nome equals to DEFAULT_NOME
        defaultCategoriaVeiculoShouldBeFound("nome.equals=" + DEFAULT_NOME);

        // Get all the categoriaVeiculoList where nome equals to UPDATED_NOME
        defaultCategoriaVeiculoShouldNotBeFound("nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllCategoriaVeiculosByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        // Get all the categoriaVeiculoList where nome in DEFAULT_NOME or UPDATED_NOME
        defaultCategoriaVeiculoShouldBeFound("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME);

        // Get all the categoriaVeiculoList where nome equals to UPDATED_NOME
        defaultCategoriaVeiculoShouldNotBeFound("nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllCategoriaVeiculosByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        // Get all the categoriaVeiculoList where nome is not null
        defaultCategoriaVeiculoShouldBeFound("nome.specified=true");

        // Get all the categoriaVeiculoList where nome is null
        defaultCategoriaVeiculoShouldNotBeFound("nome.specified=false");
    }

    @Test
    @Transactional
    void getAllCategoriaVeiculosByNomeContainsSomething() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        // Get all the categoriaVeiculoList where nome contains DEFAULT_NOME
        defaultCategoriaVeiculoShouldBeFound("nome.contains=" + DEFAULT_NOME);

        // Get all the categoriaVeiculoList where nome contains UPDATED_NOME
        defaultCategoriaVeiculoShouldNotBeFound("nome.contains=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllCategoriaVeiculosByNomeNotContainsSomething() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        // Get all the categoriaVeiculoList where nome does not contain DEFAULT_NOME
        defaultCategoriaVeiculoShouldNotBeFound("nome.doesNotContain=" + DEFAULT_NOME);

        // Get all the categoriaVeiculoList where nome does not contain UPDATED_NOME
        defaultCategoriaVeiculoShouldBeFound("nome.doesNotContain=" + UPDATED_NOME);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCategoriaVeiculoShouldBeFound(String filter) throws Exception {
        restCategoriaVeiculoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categoriaVeiculo.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)));

        // Check, that the count call also returns 1
        restCategoriaVeiculoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCategoriaVeiculoShouldNotBeFound(String filter) throws Exception {
        restCategoriaVeiculoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCategoriaVeiculoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCategoriaVeiculo() throws Exception {
        // Get the categoriaVeiculo
        restCategoriaVeiculoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCategoriaVeiculo() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        int databaseSizeBeforeUpdate = categoriaVeiculoRepository.findAll().size();

        // Update the categoriaVeiculo
        CategoriaVeiculo updatedCategoriaVeiculo = categoriaVeiculoRepository.findById(categoriaVeiculo.getId()).get();
        // Disconnect from session so that the updates on updatedCategoriaVeiculo are not directly saved in db
        em.detach(updatedCategoriaVeiculo);
        updatedCategoriaVeiculo.nome(UPDATED_NOME);
        CategoriaVeiculoDTO categoriaVeiculoDTO = categoriaVeiculoMapper.toDto(updatedCategoriaVeiculo);

        restCategoriaVeiculoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, categoriaVeiculoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoriaVeiculoDTO))
            )
            .andExpect(status().isOk());

        // Validate the CategoriaVeiculo in the database
        List<CategoriaVeiculo> categoriaVeiculoList = categoriaVeiculoRepository.findAll();
        assertThat(categoriaVeiculoList).hasSize(databaseSizeBeforeUpdate);
        CategoriaVeiculo testCategoriaVeiculo = categoriaVeiculoList.get(categoriaVeiculoList.size() - 1);
        assertThat(testCategoriaVeiculo.getNome()).isEqualTo(UPDATED_NOME);
    }

    @Test
    @Transactional
    void putNonExistingCategoriaVeiculo() throws Exception {
        int databaseSizeBeforeUpdate = categoriaVeiculoRepository.findAll().size();
        categoriaVeiculo.setId(count.incrementAndGet());

        // Create the CategoriaVeiculo
        CategoriaVeiculoDTO categoriaVeiculoDTO = categoriaVeiculoMapper.toDto(categoriaVeiculo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategoriaVeiculoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, categoriaVeiculoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoriaVeiculoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoriaVeiculo in the database
        List<CategoriaVeiculo> categoriaVeiculoList = categoriaVeiculoRepository.findAll();
        assertThat(categoriaVeiculoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCategoriaVeiculo() throws Exception {
        int databaseSizeBeforeUpdate = categoriaVeiculoRepository.findAll().size();
        categoriaVeiculo.setId(count.incrementAndGet());

        // Create the CategoriaVeiculo
        CategoriaVeiculoDTO categoriaVeiculoDTO = categoriaVeiculoMapper.toDto(categoriaVeiculo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoriaVeiculoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoriaVeiculoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoriaVeiculo in the database
        List<CategoriaVeiculo> categoriaVeiculoList = categoriaVeiculoRepository.findAll();
        assertThat(categoriaVeiculoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCategoriaVeiculo() throws Exception {
        int databaseSizeBeforeUpdate = categoriaVeiculoRepository.findAll().size();
        categoriaVeiculo.setId(count.incrementAndGet());

        // Create the CategoriaVeiculo
        CategoriaVeiculoDTO categoriaVeiculoDTO = categoriaVeiculoMapper.toDto(categoriaVeiculo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoriaVeiculoMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(categoriaVeiculoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CategoriaVeiculo in the database
        List<CategoriaVeiculo> categoriaVeiculoList = categoriaVeiculoRepository.findAll();
        assertThat(categoriaVeiculoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCategoriaVeiculoWithPatch() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        int databaseSizeBeforeUpdate = categoriaVeiculoRepository.findAll().size();

        // Update the categoriaVeiculo using partial update
        CategoriaVeiculo partialUpdatedCategoriaVeiculo = new CategoriaVeiculo();
        partialUpdatedCategoriaVeiculo.setId(categoriaVeiculo.getId());

        partialUpdatedCategoriaVeiculo.nome(UPDATED_NOME);

        restCategoriaVeiculoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategoriaVeiculo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCategoriaVeiculo))
            )
            .andExpect(status().isOk());

        // Validate the CategoriaVeiculo in the database
        List<CategoriaVeiculo> categoriaVeiculoList = categoriaVeiculoRepository.findAll();
        assertThat(categoriaVeiculoList).hasSize(databaseSizeBeforeUpdate);
        CategoriaVeiculo testCategoriaVeiculo = categoriaVeiculoList.get(categoriaVeiculoList.size() - 1);
        assertThat(testCategoriaVeiculo.getNome()).isEqualTo(UPDATED_NOME);
    }

    @Test
    @Transactional
    void fullUpdateCategoriaVeiculoWithPatch() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        int databaseSizeBeforeUpdate = categoriaVeiculoRepository.findAll().size();

        // Update the categoriaVeiculo using partial update
        CategoriaVeiculo partialUpdatedCategoriaVeiculo = new CategoriaVeiculo();
        partialUpdatedCategoriaVeiculo.setId(categoriaVeiculo.getId());

        partialUpdatedCategoriaVeiculo.nome(UPDATED_NOME);

        restCategoriaVeiculoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategoriaVeiculo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCategoriaVeiculo))
            )
            .andExpect(status().isOk());

        // Validate the CategoriaVeiculo in the database
        List<CategoriaVeiculo> categoriaVeiculoList = categoriaVeiculoRepository.findAll();
        assertThat(categoriaVeiculoList).hasSize(databaseSizeBeforeUpdate);
        CategoriaVeiculo testCategoriaVeiculo = categoriaVeiculoList.get(categoriaVeiculoList.size() - 1);
        assertThat(testCategoriaVeiculo.getNome()).isEqualTo(UPDATED_NOME);
    }

    @Test
    @Transactional
    void patchNonExistingCategoriaVeiculo() throws Exception {
        int databaseSizeBeforeUpdate = categoriaVeiculoRepository.findAll().size();
        categoriaVeiculo.setId(count.incrementAndGet());

        // Create the CategoriaVeiculo
        CategoriaVeiculoDTO categoriaVeiculoDTO = categoriaVeiculoMapper.toDto(categoriaVeiculo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategoriaVeiculoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, categoriaVeiculoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(categoriaVeiculoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoriaVeiculo in the database
        List<CategoriaVeiculo> categoriaVeiculoList = categoriaVeiculoRepository.findAll();
        assertThat(categoriaVeiculoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCategoriaVeiculo() throws Exception {
        int databaseSizeBeforeUpdate = categoriaVeiculoRepository.findAll().size();
        categoriaVeiculo.setId(count.incrementAndGet());

        // Create the CategoriaVeiculo
        CategoriaVeiculoDTO categoriaVeiculoDTO = categoriaVeiculoMapper.toDto(categoriaVeiculo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoriaVeiculoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(categoriaVeiculoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoriaVeiculo in the database
        List<CategoriaVeiculo> categoriaVeiculoList = categoriaVeiculoRepository.findAll();
        assertThat(categoriaVeiculoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCategoriaVeiculo() throws Exception {
        int databaseSizeBeforeUpdate = categoriaVeiculoRepository.findAll().size();
        categoriaVeiculo.setId(count.incrementAndGet());

        // Create the CategoriaVeiculo
        CategoriaVeiculoDTO categoriaVeiculoDTO = categoriaVeiculoMapper.toDto(categoriaVeiculo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoriaVeiculoMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(categoriaVeiculoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CategoriaVeiculo in the database
        List<CategoriaVeiculo> categoriaVeiculoList = categoriaVeiculoRepository.findAll();
        assertThat(categoriaVeiculoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCategoriaVeiculo() throws Exception {
        // Initialize the database
        categoriaVeiculoRepository.saveAndFlush(categoriaVeiculo);

        int databaseSizeBeforeDelete = categoriaVeiculoRepository.findAll().size();

        // Delete the categoriaVeiculo
        restCategoriaVeiculoMockMvc
            .perform(delete(ENTITY_API_URL_ID, categoriaVeiculo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CategoriaVeiculo> categoriaVeiculoList = categoriaVeiculoRepository.findAll();
        assertThat(categoriaVeiculoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

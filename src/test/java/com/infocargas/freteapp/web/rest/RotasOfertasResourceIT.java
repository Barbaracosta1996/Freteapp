package com.infocargas.freteapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.infocargas.freteapp.IntegrationTest;
import com.infocargas.freteapp.domain.Ofertas;
import com.infocargas.freteapp.domain.RotasOfertas;
import com.infocargas.freteapp.repository.RotasOfertasRepository;
import com.infocargas.freteapp.service.criteria.RotasOfertasCriteria;
import com.infocargas.freteapp.service.dto.RotasOfertasDTO;
import com.infocargas.freteapp.service.mapper.RotasOfertasMapper;
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
 * Integration tests for the {@link RotasOfertasResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RotasOfertasResourceIT {

    private static final String DEFAULT_ROTAS = "AAAAAAAAAA";
    private static final String UPDATED_ROTAS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/rotas-ofertas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RotasOfertasRepository rotasOfertasRepository;

    @Autowired
    private RotasOfertasMapper rotasOfertasMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRotasOfertasMockMvc;

    private RotasOfertas rotasOfertas;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RotasOfertas createEntity(EntityManager em) {
        RotasOfertas rotasOfertas = new RotasOfertas().rotas(DEFAULT_ROTAS);
        // Add required entity
        Ofertas ofertas;
        if (TestUtil.findAll(em, Ofertas.class).isEmpty()) {
            ofertas = OfertasResourceIT.createEntity(em);
            em.persist(ofertas);
            em.flush();
        } else {
            ofertas = TestUtil.findAll(em, Ofertas.class).get(0);
        }
        rotasOfertas.setOfertas(ofertas);
        return rotasOfertas;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RotasOfertas createUpdatedEntity(EntityManager em) {
        RotasOfertas rotasOfertas = new RotasOfertas().rotas(UPDATED_ROTAS);
        // Add required entity
        Ofertas ofertas;
        if (TestUtil.findAll(em, Ofertas.class).isEmpty()) {
            ofertas = OfertasResourceIT.createUpdatedEntity(em);
            em.persist(ofertas);
            em.flush();
        } else {
            ofertas = TestUtil.findAll(em, Ofertas.class).get(0);
        }
        rotasOfertas.setOfertas(ofertas);
        return rotasOfertas;
    }

    @BeforeEach
    public void initTest() {
        rotasOfertas = createEntity(em);
    }

    @Test
    @Transactional
    void createRotasOfertas() throws Exception {
        int databaseSizeBeforeCreate = rotasOfertasRepository.findAll().size();
        // Create the RotasOfertas
        RotasOfertasDTO rotasOfertasDTO = rotasOfertasMapper.toDto(rotasOfertas);
        restRotasOfertasMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rotasOfertasDTO))
            )
            .andExpect(status().isCreated());

        // Validate the RotasOfertas in the database
        List<RotasOfertas> rotasOfertasList = rotasOfertasRepository.findAll();
        assertThat(rotasOfertasList).hasSize(databaseSizeBeforeCreate + 1);
        RotasOfertas testRotasOfertas = rotasOfertasList.get(rotasOfertasList.size() - 1);
        assertThat(testRotasOfertas.getRotas()).isEqualTo(DEFAULT_ROTAS);
    }

    @Test
    @Transactional
    void createRotasOfertasWithExistingId() throws Exception {
        // Create the RotasOfertas with an existing ID
        rotasOfertas.setId(1L);
        RotasOfertasDTO rotasOfertasDTO = rotasOfertasMapper.toDto(rotasOfertas);

        int databaseSizeBeforeCreate = rotasOfertasRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRotasOfertasMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rotasOfertasDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RotasOfertas in the database
        List<RotasOfertas> rotasOfertasList = rotasOfertasRepository.findAll();
        assertThat(rotasOfertasList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRotasOfertas() throws Exception {
        // Initialize the database
        rotasOfertasRepository.saveAndFlush(rotasOfertas);

        // Get all the rotasOfertasList
        restRotasOfertasMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rotasOfertas.getId().intValue())))
            .andExpect(jsonPath("$.[*].rotas").value(hasItem(DEFAULT_ROTAS.toString())));
    }

    @Test
    @Transactional
    void getRotasOfertas() throws Exception {
        // Initialize the database
        rotasOfertasRepository.saveAndFlush(rotasOfertas);

        // Get the rotasOfertas
        restRotasOfertasMockMvc
            .perform(get(ENTITY_API_URL_ID, rotasOfertas.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rotasOfertas.getId().intValue()))
            .andExpect(jsonPath("$.rotas").value(DEFAULT_ROTAS.toString()));
    }

    @Test
    @Transactional
    void getRotasOfertasByIdFiltering() throws Exception {
        // Initialize the database
        rotasOfertasRepository.saveAndFlush(rotasOfertas);

        Long id = rotasOfertas.getId();

        defaultRotasOfertasShouldBeFound("id.equals=" + id);
        defaultRotasOfertasShouldNotBeFound("id.notEquals=" + id);

        defaultRotasOfertasShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRotasOfertasShouldNotBeFound("id.greaterThan=" + id);

        defaultRotasOfertasShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRotasOfertasShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRotasOfertasByOfertasIsEqualToSomething() throws Exception {
        // Get already existing entity
        Ofertas ofertas = rotasOfertas.getOfertas();
        rotasOfertasRepository.saveAndFlush(rotasOfertas);
        Long ofertasId = ofertas.getId();

        // Get all the rotasOfertasList where ofertas equals to ofertasId
        defaultRotasOfertasShouldBeFound("ofertasId.equals=" + ofertasId);

        // Get all the rotasOfertasList where ofertas equals to (ofertasId + 1)
        defaultRotasOfertasShouldNotBeFound("ofertasId.equals=" + (ofertasId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRotasOfertasShouldBeFound(String filter) throws Exception {
        restRotasOfertasMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rotasOfertas.getId().intValue())))
            .andExpect(jsonPath("$.[*].rotas").value(hasItem(DEFAULT_ROTAS.toString())));

        // Check, that the count call also returns 1
        restRotasOfertasMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRotasOfertasShouldNotBeFound(String filter) throws Exception {
        restRotasOfertasMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRotasOfertasMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRotasOfertas() throws Exception {
        // Get the rotasOfertas
        restRotasOfertasMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRotasOfertas() throws Exception {
        // Initialize the database
        rotasOfertasRepository.saveAndFlush(rotasOfertas);

        int databaseSizeBeforeUpdate = rotasOfertasRepository.findAll().size();

        // Update the rotasOfertas
        RotasOfertas updatedRotasOfertas = rotasOfertasRepository.findById(rotasOfertas.getId()).get();
        // Disconnect from session so that the updates on updatedRotasOfertas are not directly saved in db
        em.detach(updatedRotasOfertas);
        updatedRotasOfertas.rotas(UPDATED_ROTAS);
        RotasOfertasDTO rotasOfertasDTO = rotasOfertasMapper.toDto(updatedRotasOfertas);

        restRotasOfertasMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rotasOfertasDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rotasOfertasDTO))
            )
            .andExpect(status().isOk());

        // Validate the RotasOfertas in the database
        List<RotasOfertas> rotasOfertasList = rotasOfertasRepository.findAll();
        assertThat(rotasOfertasList).hasSize(databaseSizeBeforeUpdate);
        RotasOfertas testRotasOfertas = rotasOfertasList.get(rotasOfertasList.size() - 1);
        assertThat(testRotasOfertas.getRotas()).isEqualTo(UPDATED_ROTAS);
    }

    @Test
    @Transactional
    void putNonExistingRotasOfertas() throws Exception {
        int databaseSizeBeforeUpdate = rotasOfertasRepository.findAll().size();
        rotasOfertas.setId(count.incrementAndGet());

        // Create the RotasOfertas
        RotasOfertasDTO rotasOfertasDTO = rotasOfertasMapper.toDto(rotasOfertas);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRotasOfertasMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rotasOfertasDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rotasOfertasDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RotasOfertas in the database
        List<RotasOfertas> rotasOfertasList = rotasOfertasRepository.findAll();
        assertThat(rotasOfertasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRotasOfertas() throws Exception {
        int databaseSizeBeforeUpdate = rotasOfertasRepository.findAll().size();
        rotasOfertas.setId(count.incrementAndGet());

        // Create the RotasOfertas
        RotasOfertasDTO rotasOfertasDTO = rotasOfertasMapper.toDto(rotasOfertas);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRotasOfertasMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rotasOfertasDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RotasOfertas in the database
        List<RotasOfertas> rotasOfertasList = rotasOfertasRepository.findAll();
        assertThat(rotasOfertasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRotasOfertas() throws Exception {
        int databaseSizeBeforeUpdate = rotasOfertasRepository.findAll().size();
        rotasOfertas.setId(count.incrementAndGet());

        // Create the RotasOfertas
        RotasOfertasDTO rotasOfertasDTO = rotasOfertasMapper.toDto(rotasOfertas);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRotasOfertasMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(rotasOfertasDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RotasOfertas in the database
        List<RotasOfertas> rotasOfertasList = rotasOfertasRepository.findAll();
        assertThat(rotasOfertasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRotasOfertasWithPatch() throws Exception {
        // Initialize the database
        rotasOfertasRepository.saveAndFlush(rotasOfertas);

        int databaseSizeBeforeUpdate = rotasOfertasRepository.findAll().size();

        // Update the rotasOfertas using partial update
        RotasOfertas partialUpdatedRotasOfertas = new RotasOfertas();
        partialUpdatedRotasOfertas.setId(rotasOfertas.getId());

        partialUpdatedRotasOfertas.rotas(UPDATED_ROTAS);

        restRotasOfertasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRotasOfertas.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRotasOfertas))
            )
            .andExpect(status().isOk());

        // Validate the RotasOfertas in the database
        List<RotasOfertas> rotasOfertasList = rotasOfertasRepository.findAll();
        assertThat(rotasOfertasList).hasSize(databaseSizeBeforeUpdate);
        RotasOfertas testRotasOfertas = rotasOfertasList.get(rotasOfertasList.size() - 1);
        assertThat(testRotasOfertas.getRotas()).isEqualTo(UPDATED_ROTAS);
    }

    @Test
    @Transactional
    void fullUpdateRotasOfertasWithPatch() throws Exception {
        // Initialize the database
        rotasOfertasRepository.saveAndFlush(rotasOfertas);

        int databaseSizeBeforeUpdate = rotasOfertasRepository.findAll().size();

        // Update the rotasOfertas using partial update
        RotasOfertas partialUpdatedRotasOfertas = new RotasOfertas();
        partialUpdatedRotasOfertas.setId(rotasOfertas.getId());

        partialUpdatedRotasOfertas.rotas(UPDATED_ROTAS);

        restRotasOfertasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRotasOfertas.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRotasOfertas))
            )
            .andExpect(status().isOk());

        // Validate the RotasOfertas in the database
        List<RotasOfertas> rotasOfertasList = rotasOfertasRepository.findAll();
        assertThat(rotasOfertasList).hasSize(databaseSizeBeforeUpdate);
        RotasOfertas testRotasOfertas = rotasOfertasList.get(rotasOfertasList.size() - 1);
        assertThat(testRotasOfertas.getRotas()).isEqualTo(UPDATED_ROTAS);
    }

    @Test
    @Transactional
    void patchNonExistingRotasOfertas() throws Exception {
        int databaseSizeBeforeUpdate = rotasOfertasRepository.findAll().size();
        rotasOfertas.setId(count.incrementAndGet());

        // Create the RotasOfertas
        RotasOfertasDTO rotasOfertasDTO = rotasOfertasMapper.toDto(rotasOfertas);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRotasOfertasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rotasOfertasDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rotasOfertasDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RotasOfertas in the database
        List<RotasOfertas> rotasOfertasList = rotasOfertasRepository.findAll();
        assertThat(rotasOfertasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRotasOfertas() throws Exception {
        int databaseSizeBeforeUpdate = rotasOfertasRepository.findAll().size();
        rotasOfertas.setId(count.incrementAndGet());

        // Create the RotasOfertas
        RotasOfertasDTO rotasOfertasDTO = rotasOfertasMapper.toDto(rotasOfertas);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRotasOfertasMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rotasOfertasDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RotasOfertas in the database
        List<RotasOfertas> rotasOfertasList = rotasOfertasRepository.findAll();
        assertThat(rotasOfertasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRotasOfertas() throws Exception {
        int databaseSizeBeforeUpdate = rotasOfertasRepository.findAll().size();
        rotasOfertas.setId(count.incrementAndGet());

        // Create the RotasOfertas
        RotasOfertasDTO rotasOfertasDTO = rotasOfertasMapper.toDto(rotasOfertas);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRotasOfertasMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rotasOfertasDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RotasOfertas in the database
        List<RotasOfertas> rotasOfertasList = rotasOfertasRepository.findAll();
        assertThat(rotasOfertasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRotasOfertas() throws Exception {
        // Initialize the database
        rotasOfertasRepository.saveAndFlush(rotasOfertas);

        int databaseSizeBeforeDelete = rotasOfertasRepository.findAll().size();

        // Delete the rotasOfertas
        restRotasOfertasMockMvc
            .perform(delete(ENTITY_API_URL_ID, rotasOfertas.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RotasOfertas> rotasOfertasList = rotasOfertasRepository.findAll();
        assertThat(rotasOfertasList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

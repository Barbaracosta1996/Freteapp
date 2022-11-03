package com.infocargas.freteapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.infocargas.freteapp.IntegrationTest;
import com.infocargas.freteapp.domain.SettingsCargaApp;
import com.infocargas.freteapp.repository.SettingsCargaAppRepository;
import com.infocargas.freteapp.service.dto.SettingsCargaAppDTO;
import com.infocargas.freteapp.service.mapper.SettingsCargaAppMapper;
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
 * Integration tests for the {@link SettingsCargaAppResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SettingsCargaAppResourceIT {

    private static final String DEFAULT_RD_CODE = "AAAAAAAAAA";
    private static final String UPDATED_RD_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_WA_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_WA_TOKEN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/settings-carga-apps";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SettingsCargaAppRepository settingsCargaAppRepository;

    @Autowired
    private SettingsCargaAppMapper settingsCargaAppMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSettingsCargaAppMockMvc;

    private SettingsCargaApp settingsCargaApp;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SettingsCargaApp createEntity(EntityManager em) {
        SettingsCargaApp settingsCargaApp = new SettingsCargaApp().rdCode(DEFAULT_RD_CODE).waToken(DEFAULT_WA_TOKEN);
        return settingsCargaApp;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SettingsCargaApp createUpdatedEntity(EntityManager em) {
        SettingsCargaApp settingsCargaApp = new SettingsCargaApp().rdCode(UPDATED_RD_CODE).waToken(UPDATED_WA_TOKEN);
        return settingsCargaApp;
    }

    @BeforeEach
    public void initTest() {
        settingsCargaApp = createEntity(em);
    }

    @Test
    @Transactional
    void createSettingsCargaApp() throws Exception {
        int databaseSizeBeforeCreate = settingsCargaAppRepository.findAll().size();
        // Create the SettingsCargaApp
        SettingsCargaAppDTO settingsCargaAppDTO = settingsCargaAppMapper.toDto(settingsCargaApp);
        restSettingsCargaAppMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(settingsCargaAppDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SettingsCargaApp in the database
        List<SettingsCargaApp> settingsCargaAppList = settingsCargaAppRepository.findAll();
        assertThat(settingsCargaAppList).hasSize(databaseSizeBeforeCreate + 1);
        SettingsCargaApp testSettingsCargaApp = settingsCargaAppList.get(settingsCargaAppList.size() - 1);
        assertThat(testSettingsCargaApp.getRdCode()).isEqualTo(DEFAULT_RD_CODE);
        assertThat(testSettingsCargaApp.getWaToken()).isEqualTo(DEFAULT_WA_TOKEN);
    }

    @Test
    @Transactional
    void createSettingsCargaAppWithExistingId() throws Exception {
        // Create the SettingsCargaApp with an existing ID
        settingsCargaApp.setId(1L);
        SettingsCargaAppDTO settingsCargaAppDTO = settingsCargaAppMapper.toDto(settingsCargaApp);

        int databaseSizeBeforeCreate = settingsCargaAppRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSettingsCargaAppMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(settingsCargaAppDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SettingsCargaApp in the database
        List<SettingsCargaApp> settingsCargaAppList = settingsCargaAppRepository.findAll();
        assertThat(settingsCargaAppList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSettingsCargaApps() throws Exception {
        // Initialize the database
        settingsCargaAppRepository.saveAndFlush(settingsCargaApp);

        // Get all the settingsCargaAppList
        restSettingsCargaAppMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(settingsCargaApp.getId().intValue())))
            .andExpect(jsonPath("$.[*].rdCode").value(hasItem(DEFAULT_RD_CODE)))
            .andExpect(jsonPath("$.[*].waToken").value(hasItem(DEFAULT_WA_TOKEN)));
    }

    @Test
    @Transactional
    void getSettingsCargaApp() throws Exception {
        // Initialize the database
        settingsCargaAppRepository.saveAndFlush(settingsCargaApp);

        // Get the settingsCargaApp
        restSettingsCargaAppMockMvc
            .perform(get(ENTITY_API_URL_ID, settingsCargaApp.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(settingsCargaApp.getId().intValue()))
            .andExpect(jsonPath("$.rdCode").value(DEFAULT_RD_CODE))
            .andExpect(jsonPath("$.waToken").value(DEFAULT_WA_TOKEN));
    }

    @Test
    @Transactional
    void getNonExistingSettingsCargaApp() throws Exception {
        // Get the settingsCargaApp
        restSettingsCargaAppMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSettingsCargaApp() throws Exception {
        // Initialize the database
        settingsCargaAppRepository.saveAndFlush(settingsCargaApp);

        int databaseSizeBeforeUpdate = settingsCargaAppRepository.findAll().size();

        // Update the settingsCargaApp
        SettingsCargaApp updatedSettingsCargaApp = settingsCargaAppRepository.findById(settingsCargaApp.getId()).get();
        // Disconnect from session so that the updates on updatedSettingsCargaApp are not directly saved in db
        em.detach(updatedSettingsCargaApp);
        updatedSettingsCargaApp.rdCode(UPDATED_RD_CODE).waToken(UPDATED_WA_TOKEN);
        SettingsCargaAppDTO settingsCargaAppDTO = settingsCargaAppMapper.toDto(updatedSettingsCargaApp);

        restSettingsCargaAppMockMvc
            .perform(
                put(ENTITY_API_URL_ID, settingsCargaAppDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(settingsCargaAppDTO))
            )
            .andExpect(status().isOk());

        // Validate the SettingsCargaApp in the database
        List<SettingsCargaApp> settingsCargaAppList = settingsCargaAppRepository.findAll();
        assertThat(settingsCargaAppList).hasSize(databaseSizeBeforeUpdate);
        SettingsCargaApp testSettingsCargaApp = settingsCargaAppList.get(settingsCargaAppList.size() - 1);
        assertThat(testSettingsCargaApp.getRdCode()).isEqualTo(UPDATED_RD_CODE);
        assertThat(testSettingsCargaApp.getWaToken()).isEqualTo(UPDATED_WA_TOKEN);
    }

    @Test
    @Transactional
    void putNonExistingSettingsCargaApp() throws Exception {
        int databaseSizeBeforeUpdate = settingsCargaAppRepository.findAll().size();
        settingsCargaApp.setId(count.incrementAndGet());

        // Create the SettingsCargaApp
        SettingsCargaAppDTO settingsCargaAppDTO = settingsCargaAppMapper.toDto(settingsCargaApp);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSettingsCargaAppMockMvc
            .perform(
                put(ENTITY_API_URL_ID, settingsCargaAppDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(settingsCargaAppDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SettingsCargaApp in the database
        List<SettingsCargaApp> settingsCargaAppList = settingsCargaAppRepository.findAll();
        assertThat(settingsCargaAppList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSettingsCargaApp() throws Exception {
        int databaseSizeBeforeUpdate = settingsCargaAppRepository.findAll().size();
        settingsCargaApp.setId(count.incrementAndGet());

        // Create the SettingsCargaApp
        SettingsCargaAppDTO settingsCargaAppDTO = settingsCargaAppMapper.toDto(settingsCargaApp);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSettingsCargaAppMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(settingsCargaAppDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SettingsCargaApp in the database
        List<SettingsCargaApp> settingsCargaAppList = settingsCargaAppRepository.findAll();
        assertThat(settingsCargaAppList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSettingsCargaApp() throws Exception {
        int databaseSizeBeforeUpdate = settingsCargaAppRepository.findAll().size();
        settingsCargaApp.setId(count.incrementAndGet());

        // Create the SettingsCargaApp
        SettingsCargaAppDTO settingsCargaAppDTO = settingsCargaAppMapper.toDto(settingsCargaApp);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSettingsCargaAppMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(settingsCargaAppDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SettingsCargaApp in the database
        List<SettingsCargaApp> settingsCargaAppList = settingsCargaAppRepository.findAll();
        assertThat(settingsCargaAppList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSettingsCargaAppWithPatch() throws Exception {
        // Initialize the database
        settingsCargaAppRepository.saveAndFlush(settingsCargaApp);

        int databaseSizeBeforeUpdate = settingsCargaAppRepository.findAll().size();

        // Update the settingsCargaApp using partial update
        SettingsCargaApp partialUpdatedSettingsCargaApp = new SettingsCargaApp();
        partialUpdatedSettingsCargaApp.setId(settingsCargaApp.getId());

        partialUpdatedSettingsCargaApp.waToken(UPDATED_WA_TOKEN);

        restSettingsCargaAppMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSettingsCargaApp.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSettingsCargaApp))
            )
            .andExpect(status().isOk());

        // Validate the SettingsCargaApp in the database
        List<SettingsCargaApp> settingsCargaAppList = settingsCargaAppRepository.findAll();
        assertThat(settingsCargaAppList).hasSize(databaseSizeBeforeUpdate);
        SettingsCargaApp testSettingsCargaApp = settingsCargaAppList.get(settingsCargaAppList.size() - 1);
        assertThat(testSettingsCargaApp.getRdCode()).isEqualTo(DEFAULT_RD_CODE);
        assertThat(testSettingsCargaApp.getWaToken()).isEqualTo(UPDATED_WA_TOKEN);
    }

    @Test
    @Transactional
    void fullUpdateSettingsCargaAppWithPatch() throws Exception {
        // Initialize the database
        settingsCargaAppRepository.saveAndFlush(settingsCargaApp);

        int databaseSizeBeforeUpdate = settingsCargaAppRepository.findAll().size();

        // Update the settingsCargaApp using partial update
        SettingsCargaApp partialUpdatedSettingsCargaApp = new SettingsCargaApp();
        partialUpdatedSettingsCargaApp.setId(settingsCargaApp.getId());

        partialUpdatedSettingsCargaApp.rdCode(UPDATED_RD_CODE).waToken(UPDATED_WA_TOKEN);

        restSettingsCargaAppMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSettingsCargaApp.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSettingsCargaApp))
            )
            .andExpect(status().isOk());

        // Validate the SettingsCargaApp in the database
        List<SettingsCargaApp> settingsCargaAppList = settingsCargaAppRepository.findAll();
        assertThat(settingsCargaAppList).hasSize(databaseSizeBeforeUpdate);
        SettingsCargaApp testSettingsCargaApp = settingsCargaAppList.get(settingsCargaAppList.size() - 1);
        assertThat(testSettingsCargaApp.getRdCode()).isEqualTo(UPDATED_RD_CODE);
        assertThat(testSettingsCargaApp.getWaToken()).isEqualTo(UPDATED_WA_TOKEN);
    }

    @Test
    @Transactional
    void patchNonExistingSettingsCargaApp() throws Exception {
        int databaseSizeBeforeUpdate = settingsCargaAppRepository.findAll().size();
        settingsCargaApp.setId(count.incrementAndGet());

        // Create the SettingsCargaApp
        SettingsCargaAppDTO settingsCargaAppDTO = settingsCargaAppMapper.toDto(settingsCargaApp);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSettingsCargaAppMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, settingsCargaAppDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(settingsCargaAppDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SettingsCargaApp in the database
        List<SettingsCargaApp> settingsCargaAppList = settingsCargaAppRepository.findAll();
        assertThat(settingsCargaAppList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSettingsCargaApp() throws Exception {
        int databaseSizeBeforeUpdate = settingsCargaAppRepository.findAll().size();
        settingsCargaApp.setId(count.incrementAndGet());

        // Create the SettingsCargaApp
        SettingsCargaAppDTO settingsCargaAppDTO = settingsCargaAppMapper.toDto(settingsCargaApp);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSettingsCargaAppMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(settingsCargaAppDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SettingsCargaApp in the database
        List<SettingsCargaApp> settingsCargaAppList = settingsCargaAppRepository.findAll();
        assertThat(settingsCargaAppList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSettingsCargaApp() throws Exception {
        int databaseSizeBeforeUpdate = settingsCargaAppRepository.findAll().size();
        settingsCargaApp.setId(count.incrementAndGet());

        // Create the SettingsCargaApp
        SettingsCargaAppDTO settingsCargaAppDTO = settingsCargaAppMapper.toDto(settingsCargaApp);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSettingsCargaAppMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(settingsCargaAppDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SettingsCargaApp in the database
        List<SettingsCargaApp> settingsCargaAppList = settingsCargaAppRepository.findAll();
        assertThat(settingsCargaAppList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSettingsCargaApp() throws Exception {
        // Initialize the database
        settingsCargaAppRepository.saveAndFlush(settingsCargaApp);

        int databaseSizeBeforeDelete = settingsCargaAppRepository.findAll().size();

        // Delete the settingsCargaApp
        restSettingsCargaAppMockMvc
            .perform(delete(ENTITY_API_URL_ID, settingsCargaApp.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SettingsCargaApp> settingsCargaAppList = settingsCargaAppRepository.findAll();
        assertThat(settingsCargaAppList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

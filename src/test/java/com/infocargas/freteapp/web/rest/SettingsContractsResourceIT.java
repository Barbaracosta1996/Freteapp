package com.infocargas.freteapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.infocargas.freteapp.IntegrationTest;
import com.infocargas.freteapp.domain.SettingsContracts;
import com.infocargas.freteapp.repository.SettingsContractsRepository;
import com.infocargas.freteapp.service.dto.SettingsContractsDTO;
import com.infocargas.freteapp.service.mapper.SettingsContractsMapper;
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
 * Integration tests for the {@link SettingsContractsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SettingsContractsResourceIT {

    private static final byte[] DEFAULT_TERMS = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_TERMS = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_TERMS_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_TERMS_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_CONTRACT_DEFAULT = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_CONTRACT_DEFAULT = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_CONTRACT_DEFAULT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_CONTRACT_DEFAULT_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_PRIVACY = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PRIVACY = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PRIVACY_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PRIVACY_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/settings-contracts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SettingsContractsRepository settingsContractsRepository;

    @Autowired
    private SettingsContractsMapper settingsContractsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSettingsContractsMockMvc;

    private SettingsContracts settingsContracts;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SettingsContracts createEntity(EntityManager em) {
        SettingsContracts settingsContracts = new SettingsContracts()
            .terms(DEFAULT_TERMS)
            .termsContentType(DEFAULT_TERMS_CONTENT_TYPE)
            .contractDefault(DEFAULT_CONTRACT_DEFAULT)
            .contractDefaultContentType(DEFAULT_CONTRACT_DEFAULT_CONTENT_TYPE)
            .privacy(DEFAULT_PRIVACY)
            .privacyContentType(DEFAULT_PRIVACY_CONTENT_TYPE);
        return settingsContracts;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SettingsContracts createUpdatedEntity(EntityManager em) {
        SettingsContracts settingsContracts = new SettingsContracts()
            .terms(UPDATED_TERMS)
            .termsContentType(UPDATED_TERMS_CONTENT_TYPE)
            .contractDefault(UPDATED_CONTRACT_DEFAULT)
            .contractDefaultContentType(UPDATED_CONTRACT_DEFAULT_CONTENT_TYPE)
            .privacy(UPDATED_PRIVACY)
            .privacyContentType(UPDATED_PRIVACY_CONTENT_TYPE);
        return settingsContracts;
    }

    @BeforeEach
    public void initTest() {
        settingsContracts = createEntity(em);
    }

    @Test
    @Transactional
    void createSettingsContracts() throws Exception {
        int databaseSizeBeforeCreate = settingsContractsRepository.findAll().size();
        // Create the SettingsContracts
        SettingsContractsDTO settingsContractsDTO = settingsContractsMapper.toDto(settingsContracts);
        restSettingsContractsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(settingsContractsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SettingsContracts in the database
        List<SettingsContracts> settingsContractsList = settingsContractsRepository.findAll();
        assertThat(settingsContractsList).hasSize(databaseSizeBeforeCreate + 1);
        SettingsContracts testSettingsContracts = settingsContractsList.get(settingsContractsList.size() - 1);
        assertThat(testSettingsContracts.getTerms()).isEqualTo(DEFAULT_TERMS);
        assertThat(testSettingsContracts.getTermsContentType()).isEqualTo(DEFAULT_TERMS_CONTENT_TYPE);
        assertThat(testSettingsContracts.getContractDefault()).isEqualTo(DEFAULT_CONTRACT_DEFAULT);
        assertThat(testSettingsContracts.getContractDefaultContentType()).isEqualTo(DEFAULT_CONTRACT_DEFAULT_CONTENT_TYPE);
        assertThat(testSettingsContracts.getPrivacy()).isEqualTo(DEFAULT_PRIVACY);
        assertThat(testSettingsContracts.getPrivacyContentType()).isEqualTo(DEFAULT_PRIVACY_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createSettingsContractsWithExistingId() throws Exception {
        // Create the SettingsContracts with an existing ID
        settingsContracts.setId(1L);
        SettingsContractsDTO settingsContractsDTO = settingsContractsMapper.toDto(settingsContracts);

        int databaseSizeBeforeCreate = settingsContractsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSettingsContractsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(settingsContractsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SettingsContracts in the database
        List<SettingsContracts> settingsContractsList = settingsContractsRepository.findAll();
        assertThat(settingsContractsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSettingsContracts() throws Exception {
        // Initialize the database
        settingsContractsRepository.saveAndFlush(settingsContracts);

        // Get all the settingsContractsList
        restSettingsContractsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(settingsContracts.getId().intValue())))
            .andExpect(jsonPath("$.[*].termsContentType").value(hasItem(DEFAULT_TERMS_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].terms").value(hasItem(Base64Utils.encodeToString(DEFAULT_TERMS))))
            .andExpect(jsonPath("$.[*].contractDefaultContentType").value(hasItem(DEFAULT_CONTRACT_DEFAULT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].contractDefault").value(hasItem(Base64Utils.encodeToString(DEFAULT_CONTRACT_DEFAULT))))
            .andExpect(jsonPath("$.[*].privacyContentType").value(hasItem(DEFAULT_PRIVACY_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].privacy").value(hasItem(Base64Utils.encodeToString(DEFAULT_PRIVACY))));
    }

    @Test
    @Transactional
    void getSettingsContracts() throws Exception {
        // Initialize the database
        settingsContractsRepository.saveAndFlush(settingsContracts);

        // Get the settingsContracts
        restSettingsContractsMockMvc
            .perform(get(ENTITY_API_URL_ID, settingsContracts.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(settingsContracts.getId().intValue()))
            .andExpect(jsonPath("$.termsContentType").value(DEFAULT_TERMS_CONTENT_TYPE))
            .andExpect(jsonPath("$.terms").value(Base64Utils.encodeToString(DEFAULT_TERMS)))
            .andExpect(jsonPath("$.contractDefaultContentType").value(DEFAULT_CONTRACT_DEFAULT_CONTENT_TYPE))
            .andExpect(jsonPath("$.contractDefault").value(Base64Utils.encodeToString(DEFAULT_CONTRACT_DEFAULT)))
            .andExpect(jsonPath("$.privacyContentType").value(DEFAULT_PRIVACY_CONTENT_TYPE))
            .andExpect(jsonPath("$.privacy").value(Base64Utils.encodeToString(DEFAULT_PRIVACY)));
    }

    @Test
    @Transactional
    void getNonExistingSettingsContracts() throws Exception {
        // Get the settingsContracts
        restSettingsContractsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSettingsContracts() throws Exception {
        // Initialize the database
        settingsContractsRepository.saveAndFlush(settingsContracts);

        int databaseSizeBeforeUpdate = settingsContractsRepository.findAll().size();

        // Update the settingsContracts
        SettingsContracts updatedSettingsContracts = settingsContractsRepository.findById(settingsContracts.getId()).get();
        // Disconnect from session so that the updates on updatedSettingsContracts are not directly saved in db
        em.detach(updatedSettingsContracts);
        updatedSettingsContracts
            .terms(UPDATED_TERMS)
            .termsContentType(UPDATED_TERMS_CONTENT_TYPE)
            .contractDefault(UPDATED_CONTRACT_DEFAULT)
            .contractDefaultContentType(UPDATED_CONTRACT_DEFAULT_CONTENT_TYPE)
            .privacy(UPDATED_PRIVACY)
            .privacyContentType(UPDATED_PRIVACY_CONTENT_TYPE);
        SettingsContractsDTO settingsContractsDTO = settingsContractsMapper.toDto(updatedSettingsContracts);

        restSettingsContractsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, settingsContractsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(settingsContractsDTO))
            )
            .andExpect(status().isOk());

        // Validate the SettingsContracts in the database
        List<SettingsContracts> settingsContractsList = settingsContractsRepository.findAll();
        assertThat(settingsContractsList).hasSize(databaseSizeBeforeUpdate);
        SettingsContracts testSettingsContracts = settingsContractsList.get(settingsContractsList.size() - 1);
        assertThat(testSettingsContracts.getTerms()).isEqualTo(UPDATED_TERMS);
        assertThat(testSettingsContracts.getTermsContentType()).isEqualTo(UPDATED_TERMS_CONTENT_TYPE);
        assertThat(testSettingsContracts.getContractDefault()).isEqualTo(UPDATED_CONTRACT_DEFAULT);
        assertThat(testSettingsContracts.getContractDefaultContentType()).isEqualTo(UPDATED_CONTRACT_DEFAULT_CONTENT_TYPE);
        assertThat(testSettingsContracts.getPrivacy()).isEqualTo(UPDATED_PRIVACY);
        assertThat(testSettingsContracts.getPrivacyContentType()).isEqualTo(UPDATED_PRIVACY_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingSettingsContracts() throws Exception {
        int databaseSizeBeforeUpdate = settingsContractsRepository.findAll().size();
        settingsContracts.setId(count.incrementAndGet());

        // Create the SettingsContracts
        SettingsContractsDTO settingsContractsDTO = settingsContractsMapper.toDto(settingsContracts);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSettingsContractsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, settingsContractsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(settingsContractsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SettingsContracts in the database
        List<SettingsContracts> settingsContractsList = settingsContractsRepository.findAll();
        assertThat(settingsContractsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSettingsContracts() throws Exception {
        int databaseSizeBeforeUpdate = settingsContractsRepository.findAll().size();
        settingsContracts.setId(count.incrementAndGet());

        // Create the SettingsContracts
        SettingsContractsDTO settingsContractsDTO = settingsContractsMapper.toDto(settingsContracts);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSettingsContractsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(settingsContractsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SettingsContracts in the database
        List<SettingsContracts> settingsContractsList = settingsContractsRepository.findAll();
        assertThat(settingsContractsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSettingsContracts() throws Exception {
        int databaseSizeBeforeUpdate = settingsContractsRepository.findAll().size();
        settingsContracts.setId(count.incrementAndGet());

        // Create the SettingsContracts
        SettingsContractsDTO settingsContractsDTO = settingsContractsMapper.toDto(settingsContracts);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSettingsContractsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(settingsContractsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SettingsContracts in the database
        List<SettingsContracts> settingsContractsList = settingsContractsRepository.findAll();
        assertThat(settingsContractsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSettingsContractsWithPatch() throws Exception {
        // Initialize the database
        settingsContractsRepository.saveAndFlush(settingsContracts);

        int databaseSizeBeforeUpdate = settingsContractsRepository.findAll().size();

        // Update the settingsContracts using partial update
        SettingsContracts partialUpdatedSettingsContracts = new SettingsContracts();
        partialUpdatedSettingsContracts.setId(settingsContracts.getId());

        partialUpdatedSettingsContracts
            .terms(UPDATED_TERMS)
            .termsContentType(UPDATED_TERMS_CONTENT_TYPE)
            .privacy(UPDATED_PRIVACY)
            .privacyContentType(UPDATED_PRIVACY_CONTENT_TYPE);

        restSettingsContractsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSettingsContracts.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSettingsContracts))
            )
            .andExpect(status().isOk());

        // Validate the SettingsContracts in the database
        List<SettingsContracts> settingsContractsList = settingsContractsRepository.findAll();
        assertThat(settingsContractsList).hasSize(databaseSizeBeforeUpdate);
        SettingsContracts testSettingsContracts = settingsContractsList.get(settingsContractsList.size() - 1);
        assertThat(testSettingsContracts.getTerms()).isEqualTo(UPDATED_TERMS);
        assertThat(testSettingsContracts.getTermsContentType()).isEqualTo(UPDATED_TERMS_CONTENT_TYPE);
        assertThat(testSettingsContracts.getContractDefault()).isEqualTo(DEFAULT_CONTRACT_DEFAULT);
        assertThat(testSettingsContracts.getContractDefaultContentType()).isEqualTo(DEFAULT_CONTRACT_DEFAULT_CONTENT_TYPE);
        assertThat(testSettingsContracts.getPrivacy()).isEqualTo(UPDATED_PRIVACY);
        assertThat(testSettingsContracts.getPrivacyContentType()).isEqualTo(UPDATED_PRIVACY_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateSettingsContractsWithPatch() throws Exception {
        // Initialize the database
        settingsContractsRepository.saveAndFlush(settingsContracts);

        int databaseSizeBeforeUpdate = settingsContractsRepository.findAll().size();

        // Update the settingsContracts using partial update
        SettingsContracts partialUpdatedSettingsContracts = new SettingsContracts();
        partialUpdatedSettingsContracts.setId(settingsContracts.getId());

        partialUpdatedSettingsContracts
            .terms(UPDATED_TERMS)
            .termsContentType(UPDATED_TERMS_CONTENT_TYPE)
            .contractDefault(UPDATED_CONTRACT_DEFAULT)
            .contractDefaultContentType(UPDATED_CONTRACT_DEFAULT_CONTENT_TYPE)
            .privacy(UPDATED_PRIVACY)
            .privacyContentType(UPDATED_PRIVACY_CONTENT_TYPE);

        restSettingsContractsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSettingsContracts.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSettingsContracts))
            )
            .andExpect(status().isOk());

        // Validate the SettingsContracts in the database
        List<SettingsContracts> settingsContractsList = settingsContractsRepository.findAll();
        assertThat(settingsContractsList).hasSize(databaseSizeBeforeUpdate);
        SettingsContracts testSettingsContracts = settingsContractsList.get(settingsContractsList.size() - 1);
        assertThat(testSettingsContracts.getTerms()).isEqualTo(UPDATED_TERMS);
        assertThat(testSettingsContracts.getTermsContentType()).isEqualTo(UPDATED_TERMS_CONTENT_TYPE);
        assertThat(testSettingsContracts.getContractDefault()).isEqualTo(UPDATED_CONTRACT_DEFAULT);
        assertThat(testSettingsContracts.getContractDefaultContentType()).isEqualTo(UPDATED_CONTRACT_DEFAULT_CONTENT_TYPE);
        assertThat(testSettingsContracts.getPrivacy()).isEqualTo(UPDATED_PRIVACY);
        assertThat(testSettingsContracts.getPrivacyContentType()).isEqualTo(UPDATED_PRIVACY_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingSettingsContracts() throws Exception {
        int databaseSizeBeforeUpdate = settingsContractsRepository.findAll().size();
        settingsContracts.setId(count.incrementAndGet());

        // Create the SettingsContracts
        SettingsContractsDTO settingsContractsDTO = settingsContractsMapper.toDto(settingsContracts);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSettingsContractsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, settingsContractsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(settingsContractsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SettingsContracts in the database
        List<SettingsContracts> settingsContractsList = settingsContractsRepository.findAll();
        assertThat(settingsContractsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSettingsContracts() throws Exception {
        int databaseSizeBeforeUpdate = settingsContractsRepository.findAll().size();
        settingsContracts.setId(count.incrementAndGet());

        // Create the SettingsContracts
        SettingsContractsDTO settingsContractsDTO = settingsContractsMapper.toDto(settingsContracts);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSettingsContractsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(settingsContractsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SettingsContracts in the database
        List<SettingsContracts> settingsContractsList = settingsContractsRepository.findAll();
        assertThat(settingsContractsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSettingsContracts() throws Exception {
        int databaseSizeBeforeUpdate = settingsContractsRepository.findAll().size();
        settingsContracts.setId(count.incrementAndGet());

        // Create the SettingsContracts
        SettingsContractsDTO settingsContractsDTO = settingsContractsMapper.toDto(settingsContracts);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSettingsContractsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(settingsContractsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SettingsContracts in the database
        List<SettingsContracts> settingsContractsList = settingsContractsRepository.findAll();
        assertThat(settingsContractsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSettingsContracts() throws Exception {
        // Initialize the database
        settingsContractsRepository.saveAndFlush(settingsContracts);

        int databaseSizeBeforeDelete = settingsContractsRepository.findAll().size();

        // Delete the settingsContracts
        restSettingsContractsMockMvc
            .perform(delete(ENTITY_API_URL_ID, settingsContracts.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SettingsContracts> settingsContractsList = settingsContractsRepository.findAll();
        assertThat(settingsContractsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

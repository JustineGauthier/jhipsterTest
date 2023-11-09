package fr.it_akademy.animal.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.it_akademy.animal.IntegrationTest;
import fr.it_akademy.animal.domain.Species;
import fr.it_akademy.animal.repository.SpeciesRepository;
import fr.it_akademy.animal.service.dto.SpeciesDTO;
import fr.it_akademy.animal.service.mapper.SpeciesMapper;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SpeciesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SpeciesResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_SIZE = 1L;
    private static final Long UPDATED_SIZE = 2L;

    private static final String ENTITY_API_URL = "/api/species";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SpeciesRepository speciesRepository;

    @Autowired
    private SpeciesMapper speciesMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSpeciesMockMvc;

    private Species species;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Species createEntity(EntityManager em) {
        Species species = new Species().name(DEFAULT_NAME).size(DEFAULT_SIZE);
        return species;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Species createUpdatedEntity(EntityManager em) {
        Species species = new Species().name(UPDATED_NAME).size(UPDATED_SIZE);
        return species;
    }

    @BeforeEach
    public void initTest() {
        species = createEntity(em);
    }

    @Test
    @Transactional
    void createSpecies() throws Exception {
        int databaseSizeBeforeCreate = speciesRepository.findAll().size();
        // Create the Species
        SpeciesDTO speciesDTO = speciesMapper.toDto(species);
        restSpeciesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(speciesDTO)))
            .andExpect(status().isCreated());

        // Validate the Species in the database
        List<Species> speciesList = speciesRepository.findAll();
        assertThat(speciesList).hasSize(databaseSizeBeforeCreate + 1);
        Species testSpecies = speciesList.get(speciesList.size() - 1);
        assertThat(testSpecies.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSpecies.getSize()).isEqualTo(DEFAULT_SIZE);
    }

    @Test
    @Transactional
    void createSpeciesWithExistingId() throws Exception {
        // Create the Species with an existing ID
        species.setId(1L);
        SpeciesDTO speciesDTO = speciesMapper.toDto(species);

        int databaseSizeBeforeCreate = speciesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSpeciesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(speciesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Species in the database
        List<Species> speciesList = speciesRepository.findAll();
        assertThat(speciesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSpecies() throws Exception {
        // Initialize the database
        speciesRepository.saveAndFlush(species);

        // Get all the speciesList
        restSpeciesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(species.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].size").value(hasItem(DEFAULT_SIZE.intValue())));
    }

    @Test
    @Transactional
    void getSpecies() throws Exception {
        // Initialize the database
        speciesRepository.saveAndFlush(species);

        // Get the species
        restSpeciesMockMvc
            .perform(get(ENTITY_API_URL_ID, species.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(species.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.size").value(DEFAULT_SIZE.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingSpecies() throws Exception {
        // Get the species
        restSpeciesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSpecies() throws Exception {
        // Initialize the database
        speciesRepository.saveAndFlush(species);

        int databaseSizeBeforeUpdate = speciesRepository.findAll().size();

        // Update the species
        Species updatedSpecies = speciesRepository.findById(species.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSpecies are not directly saved in db
        em.detach(updatedSpecies);
        updatedSpecies.name(UPDATED_NAME).size(UPDATED_SIZE);
        SpeciesDTO speciesDTO = speciesMapper.toDto(updatedSpecies);

        restSpeciesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, speciesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(speciesDTO))
            )
            .andExpect(status().isOk());

        // Validate the Species in the database
        List<Species> speciesList = speciesRepository.findAll();
        assertThat(speciesList).hasSize(databaseSizeBeforeUpdate);
        Species testSpecies = speciesList.get(speciesList.size() - 1);
        assertThat(testSpecies.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSpecies.getSize()).isEqualTo(UPDATED_SIZE);
    }

    @Test
    @Transactional
    void putNonExistingSpecies() throws Exception {
        int databaseSizeBeforeUpdate = speciesRepository.findAll().size();
        species.setId(longCount.incrementAndGet());

        // Create the Species
        SpeciesDTO speciesDTO = speciesMapper.toDto(species);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpeciesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, speciesDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(speciesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Species in the database
        List<Species> speciesList = speciesRepository.findAll();
        assertThat(speciesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSpecies() throws Exception {
        int databaseSizeBeforeUpdate = speciesRepository.findAll().size();
        species.setId(longCount.incrementAndGet());

        // Create the Species
        SpeciesDTO speciesDTO = speciesMapper.toDto(species);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpeciesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(speciesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Species in the database
        List<Species> speciesList = speciesRepository.findAll();
        assertThat(speciesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSpecies() throws Exception {
        int databaseSizeBeforeUpdate = speciesRepository.findAll().size();
        species.setId(longCount.incrementAndGet());

        // Create the Species
        SpeciesDTO speciesDTO = speciesMapper.toDto(species);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpeciesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(speciesDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Species in the database
        List<Species> speciesList = speciesRepository.findAll();
        assertThat(speciesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSpeciesWithPatch() throws Exception {
        // Initialize the database
        speciesRepository.saveAndFlush(species);

        int databaseSizeBeforeUpdate = speciesRepository.findAll().size();

        // Update the species using partial update
        Species partialUpdatedSpecies = new Species();
        partialUpdatedSpecies.setId(species.getId());

        partialUpdatedSpecies.name(UPDATED_NAME);

        restSpeciesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpecies.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSpecies))
            )
            .andExpect(status().isOk());

        // Validate the Species in the database
        List<Species> speciesList = speciesRepository.findAll();
        assertThat(speciesList).hasSize(databaseSizeBeforeUpdate);
        Species testSpecies = speciesList.get(speciesList.size() - 1);
        assertThat(testSpecies.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSpecies.getSize()).isEqualTo(DEFAULT_SIZE);
    }

    @Test
    @Transactional
    void fullUpdateSpeciesWithPatch() throws Exception {
        // Initialize the database
        speciesRepository.saveAndFlush(species);

        int databaseSizeBeforeUpdate = speciesRepository.findAll().size();

        // Update the species using partial update
        Species partialUpdatedSpecies = new Species();
        partialUpdatedSpecies.setId(species.getId());

        partialUpdatedSpecies.name(UPDATED_NAME).size(UPDATED_SIZE);

        restSpeciesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpecies.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSpecies))
            )
            .andExpect(status().isOk());

        // Validate the Species in the database
        List<Species> speciesList = speciesRepository.findAll();
        assertThat(speciesList).hasSize(databaseSizeBeforeUpdate);
        Species testSpecies = speciesList.get(speciesList.size() - 1);
        assertThat(testSpecies.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSpecies.getSize()).isEqualTo(UPDATED_SIZE);
    }

    @Test
    @Transactional
    void patchNonExistingSpecies() throws Exception {
        int databaseSizeBeforeUpdate = speciesRepository.findAll().size();
        species.setId(longCount.incrementAndGet());

        // Create the Species
        SpeciesDTO speciesDTO = speciesMapper.toDto(species);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpeciesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, speciesDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(speciesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Species in the database
        List<Species> speciesList = speciesRepository.findAll();
        assertThat(speciesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSpecies() throws Exception {
        int databaseSizeBeforeUpdate = speciesRepository.findAll().size();
        species.setId(longCount.incrementAndGet());

        // Create the Species
        SpeciesDTO speciesDTO = speciesMapper.toDto(species);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpeciesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(speciesDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Species in the database
        List<Species> speciesList = speciesRepository.findAll();
        assertThat(speciesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSpecies() throws Exception {
        int databaseSizeBeforeUpdate = speciesRepository.findAll().size();
        species.setId(longCount.incrementAndGet());

        // Create the Species
        SpeciesDTO speciesDTO = speciesMapper.toDto(species);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpeciesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(speciesDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Species in the database
        List<Species> speciesList = speciesRepository.findAll();
        assertThat(speciesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSpecies() throws Exception {
        // Initialize the database
        speciesRepository.saveAndFlush(species);

        int databaseSizeBeforeDelete = speciesRepository.findAll().size();

        // Delete the species
        restSpeciesMockMvc
            .perform(delete(ENTITY_API_URL_ID, species.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Species> speciesList = speciesRepository.findAll();
        assertThat(speciesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

package fr.it_akademy.animal.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.it_akademy.animal.IntegrationTest;
import fr.it_akademy.animal.domain.TypeOfHabitat;
import fr.it_akademy.animal.repository.TypeOfHabitatRepository;
import fr.it_akademy.animal.service.TypeOfHabitatService;
import fr.it_akademy.animal.service.dto.TypeOfHabitatDTO;
import fr.it_akademy.animal.service.mapper.TypeOfHabitatMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TypeOfHabitatResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TypeOfHabitatResourceIT {

    private static final String DEFAULT_CATEGORIE = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORIE = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final String DEFAULT_GROUND = "AAAAAAAAAA";
    private static final String UPDATED_GROUND = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/type-of-habitats";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TypeOfHabitatRepository typeOfHabitatRepository;

    @Mock
    private TypeOfHabitatRepository typeOfHabitatRepositoryMock;

    @Autowired
    private TypeOfHabitatMapper typeOfHabitatMapper;

    @Mock
    private TypeOfHabitatService typeOfHabitatServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTypeOfHabitatMockMvc;

    private TypeOfHabitat typeOfHabitat;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeOfHabitat createEntity(EntityManager em) {
        TypeOfHabitat typeOfHabitat = new TypeOfHabitat().categorie(DEFAULT_CATEGORIE).location(DEFAULT_LOCATION).ground(DEFAULT_GROUND);
        return typeOfHabitat;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeOfHabitat createUpdatedEntity(EntityManager em) {
        TypeOfHabitat typeOfHabitat = new TypeOfHabitat().categorie(UPDATED_CATEGORIE).location(UPDATED_LOCATION).ground(UPDATED_GROUND);
        return typeOfHabitat;
    }

    @BeforeEach
    public void initTest() {
        typeOfHabitat = createEntity(em);
    }

    @Test
    @Transactional
    void createTypeOfHabitat() throws Exception {
        int databaseSizeBeforeCreate = typeOfHabitatRepository.findAll().size();
        // Create the TypeOfHabitat
        TypeOfHabitatDTO typeOfHabitatDTO = typeOfHabitatMapper.toDto(typeOfHabitat);
        restTypeOfHabitatMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typeOfHabitatDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TypeOfHabitat in the database
        List<TypeOfHabitat> typeOfHabitatList = typeOfHabitatRepository.findAll();
        assertThat(typeOfHabitatList).hasSize(databaseSizeBeforeCreate + 1);
        TypeOfHabitat testTypeOfHabitat = typeOfHabitatList.get(typeOfHabitatList.size() - 1);
        assertThat(testTypeOfHabitat.getCategorie()).isEqualTo(DEFAULT_CATEGORIE);
        assertThat(testTypeOfHabitat.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testTypeOfHabitat.getGround()).isEqualTo(DEFAULT_GROUND);
    }

    @Test
    @Transactional
    void createTypeOfHabitatWithExistingId() throws Exception {
        // Create the TypeOfHabitat with an existing ID
        typeOfHabitat.setId(1L);
        TypeOfHabitatDTO typeOfHabitatDTO = typeOfHabitatMapper.toDto(typeOfHabitat);

        int databaseSizeBeforeCreate = typeOfHabitatRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypeOfHabitatMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typeOfHabitatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeOfHabitat in the database
        List<TypeOfHabitat> typeOfHabitatList = typeOfHabitatRepository.findAll();
        assertThat(typeOfHabitatList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTypeOfHabitats() throws Exception {
        // Initialize the database
        typeOfHabitatRepository.saveAndFlush(typeOfHabitat);

        // Get all the typeOfHabitatList
        restTypeOfHabitatMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeOfHabitat.getId().intValue())))
            .andExpect(jsonPath("$.[*].categorie").value(hasItem(DEFAULT_CATEGORIE)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].ground").value(hasItem(DEFAULT_GROUND)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTypeOfHabitatsWithEagerRelationshipsIsEnabled() throws Exception {
        when(typeOfHabitatServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTypeOfHabitatMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(typeOfHabitatServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTypeOfHabitatsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(typeOfHabitatServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTypeOfHabitatMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(typeOfHabitatRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getTypeOfHabitat() throws Exception {
        // Initialize the database
        typeOfHabitatRepository.saveAndFlush(typeOfHabitat);

        // Get the typeOfHabitat
        restTypeOfHabitatMockMvc
            .perform(get(ENTITY_API_URL_ID, typeOfHabitat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(typeOfHabitat.getId().intValue()))
            .andExpect(jsonPath("$.categorie").value(DEFAULT_CATEGORIE))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
            .andExpect(jsonPath("$.ground").value(DEFAULT_GROUND));
    }

    @Test
    @Transactional
    void getNonExistingTypeOfHabitat() throws Exception {
        // Get the typeOfHabitat
        restTypeOfHabitatMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTypeOfHabitat() throws Exception {
        // Initialize the database
        typeOfHabitatRepository.saveAndFlush(typeOfHabitat);

        int databaseSizeBeforeUpdate = typeOfHabitatRepository.findAll().size();

        // Update the typeOfHabitat
        TypeOfHabitat updatedTypeOfHabitat = typeOfHabitatRepository.findById(typeOfHabitat.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTypeOfHabitat are not directly saved in db
        em.detach(updatedTypeOfHabitat);
        updatedTypeOfHabitat.categorie(UPDATED_CATEGORIE).location(UPDATED_LOCATION).ground(UPDATED_GROUND);
        TypeOfHabitatDTO typeOfHabitatDTO = typeOfHabitatMapper.toDto(updatedTypeOfHabitat);

        restTypeOfHabitatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typeOfHabitatDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeOfHabitatDTO))
            )
            .andExpect(status().isOk());

        // Validate the TypeOfHabitat in the database
        List<TypeOfHabitat> typeOfHabitatList = typeOfHabitatRepository.findAll();
        assertThat(typeOfHabitatList).hasSize(databaseSizeBeforeUpdate);
        TypeOfHabitat testTypeOfHabitat = typeOfHabitatList.get(typeOfHabitatList.size() - 1);
        assertThat(testTypeOfHabitat.getCategorie()).isEqualTo(UPDATED_CATEGORIE);
        assertThat(testTypeOfHabitat.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testTypeOfHabitat.getGround()).isEqualTo(UPDATED_GROUND);
    }

    @Test
    @Transactional
    void putNonExistingTypeOfHabitat() throws Exception {
        int databaseSizeBeforeUpdate = typeOfHabitatRepository.findAll().size();
        typeOfHabitat.setId(longCount.incrementAndGet());

        // Create the TypeOfHabitat
        TypeOfHabitatDTO typeOfHabitatDTO = typeOfHabitatMapper.toDto(typeOfHabitat);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeOfHabitatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typeOfHabitatDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeOfHabitatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeOfHabitat in the database
        List<TypeOfHabitat> typeOfHabitatList = typeOfHabitatRepository.findAll();
        assertThat(typeOfHabitatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTypeOfHabitat() throws Exception {
        int databaseSizeBeforeUpdate = typeOfHabitatRepository.findAll().size();
        typeOfHabitat.setId(longCount.incrementAndGet());

        // Create the TypeOfHabitat
        TypeOfHabitatDTO typeOfHabitatDTO = typeOfHabitatMapper.toDto(typeOfHabitat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeOfHabitatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeOfHabitatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeOfHabitat in the database
        List<TypeOfHabitat> typeOfHabitatList = typeOfHabitatRepository.findAll();
        assertThat(typeOfHabitatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTypeOfHabitat() throws Exception {
        int databaseSizeBeforeUpdate = typeOfHabitatRepository.findAll().size();
        typeOfHabitat.setId(longCount.incrementAndGet());

        // Create the TypeOfHabitat
        TypeOfHabitatDTO typeOfHabitatDTO = typeOfHabitatMapper.toDto(typeOfHabitat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeOfHabitatMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typeOfHabitatDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeOfHabitat in the database
        List<TypeOfHabitat> typeOfHabitatList = typeOfHabitatRepository.findAll();
        assertThat(typeOfHabitatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTypeOfHabitatWithPatch() throws Exception {
        // Initialize the database
        typeOfHabitatRepository.saveAndFlush(typeOfHabitat);

        int databaseSizeBeforeUpdate = typeOfHabitatRepository.findAll().size();

        // Update the typeOfHabitat using partial update
        TypeOfHabitat partialUpdatedTypeOfHabitat = new TypeOfHabitat();
        partialUpdatedTypeOfHabitat.setId(typeOfHabitat.getId());

        partialUpdatedTypeOfHabitat.categorie(UPDATED_CATEGORIE).location(UPDATED_LOCATION).ground(UPDATED_GROUND);

        restTypeOfHabitatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeOfHabitat.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypeOfHabitat))
            )
            .andExpect(status().isOk());

        // Validate the TypeOfHabitat in the database
        List<TypeOfHabitat> typeOfHabitatList = typeOfHabitatRepository.findAll();
        assertThat(typeOfHabitatList).hasSize(databaseSizeBeforeUpdate);
        TypeOfHabitat testTypeOfHabitat = typeOfHabitatList.get(typeOfHabitatList.size() - 1);
        assertThat(testTypeOfHabitat.getCategorie()).isEqualTo(UPDATED_CATEGORIE);
        assertThat(testTypeOfHabitat.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testTypeOfHabitat.getGround()).isEqualTo(UPDATED_GROUND);
    }

    @Test
    @Transactional
    void fullUpdateTypeOfHabitatWithPatch() throws Exception {
        // Initialize the database
        typeOfHabitatRepository.saveAndFlush(typeOfHabitat);

        int databaseSizeBeforeUpdate = typeOfHabitatRepository.findAll().size();

        // Update the typeOfHabitat using partial update
        TypeOfHabitat partialUpdatedTypeOfHabitat = new TypeOfHabitat();
        partialUpdatedTypeOfHabitat.setId(typeOfHabitat.getId());

        partialUpdatedTypeOfHabitat.categorie(UPDATED_CATEGORIE).location(UPDATED_LOCATION).ground(UPDATED_GROUND);

        restTypeOfHabitatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeOfHabitat.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypeOfHabitat))
            )
            .andExpect(status().isOk());

        // Validate the TypeOfHabitat in the database
        List<TypeOfHabitat> typeOfHabitatList = typeOfHabitatRepository.findAll();
        assertThat(typeOfHabitatList).hasSize(databaseSizeBeforeUpdate);
        TypeOfHabitat testTypeOfHabitat = typeOfHabitatList.get(typeOfHabitatList.size() - 1);
        assertThat(testTypeOfHabitat.getCategorie()).isEqualTo(UPDATED_CATEGORIE);
        assertThat(testTypeOfHabitat.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testTypeOfHabitat.getGround()).isEqualTo(UPDATED_GROUND);
    }

    @Test
    @Transactional
    void patchNonExistingTypeOfHabitat() throws Exception {
        int databaseSizeBeforeUpdate = typeOfHabitatRepository.findAll().size();
        typeOfHabitat.setId(longCount.incrementAndGet());

        // Create the TypeOfHabitat
        TypeOfHabitatDTO typeOfHabitatDTO = typeOfHabitatMapper.toDto(typeOfHabitat);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeOfHabitatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, typeOfHabitatDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeOfHabitatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeOfHabitat in the database
        List<TypeOfHabitat> typeOfHabitatList = typeOfHabitatRepository.findAll();
        assertThat(typeOfHabitatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTypeOfHabitat() throws Exception {
        int databaseSizeBeforeUpdate = typeOfHabitatRepository.findAll().size();
        typeOfHabitat.setId(longCount.incrementAndGet());

        // Create the TypeOfHabitat
        TypeOfHabitatDTO typeOfHabitatDTO = typeOfHabitatMapper.toDto(typeOfHabitat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeOfHabitatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeOfHabitatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeOfHabitat in the database
        List<TypeOfHabitat> typeOfHabitatList = typeOfHabitatRepository.findAll();
        assertThat(typeOfHabitatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTypeOfHabitat() throws Exception {
        int databaseSizeBeforeUpdate = typeOfHabitatRepository.findAll().size();
        typeOfHabitat.setId(longCount.incrementAndGet());

        // Create the TypeOfHabitat
        TypeOfHabitatDTO typeOfHabitatDTO = typeOfHabitatMapper.toDto(typeOfHabitat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeOfHabitatMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeOfHabitatDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeOfHabitat in the database
        List<TypeOfHabitat> typeOfHabitatList = typeOfHabitatRepository.findAll();
        assertThat(typeOfHabitatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTypeOfHabitat() throws Exception {
        // Initialize the database
        typeOfHabitatRepository.saveAndFlush(typeOfHabitat);

        int databaseSizeBeforeDelete = typeOfHabitatRepository.findAll().size();

        // Delete the typeOfHabitat
        restTypeOfHabitatMockMvc
            .perform(delete(ENTITY_API_URL_ID, typeOfHabitat.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TypeOfHabitat> typeOfHabitatList = typeOfHabitatRepository.findAll();
        assertThat(typeOfHabitatList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

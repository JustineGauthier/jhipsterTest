package fr.it_akademy.animal.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.it_akademy.animal.IntegrationTest;
import fr.it_akademy.animal.domain.Animal;
import fr.it_akademy.animal.repository.AnimalRepository;
import fr.it_akademy.animal.service.dto.AnimalDTO;
import fr.it_akademy.animal.service.mapper.AnimalMapper;
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
 * Integration tests for the {@link AnimalResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AnimalResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_AGE = 1L;
    private static final Long UPDATED_AGE = 2L;

    private static final String DEFAULT_CHARACTER = "AAAAAAAAAA";
    private static final String UPDATED_CHARACTER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/animals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private AnimalMapper animalMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAnimalMockMvc;

    private Animal animal;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Animal createEntity(EntityManager em) {
        Animal animal = new Animal().name(DEFAULT_NAME).age(DEFAULT_AGE).character(DEFAULT_CHARACTER);
        return animal;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Animal createUpdatedEntity(EntityManager em) {
        Animal animal = new Animal().name(UPDATED_NAME).age(UPDATED_AGE).character(UPDATED_CHARACTER);
        return animal;
    }

    @BeforeEach
    public void initTest() {
        animal = createEntity(em);
    }

    @Test
    @Transactional
    void createAnimal() throws Exception {
        int databaseSizeBeforeCreate = animalRepository.findAll().size();
        // Create the Animal
        AnimalDTO animalDTO = animalMapper.toDto(animal);
        restAnimalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(animalDTO)))
            .andExpect(status().isCreated());

        // Validate the Animal in the database
        List<Animal> animalList = animalRepository.findAll();
        assertThat(animalList).hasSize(databaseSizeBeforeCreate + 1);
        Animal testAnimal = animalList.get(animalList.size() - 1);
        assertThat(testAnimal.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAnimal.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testAnimal.getCharacter()).isEqualTo(DEFAULT_CHARACTER);
    }

    @Test
    @Transactional
    void createAnimalWithExistingId() throws Exception {
        // Create the Animal with an existing ID
        animal.setId(1L);
        AnimalDTO animalDTO = animalMapper.toDto(animal);

        int databaseSizeBeforeCreate = animalRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAnimalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(animalDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Animal in the database
        List<Animal> animalList = animalRepository.findAll();
        assertThat(animalList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAnimals() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);

        // Get all the animalList
        restAnimalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(animal.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE.intValue())))
            .andExpect(jsonPath("$.[*].character").value(hasItem(DEFAULT_CHARACTER)));
    }

    @Test
    @Transactional
    void getAnimal() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);

        // Get the animal
        restAnimalMockMvc
            .perform(get(ENTITY_API_URL_ID, animal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(animal.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.age").value(DEFAULT_AGE.intValue()))
            .andExpect(jsonPath("$.character").value(DEFAULT_CHARACTER));
    }

    @Test
    @Transactional
    void getNonExistingAnimal() throws Exception {
        // Get the animal
        restAnimalMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAnimal() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);

        int databaseSizeBeforeUpdate = animalRepository.findAll().size();

        // Update the animal
        Animal updatedAnimal = animalRepository.findById(animal.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAnimal are not directly saved in db
        em.detach(updatedAnimal);
        updatedAnimal.name(UPDATED_NAME).age(UPDATED_AGE).character(UPDATED_CHARACTER);
        AnimalDTO animalDTO = animalMapper.toDto(updatedAnimal);

        restAnimalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, animalDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(animalDTO))
            )
            .andExpect(status().isOk());

        // Validate the Animal in the database
        List<Animal> animalList = animalRepository.findAll();
        assertThat(animalList).hasSize(databaseSizeBeforeUpdate);
        Animal testAnimal = animalList.get(animalList.size() - 1);
        assertThat(testAnimal.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAnimal.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testAnimal.getCharacter()).isEqualTo(UPDATED_CHARACTER);
    }

    @Test
    @Transactional
    void putNonExistingAnimal() throws Exception {
        int databaseSizeBeforeUpdate = animalRepository.findAll().size();
        animal.setId(longCount.incrementAndGet());

        // Create the Animal
        AnimalDTO animalDTO = animalMapper.toDto(animal);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnimalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, animalDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(animalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Animal in the database
        List<Animal> animalList = animalRepository.findAll();
        assertThat(animalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAnimal() throws Exception {
        int databaseSizeBeforeUpdate = animalRepository.findAll().size();
        animal.setId(longCount.incrementAndGet());

        // Create the Animal
        AnimalDTO animalDTO = animalMapper.toDto(animal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnimalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(animalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Animal in the database
        List<Animal> animalList = animalRepository.findAll();
        assertThat(animalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAnimal() throws Exception {
        int databaseSizeBeforeUpdate = animalRepository.findAll().size();
        animal.setId(longCount.incrementAndGet());

        // Create the Animal
        AnimalDTO animalDTO = animalMapper.toDto(animal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnimalMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(animalDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Animal in the database
        List<Animal> animalList = animalRepository.findAll();
        assertThat(animalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAnimalWithPatch() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);

        int databaseSizeBeforeUpdate = animalRepository.findAll().size();

        // Update the animal using partial update
        Animal partialUpdatedAnimal = new Animal();
        partialUpdatedAnimal.setId(animal.getId());

        restAnimalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAnimal.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAnimal))
            )
            .andExpect(status().isOk());

        // Validate the Animal in the database
        List<Animal> animalList = animalRepository.findAll();
        assertThat(animalList).hasSize(databaseSizeBeforeUpdate);
        Animal testAnimal = animalList.get(animalList.size() - 1);
        assertThat(testAnimal.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAnimal.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testAnimal.getCharacter()).isEqualTo(DEFAULT_CHARACTER);
    }

    @Test
    @Transactional
    void fullUpdateAnimalWithPatch() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);

        int databaseSizeBeforeUpdate = animalRepository.findAll().size();

        // Update the animal using partial update
        Animal partialUpdatedAnimal = new Animal();
        partialUpdatedAnimal.setId(animal.getId());

        partialUpdatedAnimal.name(UPDATED_NAME).age(UPDATED_AGE).character(UPDATED_CHARACTER);

        restAnimalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAnimal.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAnimal))
            )
            .andExpect(status().isOk());

        // Validate the Animal in the database
        List<Animal> animalList = animalRepository.findAll();
        assertThat(animalList).hasSize(databaseSizeBeforeUpdate);
        Animal testAnimal = animalList.get(animalList.size() - 1);
        assertThat(testAnimal.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAnimal.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testAnimal.getCharacter()).isEqualTo(UPDATED_CHARACTER);
    }

    @Test
    @Transactional
    void patchNonExistingAnimal() throws Exception {
        int databaseSizeBeforeUpdate = animalRepository.findAll().size();
        animal.setId(longCount.incrementAndGet());

        // Create the Animal
        AnimalDTO animalDTO = animalMapper.toDto(animal);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnimalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, animalDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(animalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Animal in the database
        List<Animal> animalList = animalRepository.findAll();
        assertThat(animalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAnimal() throws Exception {
        int databaseSizeBeforeUpdate = animalRepository.findAll().size();
        animal.setId(longCount.incrementAndGet());

        // Create the Animal
        AnimalDTO animalDTO = animalMapper.toDto(animal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnimalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(animalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Animal in the database
        List<Animal> animalList = animalRepository.findAll();
        assertThat(animalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAnimal() throws Exception {
        int databaseSizeBeforeUpdate = animalRepository.findAll().size();
        animal.setId(longCount.incrementAndGet());

        // Create the Animal
        AnimalDTO animalDTO = animalMapper.toDto(animal);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnimalMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(animalDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Animal in the database
        List<Animal> animalList = animalRepository.findAll();
        assertThat(animalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAnimal() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);

        int databaseSizeBeforeDelete = animalRepository.findAll().size();

        // Delete the animal
        restAnimalMockMvc
            .perform(delete(ENTITY_API_URL_ID, animal.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Animal> animalList = animalRepository.findAll();
        assertThat(animalList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

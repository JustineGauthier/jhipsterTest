package fr.it_akademy.animal.service;

import fr.it_akademy.animal.service.dto.AnimalDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link fr.it_akademy.animal.domain.Animal}.
 */
public interface AnimalService {
    /**
     * Save a animal.
     *
     * @param animalDTO the entity to save.
     * @return the persisted entity.
     */
    AnimalDTO save(AnimalDTO animalDTO);

    /**
     * Updates a animal.
     *
     * @param animalDTO the entity to update.
     * @return the persisted entity.
     */
    AnimalDTO update(AnimalDTO animalDTO);

    /**
     * Partially updates a animal.
     *
     * @param animalDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AnimalDTO> partialUpdate(AnimalDTO animalDTO);

    /**
     * Get all the animals.
     *
     * @return the list of entities.
     */
    List<AnimalDTO> findAll();

    /**
     * Get the "id" animal.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AnimalDTO> findOne(Long id);

    /**
     * Delete the "id" animal.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

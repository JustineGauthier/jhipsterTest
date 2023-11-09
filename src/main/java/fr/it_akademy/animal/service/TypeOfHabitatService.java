package fr.it_akademy.animal.service;

import fr.it_akademy.animal.service.dto.TypeOfHabitatDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.it_akademy.animal.domain.TypeOfHabitat}.
 */
public interface TypeOfHabitatService {
    /**
     * Save a typeOfHabitat.
     *
     * @param typeOfHabitatDTO the entity to save.
     * @return the persisted entity.
     */
    TypeOfHabitatDTO save(TypeOfHabitatDTO typeOfHabitatDTO);

    /**
     * Updates a typeOfHabitat.
     *
     * @param typeOfHabitatDTO the entity to update.
     * @return the persisted entity.
     */
    TypeOfHabitatDTO update(TypeOfHabitatDTO typeOfHabitatDTO);

    /**
     * Partially updates a typeOfHabitat.
     *
     * @param typeOfHabitatDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TypeOfHabitatDTO> partialUpdate(TypeOfHabitatDTO typeOfHabitatDTO);

    /**
     * Get all the typeOfHabitats.
     *
     * @return the list of entities.
     */
    List<TypeOfHabitatDTO> findAll();

    /**
     * Get all the typeOfHabitats with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TypeOfHabitatDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" typeOfHabitat.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TypeOfHabitatDTO> findOne(Long id);

    /**
     * Delete the "id" typeOfHabitat.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

package fr.it_akademy.animal.service;

import fr.it_akademy.animal.service.dto.SpeciesDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link fr.it_akademy.animal.domain.Species}.
 */
public interface SpeciesService {
    /**
     * Save a species.
     *
     * @param speciesDTO the entity to save.
     * @return the persisted entity.
     */
    SpeciesDTO save(SpeciesDTO speciesDTO);

    /**
     * Updates a species.
     *
     * @param speciesDTO the entity to update.
     * @return the persisted entity.
     */
    SpeciesDTO update(SpeciesDTO speciesDTO);

    /**
     * Partially updates a species.
     *
     * @param speciesDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SpeciesDTO> partialUpdate(SpeciesDTO speciesDTO);

    /**
     * Get all the species.
     *
     * @return the list of entities.
     */
    List<SpeciesDTO> findAll();

    /**
     * Get the "id" species.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SpeciesDTO> findOne(Long id);

    /**
     * Delete the "id" species.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

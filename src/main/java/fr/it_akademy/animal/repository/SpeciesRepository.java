package fr.it_akademy.animal.repository;

import fr.it_akademy.animal.domain.Species;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Species entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SpeciesRepository extends JpaRepository<Species, Long> {}

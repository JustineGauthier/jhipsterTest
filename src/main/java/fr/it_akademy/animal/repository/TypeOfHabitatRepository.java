package fr.it_akademy.animal.repository;

import fr.it_akademy.animal.domain.TypeOfHabitat;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TypeOfHabitat entity.
 *
 * When extending this class, extend TypeOfHabitatRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface TypeOfHabitatRepository extends TypeOfHabitatRepositoryWithBagRelationships, JpaRepository<TypeOfHabitat, Long> {
    default Optional<TypeOfHabitat> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<TypeOfHabitat> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<TypeOfHabitat> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}

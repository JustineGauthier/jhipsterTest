package fr.it_akademy.animal.repository;

import fr.it_akademy.animal.domain.TypeOfHabitat;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface TypeOfHabitatRepositoryWithBagRelationships {
    Optional<TypeOfHabitat> fetchBagRelationships(Optional<TypeOfHabitat> typeOfHabitat);

    List<TypeOfHabitat> fetchBagRelationships(List<TypeOfHabitat> typeOfHabitats);

    Page<TypeOfHabitat> fetchBagRelationships(Page<TypeOfHabitat> typeOfHabitats);
}

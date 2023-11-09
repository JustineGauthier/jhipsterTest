package fr.it_akademy.animal.repository;

import fr.it_akademy.animal.domain.TypeOfHabitat;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class TypeOfHabitatRepositoryWithBagRelationshipsImpl implements TypeOfHabitatRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<TypeOfHabitat> fetchBagRelationships(Optional<TypeOfHabitat> typeOfHabitat) {
        return typeOfHabitat.map(this::fetchAnimals);
    }

    @Override
    public Page<TypeOfHabitat> fetchBagRelationships(Page<TypeOfHabitat> typeOfHabitats) {
        return new PageImpl<>(
            fetchBagRelationships(typeOfHabitats.getContent()),
            typeOfHabitats.getPageable(),
            typeOfHabitats.getTotalElements()
        );
    }

    @Override
    public List<TypeOfHabitat> fetchBagRelationships(List<TypeOfHabitat> typeOfHabitats) {
        return Optional.of(typeOfHabitats).map(this::fetchAnimals).orElse(Collections.emptyList());
    }

    TypeOfHabitat fetchAnimals(TypeOfHabitat result) {
        return entityManager
            .createQuery(
                "select typeOfHabitat from TypeOfHabitat typeOfHabitat left join fetch typeOfHabitat.animals where typeOfHabitat.id = :id",
                TypeOfHabitat.class
            )
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<TypeOfHabitat> fetchAnimals(List<TypeOfHabitat> typeOfHabitats) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, typeOfHabitats.size()).forEach(index -> order.put(typeOfHabitats.get(index).getId(), index));
        List<TypeOfHabitat> result = entityManager
            .createQuery(
                "select typeOfHabitat from TypeOfHabitat typeOfHabitat left join fetch typeOfHabitat.animals where typeOfHabitat in :typeOfHabitats",
                TypeOfHabitat.class
            )
            .setParameter("typeOfHabitats", typeOfHabitats)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}

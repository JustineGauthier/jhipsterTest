package fr.it_akademy.animal.repository;

import fr.it_akademy.animal.domain.Food;
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
public class FoodRepositoryWithBagRelationshipsImpl implements FoodRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Food> fetchBagRelationships(Optional<Food> food) {
        return food.map(this::fetchAnimals).map(this::fetchTypeOfHabitats);
    }

    @Override
    public Page<Food> fetchBagRelationships(Page<Food> foods) {
        return new PageImpl<>(fetchBagRelationships(foods.getContent()), foods.getPageable(), foods.getTotalElements());
    }

    @Override
    public List<Food> fetchBagRelationships(List<Food> foods) {
        return Optional.of(foods).map(this::fetchAnimals).map(this::fetchTypeOfHabitats).orElse(Collections.emptyList());
    }

    Food fetchAnimals(Food result) {
        return entityManager
            .createQuery("select food from Food food left join fetch food.animals where food.id = :id", Food.class)
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<Food> fetchAnimals(List<Food> foods) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, foods.size()).forEach(index -> order.put(foods.get(index).getId(), index));
        List<Food> result = entityManager
            .createQuery("select food from Food food left join fetch food.animals where food in :foods", Food.class)
            .setParameter("foods", foods)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    Food fetchTypeOfHabitats(Food result) {
        return entityManager
            .createQuery("select food from Food food left join fetch food.typeOfHabitats where food.id = :id", Food.class)
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<Food> fetchTypeOfHabitats(List<Food> foods) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, foods.size()).forEach(index -> order.put(foods.get(index).getId(), index));
        List<Food> result = entityManager
            .createQuery("select food from Food food left join fetch food.typeOfHabitats where food in :foods", Food.class)
            .setParameter("foods", foods)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}

package fr.it_akademy.animal.repository;

import fr.it_akademy.animal.domain.Food;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface FoodRepositoryWithBagRelationships {
    Optional<Food> fetchBagRelationships(Optional<Food> food);

    List<Food> fetchBagRelationships(List<Food> foods);

    Page<Food> fetchBagRelationships(Page<Food> foods);
}

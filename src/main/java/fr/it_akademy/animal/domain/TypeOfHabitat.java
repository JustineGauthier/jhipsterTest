package fr.it_akademy.animal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TypeOfHabitat.
 */
@Entity
@Table(name = "type_of_habitat")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TypeOfHabitat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "categorie")
    private String categorie;

    @Column(name = "location")
    private String location;

    @Column(name = "ground")
    private String ground;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_type_of_habitat__animal",
        joinColumns = @JoinColumn(name = "type_of_habitat_id"),
        inverseJoinColumns = @JoinColumn(name = "animal_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "species", "typeOfHabitats", "foods" }, allowSetters = true)
    private Set<Animal> animals = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "typeOfHabitats")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "animals", "typeOfHabitats" }, allowSetters = true)
    private Set<Food> foods = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TypeOfHabitat id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategorie() {
        return this.categorie;
    }

    public TypeOfHabitat categorie(String categorie) {
        this.setCategorie(categorie);
        return this;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getLocation() {
        return this.location;
    }

    public TypeOfHabitat location(String location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getGround() {
        return this.ground;
    }

    public TypeOfHabitat ground(String ground) {
        this.setGround(ground);
        return this;
    }

    public void setGround(String ground) {
        this.ground = ground;
    }

    public Set<Animal> getAnimals() {
        return this.animals;
    }

    public void setAnimals(Set<Animal> animals) {
        this.animals = animals;
    }

    public TypeOfHabitat animals(Set<Animal> animals) {
        this.setAnimals(animals);
        return this;
    }

    public TypeOfHabitat addAnimal(Animal animal) {
        this.animals.add(animal);
        return this;
    }

    public TypeOfHabitat removeAnimal(Animal animal) {
        this.animals.remove(animal);
        return this;
    }

    public Set<Food> getFoods() {
        return this.foods;
    }

    public void setFoods(Set<Food> foods) {
        if (this.foods != null) {
            this.foods.forEach(i -> i.removeTypeOfHabitat(this));
        }
        if (foods != null) {
            foods.forEach(i -> i.addTypeOfHabitat(this));
        }
        this.foods = foods;
    }

    public TypeOfHabitat foods(Set<Food> foods) {
        this.setFoods(foods);
        return this;
    }

    public TypeOfHabitat addFood(Food food) {
        this.foods.add(food);
        food.getTypeOfHabitats().add(this);
        return this;
    }

    public TypeOfHabitat removeFood(Food food) {
        this.foods.remove(food);
        food.getTypeOfHabitats().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeOfHabitat)) {
            return false;
        }
        return getId() != null && getId().equals(((TypeOfHabitat) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypeOfHabitat{" +
            "id=" + getId() +
            ", categorie='" + getCategorie() + "'" +
            ", location='" + getLocation() + "'" +
            ", ground='" + getGround() + "'" +
            "}";
    }
}

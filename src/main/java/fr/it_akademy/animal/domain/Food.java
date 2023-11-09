package fr.it_akademy.animal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Food.
 */
@Entity
@Table(name = "food")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Food implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "color")
    private String color;

    @Column(name = "peremption_date")
    private Instant peremptionDate;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "rel_food__animal", joinColumns = @JoinColumn(name = "food_id"), inverseJoinColumns = @JoinColumn(name = "animal_id"))
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "species", "typeOfHabitats", "foods" }, allowSetters = true)
    private Set<Animal> animals = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_food__type_of_habitat",
        joinColumns = @JoinColumn(name = "food_id"),
        inverseJoinColumns = @JoinColumn(name = "type_of_habitat_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "animals", "foods" }, allowSetters = true)
    private Set<TypeOfHabitat> typeOfHabitats = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Food id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Food name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return this.color;
    }

    public Food color(String color) {
        this.setColor(color);
        return this;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Instant getPeremptionDate() {
        return this.peremptionDate;
    }

    public Food peremptionDate(Instant peremptionDate) {
        this.setPeremptionDate(peremptionDate);
        return this;
    }

    public void setPeremptionDate(Instant peremptionDate) {
        this.peremptionDate = peremptionDate;
    }

    public Set<Animal> getAnimals() {
        return this.animals;
    }

    public void setAnimals(Set<Animal> animals) {
        this.animals = animals;
    }

    public Food animals(Set<Animal> animals) {
        this.setAnimals(animals);
        return this;
    }

    public Food addAnimal(Animal animal) {
        this.animals.add(animal);
        return this;
    }

    public Food removeAnimal(Animal animal) {
        this.animals.remove(animal);
        return this;
    }

    public Set<TypeOfHabitat> getTypeOfHabitats() {
        return this.typeOfHabitats;
    }

    public void setTypeOfHabitats(Set<TypeOfHabitat> typeOfHabitats) {
        this.typeOfHabitats = typeOfHabitats;
    }

    public Food typeOfHabitats(Set<TypeOfHabitat> typeOfHabitats) {
        this.setTypeOfHabitats(typeOfHabitats);
        return this;
    }

    public Food addTypeOfHabitat(TypeOfHabitat typeOfHabitat) {
        this.typeOfHabitats.add(typeOfHabitat);
        return this;
    }

    public Food removeTypeOfHabitat(TypeOfHabitat typeOfHabitat) {
        this.typeOfHabitats.remove(typeOfHabitat);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Food)) {
            return false;
        }
        return getId() != null && getId().equals(((Food) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Food{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", color='" + getColor() + "'" +
            ", peremptionDate='" + getPeremptionDate() + "'" +
            "}";
    }
}

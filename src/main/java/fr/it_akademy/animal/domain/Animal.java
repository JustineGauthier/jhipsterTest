package fr.it_akademy.animal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Animal.
 */
@Entity
@Table(name = "animal")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Animal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private Long age;

    @Column(name = "character")
    private String character;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "animals" }, allowSetters = true)
    private Species species;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "animals")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "animals" }, allowSetters = true)
    private Set<TypeOfHabitat> typeOfHabitats = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Animal id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Animal name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getAge() {
        return this.age;
    }

    public Animal age(Long age) {
        this.setAge(age);
        return this;
    }

    public void setAge(Long age) {
        this.age = age;
    }

    public String getCharacter() {
        return this.character;
    }

    public Animal character(String character) {
        this.setCharacter(character);
        return this;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public Species getSpecies() {
        return this.species;
    }

    public void setSpecies(Species species) {
        this.species = species;
    }

    public Animal species(Species species) {
        this.setSpecies(species);
        return this;
    }

    public Set<TypeOfHabitat> getTypeOfHabitats() {
        return this.typeOfHabitats;
    }

    public void setTypeOfHabitats(Set<TypeOfHabitat> typeOfHabitats) {
        if (this.typeOfHabitats != null) {
            this.typeOfHabitats.forEach(i -> i.removeAnimal(this));
        }
        if (typeOfHabitats != null) {
            typeOfHabitats.forEach(i -> i.addAnimal(this));
        }
        this.typeOfHabitats = typeOfHabitats;
    }

    public Animal typeOfHabitats(Set<TypeOfHabitat> typeOfHabitats) {
        this.setTypeOfHabitats(typeOfHabitats);
        return this;
    }

    public Animal addTypeOfHabitat(TypeOfHabitat typeOfHabitat) {
        this.typeOfHabitats.add(typeOfHabitat);
        typeOfHabitat.getAnimals().add(this);
        return this;
    }

    public Animal removeTypeOfHabitat(TypeOfHabitat typeOfHabitat) {
        this.typeOfHabitats.remove(typeOfHabitat);
        typeOfHabitat.getAnimals().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Animal)) {
            return false;
        }
        return getId() != null && getId().equals(((Animal) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Animal{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", age=" + getAge() +
            ", character='" + getCharacter() + "'" +
            "}";
    }
}

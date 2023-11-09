package fr.it_akademy.animal.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link fr.it_akademy.animal.domain.Animal} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AnimalDTO implements Serializable {

    private Long id;

    private String name;

    private Long age;

    private String character;

    private SpeciesDTO species;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getAge() {
        return age;
    }

    public void setAge(Long age) {
        this.age = age;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public SpeciesDTO getSpecies() {
        return species;
    }

    public void setSpecies(SpeciesDTO species) {
        this.species = species;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AnimalDTO)) {
            return false;
        }

        AnimalDTO animalDTO = (AnimalDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, animalDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AnimalDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", age=" + getAge() +
            ", character='" + getCharacter() + "'" +
            ", species=" + getSpecies() +
            "}";
    }
}

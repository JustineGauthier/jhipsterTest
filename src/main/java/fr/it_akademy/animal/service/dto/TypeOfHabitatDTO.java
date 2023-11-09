package fr.it_akademy.animal.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link fr.it_akademy.animal.domain.TypeOfHabitat} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TypeOfHabitatDTO implements Serializable {

    private Long id;

    private String categorie;

    private String location;

    private String ground;

    private Set<AnimalDTO> animals = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getGround() {
        return ground;
    }

    public void setGround(String ground) {
        this.ground = ground;
    }

    public Set<AnimalDTO> getAnimals() {
        return animals;
    }

    public void setAnimals(Set<AnimalDTO> animals) {
        this.animals = animals;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeOfHabitatDTO)) {
            return false;
        }

        TypeOfHabitatDTO typeOfHabitatDTO = (TypeOfHabitatDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, typeOfHabitatDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypeOfHabitatDTO{" +
            "id=" + getId() +
            ", categorie='" + getCategorie() + "'" +
            ", location='" + getLocation() + "'" +
            ", ground='" + getGround() + "'" +
            ", animals=" + getAnimals() +
            "}";
    }
}

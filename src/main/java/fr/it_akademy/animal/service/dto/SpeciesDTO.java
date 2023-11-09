package fr.it_akademy.animal.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link fr.it_akademy.animal.domain.Species} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SpeciesDTO implements Serializable {

    private Long id;

    private String name;

    private Long size;

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

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SpeciesDTO)) {
            return false;
        }

        SpeciesDTO speciesDTO = (SpeciesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, speciesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SpeciesDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", size=" + getSize() +
            "}";
    }
}

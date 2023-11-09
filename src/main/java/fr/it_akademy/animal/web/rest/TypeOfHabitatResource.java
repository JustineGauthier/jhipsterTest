package fr.it_akademy.animal.web.rest;

import fr.it_akademy.animal.repository.TypeOfHabitatRepository;
import fr.it_akademy.animal.service.TypeOfHabitatService;
import fr.it_akademy.animal.service.dto.TypeOfHabitatDTO;
import fr.it_akademy.animal.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link fr.it_akademy.animal.domain.TypeOfHabitat}.
 */
@RestController
@RequestMapping("/api/type-of-habitats")
public class TypeOfHabitatResource {

    private final Logger log = LoggerFactory.getLogger(TypeOfHabitatResource.class);

    private static final String ENTITY_NAME = "typeOfHabitat";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TypeOfHabitatService typeOfHabitatService;

    private final TypeOfHabitatRepository typeOfHabitatRepository;

    public TypeOfHabitatResource(TypeOfHabitatService typeOfHabitatService, TypeOfHabitatRepository typeOfHabitatRepository) {
        this.typeOfHabitatService = typeOfHabitatService;
        this.typeOfHabitatRepository = typeOfHabitatRepository;
    }

    /**
     * {@code POST  /type-of-habitats} : Create a new typeOfHabitat.
     *
     * @param typeOfHabitatDTO the typeOfHabitatDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new typeOfHabitatDTO, or with status {@code 400 (Bad Request)} if the typeOfHabitat has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TypeOfHabitatDTO> createTypeOfHabitat(@RequestBody TypeOfHabitatDTO typeOfHabitatDTO) throws URISyntaxException {
        log.debug("REST request to save TypeOfHabitat : {}", typeOfHabitatDTO);
        if (typeOfHabitatDTO.getId() != null) {
            throw new BadRequestAlertException("A new typeOfHabitat cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TypeOfHabitatDTO result = typeOfHabitatService.save(typeOfHabitatDTO);
        return ResponseEntity
            .created(new URI("/api/type-of-habitats/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /type-of-habitats/:id} : Updates an existing typeOfHabitat.
     *
     * @param id the id of the typeOfHabitatDTO to save.
     * @param typeOfHabitatDTO the typeOfHabitatDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeOfHabitatDTO,
     * or with status {@code 400 (Bad Request)} if the typeOfHabitatDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typeOfHabitatDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TypeOfHabitatDTO> updateTypeOfHabitat(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TypeOfHabitatDTO typeOfHabitatDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TypeOfHabitat : {}, {}", id, typeOfHabitatDTO);
        if (typeOfHabitatDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeOfHabitatDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeOfHabitatRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TypeOfHabitatDTO result = typeOfHabitatService.update(typeOfHabitatDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeOfHabitatDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /type-of-habitats/:id} : Partial updates given fields of an existing typeOfHabitat, field will ignore if it is null
     *
     * @param id the id of the typeOfHabitatDTO to save.
     * @param typeOfHabitatDTO the typeOfHabitatDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeOfHabitatDTO,
     * or with status {@code 400 (Bad Request)} if the typeOfHabitatDTO is not valid,
     * or with status {@code 404 (Not Found)} if the typeOfHabitatDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the typeOfHabitatDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TypeOfHabitatDTO> partialUpdateTypeOfHabitat(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TypeOfHabitatDTO typeOfHabitatDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TypeOfHabitat partially : {}, {}", id, typeOfHabitatDTO);
        if (typeOfHabitatDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeOfHabitatDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeOfHabitatRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TypeOfHabitatDTO> result = typeOfHabitatService.partialUpdate(typeOfHabitatDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeOfHabitatDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /type-of-habitats} : get all the typeOfHabitats.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of typeOfHabitats in body.
     */
    @GetMapping("")
    public List<TypeOfHabitatDTO> getAllTypeOfHabitats(@RequestParam(required = false, defaultValue = "true") boolean eagerload) {
        log.debug("REST request to get all TypeOfHabitats");
        return typeOfHabitatService.findAll();
    }

    /**
     * {@code GET  /type-of-habitats/:id} : get the "id" typeOfHabitat.
     *
     * @param id the id of the typeOfHabitatDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the typeOfHabitatDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TypeOfHabitatDTO> getTypeOfHabitat(@PathVariable Long id) {
        log.debug("REST request to get TypeOfHabitat : {}", id);
        Optional<TypeOfHabitatDTO> typeOfHabitatDTO = typeOfHabitatService.findOne(id);
        return ResponseUtil.wrapOrNotFound(typeOfHabitatDTO);
    }

    /**
     * {@code DELETE  /type-of-habitats/:id} : delete the "id" typeOfHabitat.
     *
     * @param id the id of the typeOfHabitatDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTypeOfHabitat(@PathVariable Long id) {
        log.debug("REST request to delete TypeOfHabitat : {}", id);
        typeOfHabitatService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

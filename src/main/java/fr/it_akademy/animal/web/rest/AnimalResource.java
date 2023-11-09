package fr.it_akademy.animal.web.rest;

import fr.it_akademy.animal.repository.AnimalRepository;
import fr.it_akademy.animal.service.AnimalService;
import fr.it_akademy.animal.service.dto.AnimalDTO;
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
 * REST controller for managing {@link fr.it_akademy.animal.domain.Animal}.
 */
@RestController
@RequestMapping("/api/animals")
public class AnimalResource {

    private final Logger log = LoggerFactory.getLogger(AnimalResource.class);

    private static final String ENTITY_NAME = "animal";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AnimalService animalService;

    private final AnimalRepository animalRepository;

    public AnimalResource(AnimalService animalService, AnimalRepository animalRepository) {
        this.animalService = animalService;
        this.animalRepository = animalRepository;
    }

    /**
     * {@code POST  /animals} : Create a new animal.
     *
     * @param animalDTO the animalDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new animalDTO, or with status {@code 400 (Bad Request)} if the animal has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AnimalDTO> createAnimal(@RequestBody AnimalDTO animalDTO) throws URISyntaxException {
        log.debug("REST request to save Animal : {}", animalDTO);
        if (animalDTO.getId() != null) {
            throw new BadRequestAlertException("A new animal cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AnimalDTO result = animalService.save(animalDTO);
        return ResponseEntity
            .created(new URI("/api/animals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /animals/:id} : Updates an existing animal.
     *
     * @param id the id of the animalDTO to save.
     * @param animalDTO the animalDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated animalDTO,
     * or with status {@code 400 (Bad Request)} if the animalDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the animalDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AnimalDTO> updateAnimal(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AnimalDTO animalDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Animal : {}, {}", id, animalDTO);
        if (animalDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, animalDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!animalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AnimalDTO result = animalService.update(animalDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, animalDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /animals/:id} : Partial updates given fields of an existing animal, field will ignore if it is null
     *
     * @param id the id of the animalDTO to save.
     * @param animalDTO the animalDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated animalDTO,
     * or with status {@code 400 (Bad Request)} if the animalDTO is not valid,
     * or with status {@code 404 (Not Found)} if the animalDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the animalDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AnimalDTO> partialUpdateAnimal(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AnimalDTO animalDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Animal partially : {}, {}", id, animalDTO);
        if (animalDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, animalDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!animalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AnimalDTO> result = animalService.partialUpdate(animalDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, animalDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /animals} : get all the animals.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of animals in body.
     */
    @GetMapping("")
    public List<AnimalDTO> getAllAnimals() {
        log.debug("REST request to get all Animals");
        return animalService.findAll();
    }

    /**
     * {@code GET  /animals/:id} : get the "id" animal.
     *
     * @param id the id of the animalDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the animalDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AnimalDTO> getAnimal(@PathVariable Long id) {
        log.debug("REST request to get Animal : {}", id);
        Optional<AnimalDTO> animalDTO = animalService.findOne(id);
        return ResponseUtil.wrapOrNotFound(animalDTO);
    }

    /**
     * {@code DELETE  /animals/:id} : delete the "id" animal.
     *
     * @param id the id of the animalDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnimal(@PathVariable Long id) {
        log.debug("REST request to delete Animal : {}", id);
        animalService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

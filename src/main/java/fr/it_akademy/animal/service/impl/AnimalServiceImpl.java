package fr.it_akademy.animal.service.impl;

import fr.it_akademy.animal.domain.Animal;
import fr.it_akademy.animal.repository.AnimalRepository;
import fr.it_akademy.animal.service.AnimalService;
import fr.it_akademy.animal.service.dto.AnimalDTO;
import fr.it_akademy.animal.service.mapper.AnimalMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.it_akademy.animal.domain.Animal}.
 */
@Service
@Transactional
public class AnimalServiceImpl implements AnimalService {

    private final Logger log = LoggerFactory.getLogger(AnimalServiceImpl.class);

    private final AnimalRepository animalRepository;

    private final AnimalMapper animalMapper;

    public AnimalServiceImpl(AnimalRepository animalRepository, AnimalMapper animalMapper) {
        this.animalRepository = animalRepository;
        this.animalMapper = animalMapper;
    }

    @Override
    public AnimalDTO save(AnimalDTO animalDTO) {
        log.debug("Request to save Animal : {}", animalDTO);
        Animal animal = animalMapper.toEntity(animalDTO);
        animal = animalRepository.save(animal);
        return animalMapper.toDto(animal);
    }

    @Override
    public AnimalDTO update(AnimalDTO animalDTO) {
        log.debug("Request to update Animal : {}", animalDTO);
        Animal animal = animalMapper.toEntity(animalDTO);
        animal = animalRepository.save(animal);
        return animalMapper.toDto(animal);
    }

    @Override
    public Optional<AnimalDTO> partialUpdate(AnimalDTO animalDTO) {
        log.debug("Request to partially update Animal : {}", animalDTO);

        return animalRepository
            .findById(animalDTO.getId())
            .map(existingAnimal -> {
                animalMapper.partialUpdate(existingAnimal, animalDTO);

                return existingAnimal;
            })
            .map(animalRepository::save)
            .map(animalMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AnimalDTO> findAll() {
        log.debug("Request to get all Animals");
        return animalRepository.findAll().stream().map(animalMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AnimalDTO> findOne(Long id) {
        log.debug("Request to get Animal : {}", id);
        return animalRepository.findById(id).map(animalMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Animal : {}", id);
        animalRepository.deleteById(id);
    }
}

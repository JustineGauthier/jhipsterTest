package fr.it_akademy.animal.service.impl;

import fr.it_akademy.animal.domain.TypeOfHabitat;
import fr.it_akademy.animal.repository.TypeOfHabitatRepository;
import fr.it_akademy.animal.service.TypeOfHabitatService;
import fr.it_akademy.animal.service.dto.TypeOfHabitatDTO;
import fr.it_akademy.animal.service.mapper.TypeOfHabitatMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.it_akademy.animal.domain.TypeOfHabitat}.
 */
@Service
@Transactional
public class TypeOfHabitatServiceImpl implements TypeOfHabitatService {

    private final Logger log = LoggerFactory.getLogger(TypeOfHabitatServiceImpl.class);

    private final TypeOfHabitatRepository typeOfHabitatRepository;

    private final TypeOfHabitatMapper typeOfHabitatMapper;

    public TypeOfHabitatServiceImpl(TypeOfHabitatRepository typeOfHabitatRepository, TypeOfHabitatMapper typeOfHabitatMapper) {
        this.typeOfHabitatRepository = typeOfHabitatRepository;
        this.typeOfHabitatMapper = typeOfHabitatMapper;
    }

    @Override
    public TypeOfHabitatDTO save(TypeOfHabitatDTO typeOfHabitatDTO) {
        log.debug("Request to save TypeOfHabitat : {}", typeOfHabitatDTO);
        TypeOfHabitat typeOfHabitat = typeOfHabitatMapper.toEntity(typeOfHabitatDTO);
        typeOfHabitat = typeOfHabitatRepository.save(typeOfHabitat);
        return typeOfHabitatMapper.toDto(typeOfHabitat);
    }

    @Override
    public TypeOfHabitatDTO update(TypeOfHabitatDTO typeOfHabitatDTO) {
        log.debug("Request to update TypeOfHabitat : {}", typeOfHabitatDTO);
        TypeOfHabitat typeOfHabitat = typeOfHabitatMapper.toEntity(typeOfHabitatDTO);
        typeOfHabitat = typeOfHabitatRepository.save(typeOfHabitat);
        return typeOfHabitatMapper.toDto(typeOfHabitat);
    }

    @Override
    public Optional<TypeOfHabitatDTO> partialUpdate(TypeOfHabitatDTO typeOfHabitatDTO) {
        log.debug("Request to partially update TypeOfHabitat : {}", typeOfHabitatDTO);

        return typeOfHabitatRepository
            .findById(typeOfHabitatDTO.getId())
            .map(existingTypeOfHabitat -> {
                typeOfHabitatMapper.partialUpdate(existingTypeOfHabitat, typeOfHabitatDTO);

                return existingTypeOfHabitat;
            })
            .map(typeOfHabitatRepository::save)
            .map(typeOfHabitatMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TypeOfHabitatDTO> findAll() {
        log.debug("Request to get all TypeOfHabitats");
        return typeOfHabitatRepository.findAll().stream().map(typeOfHabitatMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    public Page<TypeOfHabitatDTO> findAllWithEagerRelationships(Pageable pageable) {
        return typeOfHabitatRepository.findAllWithEagerRelationships(pageable).map(typeOfHabitatMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TypeOfHabitatDTO> findOne(Long id) {
        log.debug("Request to get TypeOfHabitat : {}", id);
        return typeOfHabitatRepository.findOneWithEagerRelationships(id).map(typeOfHabitatMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TypeOfHabitat : {}", id);
        typeOfHabitatRepository.deleteById(id);
    }
}

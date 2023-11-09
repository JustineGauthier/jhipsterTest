package fr.it_akademy.animal.service.impl;

import fr.it_akademy.animal.domain.Species;
import fr.it_akademy.animal.repository.SpeciesRepository;
import fr.it_akademy.animal.service.SpeciesService;
import fr.it_akademy.animal.service.dto.SpeciesDTO;
import fr.it_akademy.animal.service.mapper.SpeciesMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.it_akademy.animal.domain.Species}.
 */
@Service
@Transactional
public class SpeciesServiceImpl implements SpeciesService {

    private final Logger log = LoggerFactory.getLogger(SpeciesServiceImpl.class);

    private final SpeciesRepository speciesRepository;

    private final SpeciesMapper speciesMapper;

    public SpeciesServiceImpl(SpeciesRepository speciesRepository, SpeciesMapper speciesMapper) {
        this.speciesRepository = speciesRepository;
        this.speciesMapper = speciesMapper;
    }

    @Override
    public SpeciesDTO save(SpeciesDTO speciesDTO) {
        log.debug("Request to save Species : {}", speciesDTO);
        Species species = speciesMapper.toEntity(speciesDTO);
        species = speciesRepository.save(species);
        return speciesMapper.toDto(species);
    }

    @Override
    public SpeciesDTO update(SpeciesDTO speciesDTO) {
        log.debug("Request to update Species : {}", speciesDTO);
        Species species = speciesMapper.toEntity(speciesDTO);
        species = speciesRepository.save(species);
        return speciesMapper.toDto(species);
    }

    @Override
    public Optional<SpeciesDTO> partialUpdate(SpeciesDTO speciesDTO) {
        log.debug("Request to partially update Species : {}", speciesDTO);

        return speciesRepository
            .findById(speciesDTO.getId())
            .map(existingSpecies -> {
                speciesMapper.partialUpdate(existingSpecies, speciesDTO);

                return existingSpecies;
            })
            .map(speciesRepository::save)
            .map(speciesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SpeciesDTO> findAll() {
        log.debug("Request to get all Species");
        return speciesRepository.findAll().stream().map(speciesMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SpeciesDTO> findOne(Long id) {
        log.debug("Request to get Species : {}", id);
        return speciesRepository.findById(id).map(speciesMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Species : {}", id);
        speciesRepository.deleteById(id);
    }
}

package bracquib.coopcycle.service;

import bracquib.coopcycle.domain.PlateformePaiement;
import bracquib.coopcycle.repository.PlateformePaiementRepository;
import bracquib.coopcycle.service.dto.PlateformePaiementDTO;
import bracquib.coopcycle.service.mapper.PlateformePaiementMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link PlateformePaiement}.
 */
@Service
@Transactional
public class PlateformePaiementService {

    private final Logger log = LoggerFactory.getLogger(PlateformePaiementService.class);

    private final PlateformePaiementRepository plateformePaiementRepository;

    private final PlateformePaiementMapper plateformePaiementMapper;

    public PlateformePaiementService(
        PlateformePaiementRepository plateformePaiementRepository,
        PlateformePaiementMapper plateformePaiementMapper
    ) {
        this.plateformePaiementRepository = plateformePaiementRepository;
        this.plateformePaiementMapper = plateformePaiementMapper;
    }

    /**
     * Save a plateformePaiement.
     *
     * @param plateformePaiementDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<PlateformePaiementDTO> save(PlateformePaiementDTO plateformePaiementDTO) {
        log.debug("Request to save PlateformePaiement : {}", plateformePaiementDTO);
        return plateformePaiementRepository
            .save(plateformePaiementMapper.toEntity(plateformePaiementDTO))
            .map(plateformePaiementMapper::toDto);
    }

    /**
     * Update a plateformePaiement.
     *
     * @param plateformePaiementDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<PlateformePaiementDTO> update(PlateformePaiementDTO plateformePaiementDTO) {
        log.debug("Request to update PlateformePaiement : {}", plateformePaiementDTO);
        return plateformePaiementRepository
            .save(plateformePaiementMapper.toEntity(plateformePaiementDTO))
            .map(plateformePaiementMapper::toDto);
    }

    /**
     * Partially update a plateformePaiement.
     *
     * @param plateformePaiementDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<PlateformePaiementDTO> partialUpdate(PlateformePaiementDTO plateformePaiementDTO) {
        log.debug("Request to partially update PlateformePaiement : {}", plateformePaiementDTO);

        return plateformePaiementRepository
            .findById(plateformePaiementDTO.getId())
            .map(existingPlateformePaiement -> {
                plateformePaiementMapper.partialUpdate(existingPlateformePaiement, plateformePaiementDTO);

                return existingPlateformePaiement;
            })
            .flatMap(plateformePaiementRepository::save)
            .map(plateformePaiementMapper::toDto);
    }

    /**
     * Get all the plateformePaiements.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<PlateformePaiementDTO> findAll() {
        log.debug("Request to get all PlateformePaiements");
        return plateformePaiementRepository.findAll().map(plateformePaiementMapper::toDto);
    }

    /**
     * Get all the plateformePaiements with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Flux<PlateformePaiementDTO> findAllWithEagerRelationships(Pageable pageable) {
        return plateformePaiementRepository.findAllWithEagerRelationships(pageable).map(plateformePaiementMapper::toDto);
    }

    /**
     * Returns the number of plateformePaiements available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return plateformePaiementRepository.count();
    }

    /**
     * Get one plateformePaiement by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<PlateformePaiementDTO> findOne(Long id) {
        log.debug("Request to get PlateformePaiement : {}", id);
        return plateformePaiementRepository.findOneWithEagerRelationships(id).map(plateformePaiementMapper::toDto);
    }

    /**
     * Delete the plateformePaiement by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete PlateformePaiement : {}", id);
        return plateformePaiementRepository.deleteById(id);
    }
}

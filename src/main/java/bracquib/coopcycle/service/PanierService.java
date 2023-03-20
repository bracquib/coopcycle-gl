package bracquib.coopcycle.service;

import bracquib.coopcycle.domain.Panier;
import bracquib.coopcycle.repository.PanierRepository;
import bracquib.coopcycle.service.dto.PanierDTO;
import bracquib.coopcycle.service.mapper.PanierMapper;
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
 * Service Implementation for managing {@link Panier}.
 */
@Service
@Transactional
public class PanierService {

    private final Logger log = LoggerFactory.getLogger(PanierService.class);

    private final PanierRepository panierRepository;

    private final PanierMapper panierMapper;

    public PanierService(PanierRepository panierRepository, PanierMapper panierMapper) {
        this.panierRepository = panierRepository;
        this.panierMapper = panierMapper;
    }

    /**
     * Save a panier.
     *
     * @param panierDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<PanierDTO> save(PanierDTO panierDTO) {
        log.debug("Request to save Panier : {}", panierDTO);
        return panierRepository.save(panierMapper.toEntity(panierDTO)).map(panierMapper::toDto);
    }

    /**
     * Update a panier.
     *
     * @param panierDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<PanierDTO> update(PanierDTO panierDTO) {
        log.debug("Request to update Panier : {}", panierDTO);
        return panierRepository.save(panierMapper.toEntity(panierDTO)).map(panierMapper::toDto);
    }

    /**
     * Partially update a panier.
     *
     * @param panierDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<PanierDTO> partialUpdate(PanierDTO panierDTO) {
        log.debug("Request to partially update Panier : {}", panierDTO);

        return panierRepository
            .findById(panierDTO.getId())
            .map(existingPanier -> {
                panierMapper.partialUpdate(existingPanier, panierDTO);

                return existingPanier;
            })
            .flatMap(panierRepository::save)
            .map(panierMapper::toDto);
    }

    /**
     * Get all the paniers.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<PanierDTO> findAll() {
        log.debug("Request to get all Paniers");
        return panierRepository.findAll().map(panierMapper::toDto);
    }

    /**
     * Get all the paniers with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Flux<PanierDTO> findAllWithEagerRelationships(Pageable pageable) {
        return panierRepository.findAllWithEagerRelationships(pageable).map(panierMapper::toDto);
    }

    /**
     * Returns the number of paniers available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return panierRepository.count();
    }

    /**
     * Get one panier by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<PanierDTO> findOne(Long id) {
        log.debug("Request to get Panier : {}", id);
        return panierRepository.findOneWithEagerRelationships(id).map(panierMapper::toDto);
    }

    /**
     * Delete the panier by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Panier : {}", id);
        return panierRepository.deleteById(id);
    }
}

package bracquib.coopcycle.service;

import bracquib.coopcycle.domain.Livreur;
import bracquib.coopcycle.repository.LivreurRepository;
import bracquib.coopcycle.service.dto.LivreurDTO;
import bracquib.coopcycle.service.mapper.LivreurMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Livreur}.
 */
@Service
@Transactional
public class LivreurService {

    private final Logger log = LoggerFactory.getLogger(LivreurService.class);

    private final LivreurRepository livreurRepository;

    private final LivreurMapper livreurMapper;

    public LivreurService(LivreurRepository livreurRepository, LivreurMapper livreurMapper) {
        this.livreurRepository = livreurRepository;
        this.livreurMapper = livreurMapper;
    }

    /**
     * Save a livreur.
     *
     * @param livreurDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<LivreurDTO> save(LivreurDTO livreurDTO) {
        log.debug("Request to save Livreur : {}", livreurDTO);
        return livreurRepository.save(livreurMapper.toEntity(livreurDTO)).map(livreurMapper::toDto);
    }

    /**
     * Update a livreur.
     *
     * @param livreurDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<LivreurDTO> update(LivreurDTO livreurDTO) {
        log.debug("Request to update Livreur : {}", livreurDTO);
        return livreurRepository.save(livreurMapper.toEntity(livreurDTO)).map(livreurMapper::toDto);
    }

    /**
     * Partially update a livreur.
     *
     * @param livreurDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<LivreurDTO> partialUpdate(LivreurDTO livreurDTO) {
        log.debug("Request to partially update Livreur : {}", livreurDTO);

        return livreurRepository
            .findById(livreurDTO.getId())
            .map(existingLivreur -> {
                livreurMapper.partialUpdate(existingLivreur, livreurDTO);

                return existingLivreur;
            })
            .flatMap(livreurRepository::save)
            .map(livreurMapper::toDto);
    }

    /**
     * Get all the livreurs.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<LivreurDTO> findAll() {
        log.debug("Request to get all Livreurs");
        return livreurRepository.findAll().map(livreurMapper::toDto);
    }

    /**
     * Returns the number of livreurs available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return livreurRepository.count();
    }

    /**
     * Get one livreur by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<LivreurDTO> findOne(Long id) {
        log.debug("Request to get Livreur : {}", id);
        return livreurRepository.findById(id).map(livreurMapper::toDto);
    }

    /**
     * Delete the livreur by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Livreur : {}", id);
        return livreurRepository.deleteById(id);
    }
}

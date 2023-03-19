package bracquib.coopcycle.service;

import bracquib.coopcycle.domain.Restaurant;
import bracquib.coopcycle.repository.RestaurantRepository;
import bracquib.coopcycle.service.dto.RestaurantDTO;
import bracquib.coopcycle.service.mapper.RestaurantMapper;
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
 * Service Implementation for managing {@link Restaurant}.
 */
@Service
@Transactional
public class RestaurantService {

    private final Logger log = LoggerFactory.getLogger(RestaurantService.class);

    private final RestaurantRepository restaurantRepository;

    private final RestaurantMapper restaurantMapper;

    public RestaurantService(RestaurantRepository restaurantRepository, RestaurantMapper restaurantMapper) {
        this.restaurantRepository = restaurantRepository;
        this.restaurantMapper = restaurantMapper;
    }

    /**
     * Save a restaurant.
     *
     * @param restaurantDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<RestaurantDTO> save(RestaurantDTO restaurantDTO) {
        log.debug("Request to save Restaurant : {}", restaurantDTO);
        return restaurantRepository.save(restaurantMapper.toEntity(restaurantDTO)).map(restaurantMapper::toDto);
    }

    /**
     * Update a restaurant.
     *
     * @param restaurantDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<RestaurantDTO> update(RestaurantDTO restaurantDTO) {
        log.debug("Request to update Restaurant : {}", restaurantDTO);
        return restaurantRepository.save(restaurantMapper.toEntity(restaurantDTO)).map(restaurantMapper::toDto);
    }

    /**
     * Partially update a restaurant.
     *
     * @param restaurantDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<RestaurantDTO> partialUpdate(RestaurantDTO restaurantDTO) {
        log.debug("Request to partially update Restaurant : {}", restaurantDTO);

        return restaurantRepository
            .findById(restaurantDTO.getId())
            .map(existingRestaurant -> {
                restaurantMapper.partialUpdate(existingRestaurant, restaurantDTO);

                return existingRestaurant;
            })
            .flatMap(restaurantRepository::save)
            .map(restaurantMapper::toDto);
    }

    /**
     * Get all the restaurants.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<RestaurantDTO> findAll() {
        log.debug("Request to get all Restaurants");
        return restaurantRepository.findAll().map(restaurantMapper::toDto);
    }

    /**
     * Returns the number of restaurants available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return restaurantRepository.count();
    }

    /**
     * Get one restaurant by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<RestaurantDTO> findOne(Long id) {
        log.debug("Request to get Restaurant : {}", id);
        return restaurantRepository.findById(id).map(restaurantMapper::toDto);
    }

    /**
     * Delete the restaurant by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Restaurant : {}", id);
        return restaurantRepository.deleteById(id);
    }
}

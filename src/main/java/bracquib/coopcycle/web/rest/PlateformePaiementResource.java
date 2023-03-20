package bracquib.coopcycle.web.rest;

import bracquib.coopcycle.repository.PlateformePaiementRepository;
import bracquib.coopcycle.service.PlateformePaiementService;
import bracquib.coopcycle.service.dto.PlateformePaiementDTO;
import bracquib.coopcycle.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link bracquib.coopcycle.domain.PlateformePaiement}.
 */
@RestController
@RequestMapping("/api")
public class PlateformePaiementResource {

    private final Logger log = LoggerFactory.getLogger(PlateformePaiementResource.class);

    private static final String ENTITY_NAME = "plateformePaiement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlateformePaiementService plateformePaiementService;

    private final PlateformePaiementRepository plateformePaiementRepository;

    public PlateformePaiementResource(
        PlateformePaiementService plateformePaiementService,
        PlateformePaiementRepository plateformePaiementRepository
    ) {
        this.plateformePaiementService = plateformePaiementService;
        this.plateformePaiementRepository = plateformePaiementRepository;
    }

    /**
     * {@code POST  /plateforme-paiements} : Create a new plateformePaiement.
     *
     * @param plateformePaiementDTO the plateformePaiementDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new plateformePaiementDTO, or with status {@code 400 (Bad Request)} if the plateformePaiement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/plateforme-paiements")
    public Mono<ResponseEntity<PlateformePaiementDTO>> createPlateformePaiement(
        @Valid @RequestBody PlateformePaiementDTO plateformePaiementDTO
    ) throws URISyntaxException {
        log.debug("REST request to save PlateformePaiement : {}", plateformePaiementDTO);
        if (plateformePaiementDTO.getId() != null) {
            throw new BadRequestAlertException("A new plateformePaiement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return plateformePaiementService
            .save(plateformePaiementDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/plateforme-paiements/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /plateforme-paiements/:id} : Updates an existing plateformePaiement.
     *
     * @param id the id of the plateformePaiementDTO to save.
     * @param plateformePaiementDTO the plateformePaiementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated plateformePaiementDTO,
     * or with status {@code 400 (Bad Request)} if the plateformePaiementDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the plateformePaiementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/plateforme-paiements/{id}")
    public Mono<ResponseEntity<PlateformePaiementDTO>> updatePlateformePaiement(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PlateformePaiementDTO plateformePaiementDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PlateformePaiement : {}, {}", id, plateformePaiementDTO);
        if (plateformePaiementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, plateformePaiementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return plateformePaiementRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return plateformePaiementService
                    .update(plateformePaiementDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /plateforme-paiements/:id} : Partial updates given fields of an existing plateformePaiement, field will ignore if it is null
     *
     * @param id the id of the plateformePaiementDTO to save.
     * @param plateformePaiementDTO the plateformePaiementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated plateformePaiementDTO,
     * or with status {@code 400 (Bad Request)} if the plateformePaiementDTO is not valid,
     * or with status {@code 404 (Not Found)} if the plateformePaiementDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the plateformePaiementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/plateforme-paiements/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<PlateformePaiementDTO>> partialUpdatePlateformePaiement(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PlateformePaiementDTO plateformePaiementDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PlateformePaiement partially : {}, {}", id, plateformePaiementDTO);
        if (plateformePaiementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, plateformePaiementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return plateformePaiementRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<PlateformePaiementDTO> result = plateformePaiementService.partialUpdate(plateformePaiementDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /plateforme-paiements} : get all the plateformePaiements.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of plateformePaiements in body.
     */
    @GetMapping("/plateforme-paiements")
    public Mono<List<PlateformePaiementDTO>> getAllPlateformePaiements(
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get all PlateformePaiements");
        return plateformePaiementService.findAll().collectList();
    }

    /**
     * {@code GET  /plateforme-paiements} : get all the plateformePaiements as a stream.
     * @return the {@link Flux} of plateformePaiements.
     */
    @GetMapping(value = "/plateforme-paiements", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<PlateformePaiementDTO> getAllPlateformePaiementsAsStream() {
        log.debug("REST request to get all PlateformePaiements as a stream");
        return plateformePaiementService.findAll();
    }

    /**
     * {@code GET  /plateforme-paiements/:id} : get the "id" plateformePaiement.
     *
     * @param id the id of the plateformePaiementDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the plateformePaiementDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/plateforme-paiements/{id}")
    public Mono<ResponseEntity<PlateformePaiementDTO>> getPlateformePaiement(@PathVariable Long id) {
        log.debug("REST request to get PlateformePaiement : {}", id);
        Mono<PlateformePaiementDTO> plateformePaiementDTO = plateformePaiementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(plateformePaiementDTO);
    }

    /**
     * {@code DELETE  /plateforme-paiements/:id} : delete the "id" plateformePaiement.
     *
     * @param id the id of the plateformePaiementDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/plateforme-paiements/{id}")
    public Mono<ResponseEntity<Void>> deletePlateformePaiement(@PathVariable Long id) {
        log.debug("REST request to delete PlateformePaiement : {}", id);
        return plateformePaiementService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity
                        .noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }
}

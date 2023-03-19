package bracquib.coopcycle.web.rest;

import bracquib.coopcycle.repository.LivreurRepository;
import bracquib.coopcycle.service.LivreurService;
import bracquib.coopcycle.service.dto.LivreurDTO;
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
 * REST controller for managing {@link bracquib.coopcycle.domain.Livreur}.
 */
@RestController
@RequestMapping("/api")
public class LivreurResource {

    private final Logger log = LoggerFactory.getLogger(LivreurResource.class);

    private static final String ENTITY_NAME = "livreur";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LivreurService livreurService;

    private final LivreurRepository livreurRepository;

    public LivreurResource(LivreurService livreurService, LivreurRepository livreurRepository) {
        this.livreurService = livreurService;
        this.livreurRepository = livreurRepository;
    }

    /**
     * {@code POST  /livreurs} : Create a new livreur.
     *
     * @param livreurDTO the livreurDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new livreurDTO, or with status {@code 400 (Bad Request)} if the livreur has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/livreurs")
    public Mono<ResponseEntity<LivreurDTO>> createLivreur(@Valid @RequestBody LivreurDTO livreurDTO) throws URISyntaxException {
        log.debug("REST request to save Livreur : {}", livreurDTO);
        if (livreurDTO.getId() != null) {
            throw new BadRequestAlertException("A new livreur cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return livreurService
            .save(livreurDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/livreurs/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /livreurs/:id} : Updates an existing livreur.
     *
     * @param id the id of the livreurDTO to save.
     * @param livreurDTO the livreurDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated livreurDTO,
     * or with status {@code 400 (Bad Request)} if the livreurDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the livreurDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/livreurs/{id}")
    public Mono<ResponseEntity<LivreurDTO>> updateLivreur(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LivreurDTO livreurDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Livreur : {}, {}", id, livreurDTO);
        if (livreurDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, livreurDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return livreurRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return livreurService
                    .update(livreurDTO)
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
     * {@code PATCH  /livreurs/:id} : Partial updates given fields of an existing livreur, field will ignore if it is null
     *
     * @param id the id of the livreurDTO to save.
     * @param livreurDTO the livreurDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated livreurDTO,
     * or with status {@code 400 (Bad Request)} if the livreurDTO is not valid,
     * or with status {@code 404 (Not Found)} if the livreurDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the livreurDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/livreurs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<LivreurDTO>> partialUpdateLivreur(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LivreurDTO livreurDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Livreur partially : {}, {}", id, livreurDTO);
        if (livreurDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, livreurDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return livreurRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<LivreurDTO> result = livreurService.partialUpdate(livreurDTO);

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
     * {@code GET  /livreurs} : get all the livreurs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of livreurs in body.
     */
    @GetMapping("/livreurs")
    public Mono<List<LivreurDTO>> getAllLivreurs() {
        log.debug("REST request to get all Livreurs");
        return livreurService.findAll().collectList();
    }

    /**
     * {@code GET  /livreurs} : get all the livreurs as a stream.
     * @return the {@link Flux} of livreurs.
     */
    @GetMapping(value = "/livreurs", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<LivreurDTO> getAllLivreursAsStream() {
        log.debug("REST request to get all Livreurs as a stream");
        return livreurService.findAll();
    }

    /**
     * {@code GET  /livreurs/:id} : get the "id" livreur.
     *
     * @param id the id of the livreurDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the livreurDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/livreurs/{id}")
    public Mono<ResponseEntity<LivreurDTO>> getLivreur(@PathVariable Long id) {
        log.debug("REST request to get Livreur : {}", id);
        Mono<LivreurDTO> livreurDTO = livreurService.findOne(id);
        return ResponseUtil.wrapOrNotFound(livreurDTO);
    }

    /**
     * {@code DELETE  /livreurs/:id} : delete the "id" livreur.
     *
     * @param id the id of the livreurDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/livreurs/{id}")
    public Mono<ResponseEntity<Void>> deleteLivreur(@PathVariable Long id) {
        log.debug("REST request to delete Livreur : {}", id);
        return livreurService
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

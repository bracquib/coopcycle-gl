package bracquib.coopcycle.web.rest;

import bracquib.coopcycle.repository.SocietaireRepository;
import bracquib.coopcycle.service.SocietaireService;
import bracquib.coopcycle.service.dto.SocietaireDTO;
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
 * REST controller for managing {@link bracquib.coopcycle.domain.Societaire}.
 */
@RestController
@RequestMapping("/api")
public class SocietaireResource {

    private final Logger log = LoggerFactory.getLogger(SocietaireResource.class);

    private static final String ENTITY_NAME = "societaire";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SocietaireService societaireService;

    private final SocietaireRepository societaireRepository;

    public SocietaireResource(SocietaireService societaireService, SocietaireRepository societaireRepository) {
        this.societaireService = societaireService;
        this.societaireRepository = societaireRepository;
    }

    /**
     * {@code POST  /societaires} : Create a new societaire.
     *
     * @param societaireDTO the societaireDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new societaireDTO, or with status {@code 400 (Bad Request)} if the societaire has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/societaires")
    public Mono<ResponseEntity<SocietaireDTO>> createSocietaire(@Valid @RequestBody SocietaireDTO societaireDTO) throws URISyntaxException {
        log.debug("REST request to save Societaire : {}", societaireDTO);
        if (societaireDTO.getId() != null) {
            throw new BadRequestAlertException("A new societaire cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return societaireService
            .save(societaireDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/societaires/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /societaires/:id} : Updates an existing societaire.
     *
     * @param id the id of the societaireDTO to save.
     * @param societaireDTO the societaireDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated societaireDTO,
     * or with status {@code 400 (Bad Request)} if the societaireDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the societaireDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/societaires/{id}")
    public Mono<ResponseEntity<SocietaireDTO>> updateSocietaire(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SocietaireDTO societaireDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Societaire : {}, {}", id, societaireDTO);
        if (societaireDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, societaireDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return societaireRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return societaireService
                    .update(societaireDTO)
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
     * {@code PATCH  /societaires/:id} : Partial updates given fields of an existing societaire, field will ignore if it is null
     *
     * @param id the id of the societaireDTO to save.
     * @param societaireDTO the societaireDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated societaireDTO,
     * or with status {@code 400 (Bad Request)} if the societaireDTO is not valid,
     * or with status {@code 404 (Not Found)} if the societaireDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the societaireDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/societaires/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<SocietaireDTO>> partialUpdateSocietaire(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SocietaireDTO societaireDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Societaire partially : {}, {}", id, societaireDTO);
        if (societaireDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, societaireDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return societaireRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<SocietaireDTO> result = societaireService.partialUpdate(societaireDTO);

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
     * {@code GET  /societaires} : get all the societaires.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of societaires in body.
     */
    @GetMapping("/societaires")
    public Mono<List<SocietaireDTO>> getAllSocietaires(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Societaires");
        return societaireService.findAll().collectList();
    }

    /**
     * {@code GET  /societaires} : get all the societaires as a stream.
     * @return the {@link Flux} of societaires.
     */
    @GetMapping(value = "/societaires", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<SocietaireDTO> getAllSocietairesAsStream() {
        log.debug("REST request to get all Societaires as a stream");
        return societaireService.findAll();
    }

    /**
     * {@code GET  /societaires/:id} : get the "id" societaire.
     *
     * @param id the id of the societaireDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the societaireDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/societaires/{id}")
    public Mono<ResponseEntity<SocietaireDTO>> getSocietaire(@PathVariable Long id) {
        log.debug("REST request to get Societaire : {}", id);
        Mono<SocietaireDTO> societaireDTO = societaireService.findOne(id);
        return ResponseUtil.wrapOrNotFound(societaireDTO);
    }

    /**
     * {@code DELETE  /societaires/:id} : delete the "id" societaire.
     *
     * @param id the id of the societaireDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/societaires/{id}")
    public Mono<ResponseEntity<Void>> deleteSocietaire(@PathVariable Long id) {
        log.debug("REST request to delete Societaire : {}", id);
        return societaireService
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

package bracquib.coopcycle.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import bracquib.coopcycle.IntegrationTest;
import bracquib.coopcycle.domain.Panier;
import bracquib.coopcycle.repository.EntityManager;
import bracquib.coopcycle.repository.PanierRepository;
import bracquib.coopcycle.service.PanierService;
import bracquib.coopcycle.service.dto.PanierDTO;
import bracquib.coopcycle.service.mapper.PanierMapper;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Integration tests for the {@link PanierResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class PanierResourceIT {

    private static final String ENTITY_API_URL = "/api/paniers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PanierRepository panierRepository;

    @Mock
    private PanierRepository panierRepositoryMock;

    @Autowired
    private PanierMapper panierMapper;

    @Mock
    private PanierService panierServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Panier panier;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Panier createEntity(EntityManager em) {
        Panier panier = new Panier();
        return panier;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Panier createUpdatedEntity(EntityManager em) {
        Panier panier = new Panier();
        return panier;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Panier.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        panier = createEntity(em);
    }

    @Test
    void createPanier() throws Exception {
        int databaseSizeBeforeCreate = panierRepository.findAll().collectList().block().size();
        // Create the Panier
        PanierDTO panierDTO = panierMapper.toDto(panier);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(panierDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Panier in the database
        List<Panier> panierList = panierRepository.findAll().collectList().block();
        assertThat(panierList).hasSize(databaseSizeBeforeCreate + 1);
        Panier testPanier = panierList.get(panierList.size() - 1);
    }

    @Test
    void createPanierWithExistingId() throws Exception {
        // Create the Panier with an existing ID
        panier.setId(1L);
        PanierDTO panierDTO = panierMapper.toDto(panier);

        int databaseSizeBeforeCreate = panierRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(panierDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Panier in the database
        List<Panier> panierList = panierRepository.findAll().collectList().block();
        assertThat(panierList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllPaniersAsStream() {
        // Initialize the database
        panierRepository.save(panier).block();

        List<Panier> panierList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(PanierDTO.class)
            .getResponseBody()
            .map(panierMapper::toEntity)
            .filter(panier::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(panierList).isNotNull();
        assertThat(panierList).hasSize(1);
        Panier testPanier = panierList.get(0);
    }

    @Test
    void getAllPaniers() {
        // Initialize the database
        panierRepository.save(panier).block();

        // Get all the panierList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(panier.getId().intValue()));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPaniersWithEagerRelationshipsIsEnabled() {
        when(panierServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(panierServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPaniersWithEagerRelationshipsIsNotEnabled() {
        when(panierServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(panierRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getPanier() {
        // Initialize the database
        panierRepository.save(panier).block();

        // Get the panier
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, panier.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(panier.getId().intValue()));
    }

    @Test
    void getNonExistingPanier() {
        // Get the panier
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingPanier() throws Exception {
        // Initialize the database
        panierRepository.save(panier).block();

        int databaseSizeBeforeUpdate = panierRepository.findAll().collectList().block().size();

        // Update the panier
        Panier updatedPanier = panierRepository.findById(panier.getId()).block();
        PanierDTO panierDTO = panierMapper.toDto(updatedPanier);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, panierDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(panierDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Panier in the database
        List<Panier> panierList = panierRepository.findAll().collectList().block();
        assertThat(panierList).hasSize(databaseSizeBeforeUpdate);
        Panier testPanier = panierList.get(panierList.size() - 1);
    }

    @Test
    void putNonExistingPanier() throws Exception {
        int databaseSizeBeforeUpdate = panierRepository.findAll().collectList().block().size();
        panier.setId(count.incrementAndGet());

        // Create the Panier
        PanierDTO panierDTO = panierMapper.toDto(panier);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, panierDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(panierDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Panier in the database
        List<Panier> panierList = panierRepository.findAll().collectList().block();
        assertThat(panierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPanier() throws Exception {
        int databaseSizeBeforeUpdate = panierRepository.findAll().collectList().block().size();
        panier.setId(count.incrementAndGet());

        // Create the Panier
        PanierDTO panierDTO = panierMapper.toDto(panier);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(panierDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Panier in the database
        List<Panier> panierList = panierRepository.findAll().collectList().block();
        assertThat(panierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPanier() throws Exception {
        int databaseSizeBeforeUpdate = panierRepository.findAll().collectList().block().size();
        panier.setId(count.incrementAndGet());

        // Create the Panier
        PanierDTO panierDTO = panierMapper.toDto(panier);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(panierDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Panier in the database
        List<Panier> panierList = panierRepository.findAll().collectList().block();
        assertThat(panierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePanierWithPatch() throws Exception {
        // Initialize the database
        panierRepository.save(panier).block();

        int databaseSizeBeforeUpdate = panierRepository.findAll().collectList().block().size();

        // Update the panier using partial update
        Panier partialUpdatedPanier = new Panier();
        partialUpdatedPanier.setId(panier.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPanier.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPanier))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Panier in the database
        List<Panier> panierList = panierRepository.findAll().collectList().block();
        assertThat(panierList).hasSize(databaseSizeBeforeUpdate);
        Panier testPanier = panierList.get(panierList.size() - 1);
    }

    @Test
    void fullUpdatePanierWithPatch() throws Exception {
        // Initialize the database
        panierRepository.save(panier).block();

        int databaseSizeBeforeUpdate = panierRepository.findAll().collectList().block().size();

        // Update the panier using partial update
        Panier partialUpdatedPanier = new Panier();
        partialUpdatedPanier.setId(panier.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPanier.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPanier))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Panier in the database
        List<Panier> panierList = panierRepository.findAll().collectList().block();
        assertThat(panierList).hasSize(databaseSizeBeforeUpdate);
        Panier testPanier = panierList.get(panierList.size() - 1);
    }

    @Test
    void patchNonExistingPanier() throws Exception {
        int databaseSizeBeforeUpdate = panierRepository.findAll().collectList().block().size();
        panier.setId(count.incrementAndGet());

        // Create the Panier
        PanierDTO panierDTO = panierMapper.toDto(panier);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, panierDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(panierDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Panier in the database
        List<Panier> panierList = panierRepository.findAll().collectList().block();
        assertThat(panierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPanier() throws Exception {
        int databaseSizeBeforeUpdate = panierRepository.findAll().collectList().block().size();
        panier.setId(count.incrementAndGet());

        // Create the Panier
        PanierDTO panierDTO = panierMapper.toDto(panier);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(panierDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Panier in the database
        List<Panier> panierList = panierRepository.findAll().collectList().block();
        assertThat(panierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPanier() throws Exception {
        int databaseSizeBeforeUpdate = panierRepository.findAll().collectList().block().size();
        panier.setId(count.incrementAndGet());

        // Create the Panier
        PanierDTO panierDTO = panierMapper.toDto(panier);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(panierDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Panier in the database
        List<Panier> panierList = panierRepository.findAll().collectList().block();
        assertThat(panierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePanier() {
        // Initialize the database
        panierRepository.save(panier).block();

        int databaseSizeBeforeDelete = panierRepository.findAll().collectList().block().size();

        // Delete the panier
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, panier.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Panier> panierList = panierRepository.findAll().collectList().block();
        assertThat(panierList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

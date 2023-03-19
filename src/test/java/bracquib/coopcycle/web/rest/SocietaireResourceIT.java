package bracquib.coopcycle.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import bracquib.coopcycle.IntegrationTest;
import bracquib.coopcycle.domain.Societaire;
import bracquib.coopcycle.repository.EntityManager;
import bracquib.coopcycle.repository.SocietaireRepository;
import bracquib.coopcycle.service.dto.SocietaireDTO;
import bracquib.coopcycle.service.mapper.SocietaireMapper;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link SocietaireResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class SocietaireResourceIT {

    private static final String DEFAULT_CLIENT = "AAAAAAAAAA";
    private static final String UPDATED_CLIENT = "BBBBBBBBBB";

    private static final String DEFAULT_RESTAURANT = "AAAAAAAAAA";
    private static final String UPDATED_RESTAURANT = "BBBBBBBBBB";

    private static final String DEFAULT_LIVREUR = "AAAAAAAAAA";
    private static final String UPDATED_LIVREUR = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/societaires";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SocietaireRepository societaireRepository;

    @Autowired
    private SocietaireMapper societaireMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Societaire societaire;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Societaire createEntity(EntityManager em) {
        Societaire societaire = new Societaire().client(DEFAULT_CLIENT).restaurant(DEFAULT_RESTAURANT).livreur(DEFAULT_LIVREUR);
        return societaire;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Societaire createUpdatedEntity(EntityManager em) {
        Societaire societaire = new Societaire().client(UPDATED_CLIENT).restaurant(UPDATED_RESTAURANT).livreur(UPDATED_LIVREUR);
        return societaire;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Societaire.class).block();
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
        societaire = createEntity(em);
    }

    @Test
    void createSocietaire() throws Exception {
        int databaseSizeBeforeCreate = societaireRepository.findAll().collectList().block().size();
        // Create the Societaire
        SocietaireDTO societaireDTO = societaireMapper.toDto(societaire);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(societaireDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Societaire in the database
        List<Societaire> societaireList = societaireRepository.findAll().collectList().block();
        assertThat(societaireList).hasSize(databaseSizeBeforeCreate + 1);
        Societaire testSocietaire = societaireList.get(societaireList.size() - 1);
        assertThat(testSocietaire.getClient()).isEqualTo(DEFAULT_CLIENT);
        assertThat(testSocietaire.getRestaurant()).isEqualTo(DEFAULT_RESTAURANT);
        assertThat(testSocietaire.getLivreur()).isEqualTo(DEFAULT_LIVREUR);
    }

    @Test
    void createSocietaireWithExistingId() throws Exception {
        // Create the Societaire with an existing ID
        societaire.setId(1L);
        SocietaireDTO societaireDTO = societaireMapper.toDto(societaire);

        int databaseSizeBeforeCreate = societaireRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(societaireDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Societaire in the database
        List<Societaire> societaireList = societaireRepository.findAll().collectList().block();
        assertThat(societaireList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkClientIsRequired() throws Exception {
        int databaseSizeBeforeTest = societaireRepository.findAll().collectList().block().size();
        // set the field null
        societaire.setClient(null);

        // Create the Societaire, which fails.
        SocietaireDTO societaireDTO = societaireMapper.toDto(societaire);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(societaireDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Societaire> societaireList = societaireRepository.findAll().collectList().block();
        assertThat(societaireList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllSocietairesAsStream() {
        // Initialize the database
        societaireRepository.save(societaire).block();

        List<Societaire> societaireList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(SocietaireDTO.class)
            .getResponseBody()
            .map(societaireMapper::toEntity)
            .filter(societaire::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(societaireList).isNotNull();
        assertThat(societaireList).hasSize(1);
        Societaire testSocietaire = societaireList.get(0);
        assertThat(testSocietaire.getClient()).isEqualTo(DEFAULT_CLIENT);
        assertThat(testSocietaire.getRestaurant()).isEqualTo(DEFAULT_RESTAURANT);
        assertThat(testSocietaire.getLivreur()).isEqualTo(DEFAULT_LIVREUR);
    }

    @Test
    void getAllSocietaires() {
        // Initialize the database
        societaireRepository.save(societaire).block();

        // Get all the societaireList
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
            .value(hasItem(societaire.getId().intValue()))
            .jsonPath("$.[*].client")
            .value(hasItem(DEFAULT_CLIENT))
            .jsonPath("$.[*].restaurant")
            .value(hasItem(DEFAULT_RESTAURANT))
            .jsonPath("$.[*].livreur")
            .value(hasItem(DEFAULT_LIVREUR));
    }

    @Test
    void getSocietaire() {
        // Initialize the database
        societaireRepository.save(societaire).block();

        // Get the societaire
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, societaire.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(societaire.getId().intValue()))
            .jsonPath("$.client")
            .value(is(DEFAULT_CLIENT))
            .jsonPath("$.restaurant")
            .value(is(DEFAULT_RESTAURANT))
            .jsonPath("$.livreur")
            .value(is(DEFAULT_LIVREUR));
    }

    @Test
    void getNonExistingSocietaire() {
        // Get the societaire
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingSocietaire() throws Exception {
        // Initialize the database
        societaireRepository.save(societaire).block();

        int databaseSizeBeforeUpdate = societaireRepository.findAll().collectList().block().size();

        // Update the societaire
        Societaire updatedSocietaire = societaireRepository.findById(societaire.getId()).block();
        updatedSocietaire.client(UPDATED_CLIENT).restaurant(UPDATED_RESTAURANT).livreur(UPDATED_LIVREUR);
        SocietaireDTO societaireDTO = societaireMapper.toDto(updatedSocietaire);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, societaireDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(societaireDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Societaire in the database
        List<Societaire> societaireList = societaireRepository.findAll().collectList().block();
        assertThat(societaireList).hasSize(databaseSizeBeforeUpdate);
        Societaire testSocietaire = societaireList.get(societaireList.size() - 1);
        assertThat(testSocietaire.getClient()).isEqualTo(UPDATED_CLIENT);
        assertThat(testSocietaire.getRestaurant()).isEqualTo(UPDATED_RESTAURANT);
        assertThat(testSocietaire.getLivreur()).isEqualTo(UPDATED_LIVREUR);
    }

    @Test
    void putNonExistingSocietaire() throws Exception {
        int databaseSizeBeforeUpdate = societaireRepository.findAll().collectList().block().size();
        societaire.setId(count.incrementAndGet());

        // Create the Societaire
        SocietaireDTO societaireDTO = societaireMapper.toDto(societaire);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, societaireDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(societaireDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Societaire in the database
        List<Societaire> societaireList = societaireRepository.findAll().collectList().block();
        assertThat(societaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchSocietaire() throws Exception {
        int databaseSizeBeforeUpdate = societaireRepository.findAll().collectList().block().size();
        societaire.setId(count.incrementAndGet());

        // Create the Societaire
        SocietaireDTO societaireDTO = societaireMapper.toDto(societaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(societaireDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Societaire in the database
        List<Societaire> societaireList = societaireRepository.findAll().collectList().block();
        assertThat(societaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamSocietaire() throws Exception {
        int databaseSizeBeforeUpdate = societaireRepository.findAll().collectList().block().size();
        societaire.setId(count.incrementAndGet());

        // Create the Societaire
        SocietaireDTO societaireDTO = societaireMapper.toDto(societaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(societaireDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Societaire in the database
        List<Societaire> societaireList = societaireRepository.findAll().collectList().block();
        assertThat(societaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateSocietaireWithPatch() throws Exception {
        // Initialize the database
        societaireRepository.save(societaire).block();

        int databaseSizeBeforeUpdate = societaireRepository.findAll().collectList().block().size();

        // Update the societaire using partial update
        Societaire partialUpdatedSocietaire = new Societaire();
        partialUpdatedSocietaire.setId(societaire.getId());

        partialUpdatedSocietaire.client(UPDATED_CLIENT).restaurant(UPDATED_RESTAURANT).livreur(UPDATED_LIVREUR);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSocietaire.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSocietaire))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Societaire in the database
        List<Societaire> societaireList = societaireRepository.findAll().collectList().block();
        assertThat(societaireList).hasSize(databaseSizeBeforeUpdate);
        Societaire testSocietaire = societaireList.get(societaireList.size() - 1);
        assertThat(testSocietaire.getClient()).isEqualTo(UPDATED_CLIENT);
        assertThat(testSocietaire.getRestaurant()).isEqualTo(UPDATED_RESTAURANT);
        assertThat(testSocietaire.getLivreur()).isEqualTo(UPDATED_LIVREUR);
    }

    @Test
    void fullUpdateSocietaireWithPatch() throws Exception {
        // Initialize the database
        societaireRepository.save(societaire).block();

        int databaseSizeBeforeUpdate = societaireRepository.findAll().collectList().block().size();

        // Update the societaire using partial update
        Societaire partialUpdatedSocietaire = new Societaire();
        partialUpdatedSocietaire.setId(societaire.getId());

        partialUpdatedSocietaire.client(UPDATED_CLIENT).restaurant(UPDATED_RESTAURANT).livreur(UPDATED_LIVREUR);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSocietaire.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSocietaire))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Societaire in the database
        List<Societaire> societaireList = societaireRepository.findAll().collectList().block();
        assertThat(societaireList).hasSize(databaseSizeBeforeUpdate);
        Societaire testSocietaire = societaireList.get(societaireList.size() - 1);
        assertThat(testSocietaire.getClient()).isEqualTo(UPDATED_CLIENT);
        assertThat(testSocietaire.getRestaurant()).isEqualTo(UPDATED_RESTAURANT);
        assertThat(testSocietaire.getLivreur()).isEqualTo(UPDATED_LIVREUR);
    }

    @Test
    void patchNonExistingSocietaire() throws Exception {
        int databaseSizeBeforeUpdate = societaireRepository.findAll().collectList().block().size();
        societaire.setId(count.incrementAndGet());

        // Create the Societaire
        SocietaireDTO societaireDTO = societaireMapper.toDto(societaire);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, societaireDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(societaireDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Societaire in the database
        List<Societaire> societaireList = societaireRepository.findAll().collectList().block();
        assertThat(societaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchSocietaire() throws Exception {
        int databaseSizeBeforeUpdate = societaireRepository.findAll().collectList().block().size();
        societaire.setId(count.incrementAndGet());

        // Create the Societaire
        SocietaireDTO societaireDTO = societaireMapper.toDto(societaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(societaireDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Societaire in the database
        List<Societaire> societaireList = societaireRepository.findAll().collectList().block();
        assertThat(societaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamSocietaire() throws Exception {
        int databaseSizeBeforeUpdate = societaireRepository.findAll().collectList().block().size();
        societaire.setId(count.incrementAndGet());

        // Create the Societaire
        SocietaireDTO societaireDTO = societaireMapper.toDto(societaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(societaireDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Societaire in the database
        List<Societaire> societaireList = societaireRepository.findAll().collectList().block();
        assertThat(societaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteSocietaire() {
        // Initialize the database
        societaireRepository.save(societaire).block();

        int databaseSizeBeforeDelete = societaireRepository.findAll().collectList().block().size();

        // Delete the societaire
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, societaire.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Societaire> societaireList = societaireRepository.findAll().collectList().block();
        assertThat(societaireList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

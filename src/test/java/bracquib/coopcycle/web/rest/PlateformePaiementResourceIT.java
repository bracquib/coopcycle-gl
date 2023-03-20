package bracquib.coopcycle.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import bracquib.coopcycle.IntegrationTest;
import bracquib.coopcycle.domain.PlateformePaiement;
import bracquib.coopcycle.domain.enumeration.TypePaiement;
import bracquib.coopcycle.repository.EntityManager;
import bracquib.coopcycle.repository.PlateformePaiementRepository;
import bracquib.coopcycle.service.PlateformePaiementService;
import bracquib.coopcycle.service.dto.PlateformePaiementDTO;
import bracquib.coopcycle.service.mapper.PlateformePaiementMapper;
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
 * Integration tests for the {@link PlateformePaiementResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class PlateformePaiementResourceIT {

    private static final String DEFAULT_AMOUNT = "AAAAAAAAAA";
    private static final String UPDATED_AMOUNT = "BBBBBBBBBB";

    private static final TypePaiement DEFAULT_PAYMENT_TYPE = TypePaiement.CB;
    private static final TypePaiement UPDATED_PAYMENT_TYPE = TypePaiement.VISA;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/plateforme-paiements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PlateformePaiementRepository plateformePaiementRepository;

    @Mock
    private PlateformePaiementRepository plateformePaiementRepositoryMock;

    @Autowired
    private PlateformePaiementMapper plateformePaiementMapper;

    @Mock
    private PlateformePaiementService plateformePaiementServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private PlateformePaiement plateformePaiement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlateformePaiement createEntity(EntityManager em) {
        PlateformePaiement plateformePaiement = new PlateformePaiement()
            .amount(DEFAULT_AMOUNT)
            .paymentType(DEFAULT_PAYMENT_TYPE)
            .description(DEFAULT_DESCRIPTION);
        return plateformePaiement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlateformePaiement createUpdatedEntity(EntityManager em) {
        PlateformePaiement plateformePaiement = new PlateformePaiement()
            .amount(UPDATED_AMOUNT)
            .paymentType(UPDATED_PAYMENT_TYPE)
            .description(UPDATED_DESCRIPTION);
        return plateformePaiement;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(PlateformePaiement.class).block();
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
        plateformePaiement = createEntity(em);
    }

    @Test
    void createPlateformePaiement() throws Exception {
        int databaseSizeBeforeCreate = plateformePaiementRepository.findAll().collectList().block().size();
        // Create the PlateformePaiement
        PlateformePaiementDTO plateformePaiementDTO = plateformePaiementMapper.toDto(plateformePaiement);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(plateformePaiementDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the PlateformePaiement in the database
        List<PlateformePaiement> plateformePaiementList = plateformePaiementRepository.findAll().collectList().block();
        assertThat(plateformePaiementList).hasSize(databaseSizeBeforeCreate + 1);
        PlateformePaiement testPlateformePaiement = plateformePaiementList.get(plateformePaiementList.size() - 1);
        assertThat(testPlateformePaiement.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testPlateformePaiement.getPaymentType()).isEqualTo(DEFAULT_PAYMENT_TYPE);
        assertThat(testPlateformePaiement.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    void createPlateformePaiementWithExistingId() throws Exception {
        // Create the PlateformePaiement with an existing ID
        plateformePaiement.setId(1L);
        PlateformePaiementDTO plateformePaiementDTO = plateformePaiementMapper.toDto(plateformePaiement);

        int databaseSizeBeforeCreate = plateformePaiementRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(plateformePaiementDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PlateformePaiement in the database
        List<PlateformePaiement> plateformePaiementList = plateformePaiementRepository.findAll().collectList().block();
        assertThat(plateformePaiementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = plateformePaiementRepository.findAll().collectList().block().size();
        // set the field null
        plateformePaiement.setAmount(null);

        // Create the PlateformePaiement, which fails.
        PlateformePaiementDTO plateformePaiementDTO = plateformePaiementMapper.toDto(plateformePaiement);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(plateformePaiementDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<PlateformePaiement> plateformePaiementList = plateformePaiementRepository.findAll().collectList().block();
        assertThat(plateformePaiementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkPaymentTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = plateformePaiementRepository.findAll().collectList().block().size();
        // set the field null
        plateformePaiement.setPaymentType(null);

        // Create the PlateformePaiement, which fails.
        PlateformePaiementDTO plateformePaiementDTO = plateformePaiementMapper.toDto(plateformePaiement);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(plateformePaiementDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<PlateformePaiement> plateformePaiementList = plateformePaiementRepository.findAll().collectList().block();
        assertThat(plateformePaiementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllPlateformePaiementsAsStream() {
        // Initialize the database
        plateformePaiementRepository.save(plateformePaiement).block();

        List<PlateformePaiement> plateformePaiementList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(PlateformePaiementDTO.class)
            .getResponseBody()
            .map(plateformePaiementMapper::toEntity)
            .filter(plateformePaiement::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(plateformePaiementList).isNotNull();
        assertThat(plateformePaiementList).hasSize(1);
        PlateformePaiement testPlateformePaiement = plateformePaiementList.get(0);
        assertThat(testPlateformePaiement.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testPlateformePaiement.getPaymentType()).isEqualTo(DEFAULT_PAYMENT_TYPE);
        assertThat(testPlateformePaiement.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    void getAllPlateformePaiements() {
        // Initialize the database
        plateformePaiementRepository.save(plateformePaiement).block();

        // Get all the plateformePaiementList
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
            .value(hasItem(plateformePaiement.getId().intValue()))
            .jsonPath("$.[*].amount")
            .value(hasItem(DEFAULT_AMOUNT))
            .jsonPath("$.[*].paymentType")
            .value(hasItem(DEFAULT_PAYMENT_TYPE.toString()))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPlateformePaiementsWithEagerRelationshipsIsEnabled() {
        when(plateformePaiementServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(plateformePaiementServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPlateformePaiementsWithEagerRelationshipsIsNotEnabled() {
        when(plateformePaiementServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(plateformePaiementRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getPlateformePaiement() {
        // Initialize the database
        plateformePaiementRepository.save(plateformePaiement).block();

        // Get the plateformePaiement
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, plateformePaiement.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(plateformePaiement.getId().intValue()))
            .jsonPath("$.amount")
            .value(is(DEFAULT_AMOUNT))
            .jsonPath("$.paymentType")
            .value(is(DEFAULT_PAYMENT_TYPE.toString()))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION));
    }

    @Test
    void getNonExistingPlateformePaiement() {
        // Get the plateformePaiement
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingPlateformePaiement() throws Exception {
        // Initialize the database
        plateformePaiementRepository.save(plateformePaiement).block();

        int databaseSizeBeforeUpdate = plateformePaiementRepository.findAll().collectList().block().size();

        // Update the plateformePaiement
        PlateformePaiement updatedPlateformePaiement = plateformePaiementRepository.findById(plateformePaiement.getId()).block();
        updatedPlateformePaiement.amount(UPDATED_AMOUNT).paymentType(UPDATED_PAYMENT_TYPE).description(UPDATED_DESCRIPTION);
        PlateformePaiementDTO plateformePaiementDTO = plateformePaiementMapper.toDto(updatedPlateformePaiement);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, plateformePaiementDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(plateformePaiementDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PlateformePaiement in the database
        List<PlateformePaiement> plateformePaiementList = plateformePaiementRepository.findAll().collectList().block();
        assertThat(plateformePaiementList).hasSize(databaseSizeBeforeUpdate);
        PlateformePaiement testPlateformePaiement = plateformePaiementList.get(plateformePaiementList.size() - 1);
        assertThat(testPlateformePaiement.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testPlateformePaiement.getPaymentType()).isEqualTo(UPDATED_PAYMENT_TYPE);
        assertThat(testPlateformePaiement.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void putNonExistingPlateformePaiement() throws Exception {
        int databaseSizeBeforeUpdate = plateformePaiementRepository.findAll().collectList().block().size();
        plateformePaiement.setId(count.incrementAndGet());

        // Create the PlateformePaiement
        PlateformePaiementDTO plateformePaiementDTO = plateformePaiementMapper.toDto(plateformePaiement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, plateformePaiementDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(plateformePaiementDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PlateformePaiement in the database
        List<PlateformePaiement> plateformePaiementList = plateformePaiementRepository.findAll().collectList().block();
        assertThat(plateformePaiementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPlateformePaiement() throws Exception {
        int databaseSizeBeforeUpdate = plateformePaiementRepository.findAll().collectList().block().size();
        plateformePaiement.setId(count.incrementAndGet());

        // Create the PlateformePaiement
        PlateformePaiementDTO plateformePaiementDTO = plateformePaiementMapper.toDto(plateformePaiement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(plateformePaiementDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PlateformePaiement in the database
        List<PlateformePaiement> plateformePaiementList = plateformePaiementRepository.findAll().collectList().block();
        assertThat(plateformePaiementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPlateformePaiement() throws Exception {
        int databaseSizeBeforeUpdate = plateformePaiementRepository.findAll().collectList().block().size();
        plateformePaiement.setId(count.incrementAndGet());

        // Create the PlateformePaiement
        PlateformePaiementDTO plateformePaiementDTO = plateformePaiementMapper.toDto(plateformePaiement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(plateformePaiementDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the PlateformePaiement in the database
        List<PlateformePaiement> plateformePaiementList = plateformePaiementRepository.findAll().collectList().block();
        assertThat(plateformePaiementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePlateformePaiementWithPatch() throws Exception {
        // Initialize the database
        plateformePaiementRepository.save(plateformePaiement).block();

        int databaseSizeBeforeUpdate = plateformePaiementRepository.findAll().collectList().block().size();

        // Update the plateformePaiement using partial update
        PlateformePaiement partialUpdatedPlateformePaiement = new PlateformePaiement();
        partialUpdatedPlateformePaiement.setId(plateformePaiement.getId());

        partialUpdatedPlateformePaiement.amount(UPDATED_AMOUNT).paymentType(UPDATED_PAYMENT_TYPE).description(UPDATED_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPlateformePaiement.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPlateformePaiement))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PlateformePaiement in the database
        List<PlateformePaiement> plateformePaiementList = plateformePaiementRepository.findAll().collectList().block();
        assertThat(plateformePaiementList).hasSize(databaseSizeBeforeUpdate);
        PlateformePaiement testPlateformePaiement = plateformePaiementList.get(plateformePaiementList.size() - 1);
        assertThat(testPlateformePaiement.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testPlateformePaiement.getPaymentType()).isEqualTo(UPDATED_PAYMENT_TYPE);
        assertThat(testPlateformePaiement.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void fullUpdatePlateformePaiementWithPatch() throws Exception {
        // Initialize the database
        plateformePaiementRepository.save(plateformePaiement).block();

        int databaseSizeBeforeUpdate = plateformePaiementRepository.findAll().collectList().block().size();

        // Update the plateformePaiement using partial update
        PlateformePaiement partialUpdatedPlateformePaiement = new PlateformePaiement();
        partialUpdatedPlateformePaiement.setId(plateformePaiement.getId());

        partialUpdatedPlateformePaiement.amount(UPDATED_AMOUNT).paymentType(UPDATED_PAYMENT_TYPE).description(UPDATED_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPlateformePaiement.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPlateformePaiement))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PlateformePaiement in the database
        List<PlateformePaiement> plateformePaiementList = plateformePaiementRepository.findAll().collectList().block();
        assertThat(plateformePaiementList).hasSize(databaseSizeBeforeUpdate);
        PlateformePaiement testPlateformePaiement = plateformePaiementList.get(plateformePaiementList.size() - 1);
        assertThat(testPlateformePaiement.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testPlateformePaiement.getPaymentType()).isEqualTo(UPDATED_PAYMENT_TYPE);
        assertThat(testPlateformePaiement.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void patchNonExistingPlateformePaiement() throws Exception {
        int databaseSizeBeforeUpdate = plateformePaiementRepository.findAll().collectList().block().size();
        plateformePaiement.setId(count.incrementAndGet());

        // Create the PlateformePaiement
        PlateformePaiementDTO plateformePaiementDTO = plateformePaiementMapper.toDto(plateformePaiement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, plateformePaiementDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(plateformePaiementDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PlateformePaiement in the database
        List<PlateformePaiement> plateformePaiementList = plateformePaiementRepository.findAll().collectList().block();
        assertThat(plateformePaiementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPlateformePaiement() throws Exception {
        int databaseSizeBeforeUpdate = plateformePaiementRepository.findAll().collectList().block().size();
        plateformePaiement.setId(count.incrementAndGet());

        // Create the PlateformePaiement
        PlateformePaiementDTO plateformePaiementDTO = plateformePaiementMapper.toDto(plateformePaiement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(plateformePaiementDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PlateformePaiement in the database
        List<PlateformePaiement> plateformePaiementList = plateformePaiementRepository.findAll().collectList().block();
        assertThat(plateformePaiementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPlateformePaiement() throws Exception {
        int databaseSizeBeforeUpdate = plateformePaiementRepository.findAll().collectList().block().size();
        plateformePaiement.setId(count.incrementAndGet());

        // Create the PlateformePaiement
        PlateformePaiementDTO plateformePaiementDTO = plateformePaiementMapper.toDto(plateformePaiement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(plateformePaiementDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the PlateformePaiement in the database
        List<PlateformePaiement> plateformePaiementList = plateformePaiementRepository.findAll().collectList().block();
        assertThat(plateformePaiementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePlateformePaiement() {
        // Initialize the database
        plateformePaiementRepository.save(plateformePaiement).block();

        int databaseSizeBeforeDelete = plateformePaiementRepository.findAll().collectList().block().size();

        // Delete the plateformePaiement
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, plateformePaiement.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<PlateformePaiement> plateformePaiementList = plateformePaiementRepository.findAll().collectList().block();
        assertThat(plateformePaiementList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

package bracquib.coopcycle.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import bracquib.coopcycle.IntegrationTest;
import bracquib.coopcycle.domain.Commande;
import bracquib.coopcycle.repository.CommandeRepository;
import bracquib.coopcycle.repository.EntityManager;
import bracquib.coopcycle.service.CommandeService;
import bracquib.coopcycle.service.dto.CommandeDTO;
import bracquib.coopcycle.service.mapper.CommandeMapper;
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
 * Integration tests for the {@link CommandeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class CommandeResourceIT {

    private static final String DEFAULT_CREATION_DATE = "AAAAAAAAAA";
    private static final String UPDATED_CREATION_DATE = "BBBBBBBBBB";

    private static final String DEFAULT_DELIVERY_DATE = "AAAAAAAAAA";
    private static final String UPDATED_DELIVERY_DATE = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/commandes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CommandeRepository commandeRepository;

    @Mock
    private CommandeRepository commandeRepositoryMock;

    @Autowired
    private CommandeMapper commandeMapper;

    @Mock
    private CommandeService commandeServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Commande commande;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Commande createEntity(EntityManager em) {
        Commande commande = new Commande().creationDate(DEFAULT_CREATION_DATE).deliveryDate(DEFAULT_DELIVERY_DATE).status(DEFAULT_STATUS);
        return commande;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Commande createUpdatedEntity(EntityManager em) {
        Commande commande = new Commande().creationDate(UPDATED_CREATION_DATE).deliveryDate(UPDATED_DELIVERY_DATE).status(UPDATED_STATUS);
        return commande;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Commande.class).block();
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
        commande = createEntity(em);
    }

    @Test
    void createCommande() throws Exception {
        int databaseSizeBeforeCreate = commandeRepository.findAll().collectList().block().size();
        // Create the Commande
        CommandeDTO commandeDTO = commandeMapper.toDto(commande);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(commandeDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll().collectList().block();
        assertThat(commandeList).hasSize(databaseSizeBeforeCreate + 1);
        Commande testCommande = commandeList.get(commandeList.size() - 1);
        assertThat(testCommande.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testCommande.getDeliveryDate()).isEqualTo(DEFAULT_DELIVERY_DATE);
        assertThat(testCommande.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    void createCommandeWithExistingId() throws Exception {
        // Create the Commande with an existing ID
        commande.setId(1L);
        CommandeDTO commandeDTO = commandeMapper.toDto(commande);

        int databaseSizeBeforeCreate = commandeRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(commandeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll().collectList().block();
        assertThat(commandeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = commandeRepository.findAll().collectList().block().size();
        // set the field null
        commande.setCreationDate(null);

        // Create the Commande, which fails.
        CommandeDTO commandeDTO = commandeMapper.toDto(commande);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(commandeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Commande> commandeList = commandeRepository.findAll().collectList().block();
        assertThat(commandeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = commandeRepository.findAll().collectList().block().size();
        // set the field null
        commande.setStatus(null);

        // Create the Commande, which fails.
        CommandeDTO commandeDTO = commandeMapper.toDto(commande);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(commandeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Commande> commandeList = commandeRepository.findAll().collectList().block();
        assertThat(commandeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllCommandesAsStream() {
        // Initialize the database
        commandeRepository.save(commande).block();

        List<Commande> commandeList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(CommandeDTO.class)
            .getResponseBody()
            .map(commandeMapper::toEntity)
            .filter(commande::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(commandeList).isNotNull();
        assertThat(commandeList).hasSize(1);
        Commande testCommande = commandeList.get(0);
        assertThat(testCommande.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testCommande.getDeliveryDate()).isEqualTo(DEFAULT_DELIVERY_DATE);
        assertThat(testCommande.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    void getAllCommandes() {
        // Initialize the database
        commandeRepository.save(commande).block();

        // Get all the commandeList
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
            .value(hasItem(commande.getId().intValue()))
            .jsonPath("$.[*].creationDate")
            .value(hasItem(DEFAULT_CREATION_DATE))
            .jsonPath("$.[*].deliveryDate")
            .value(hasItem(DEFAULT_DELIVERY_DATE))
            .jsonPath("$.[*].status")
            .value(hasItem(DEFAULT_STATUS));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCommandesWithEagerRelationshipsIsEnabled() {
        when(commandeServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(commandeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCommandesWithEagerRelationshipsIsNotEnabled() {
        when(commandeServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(commandeRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getCommande() {
        // Initialize the database
        commandeRepository.save(commande).block();

        // Get the commande
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, commande.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(commande.getId().intValue()))
            .jsonPath("$.creationDate")
            .value(is(DEFAULT_CREATION_DATE))
            .jsonPath("$.deliveryDate")
            .value(is(DEFAULT_DELIVERY_DATE))
            .jsonPath("$.status")
            .value(is(DEFAULT_STATUS));
    }

    @Test
    void getNonExistingCommande() {
        // Get the commande
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingCommande() throws Exception {
        // Initialize the database
        commandeRepository.save(commande).block();

        int databaseSizeBeforeUpdate = commandeRepository.findAll().collectList().block().size();

        // Update the commande
        Commande updatedCommande = commandeRepository.findById(commande.getId()).block();
        updatedCommande.creationDate(UPDATED_CREATION_DATE).deliveryDate(UPDATED_DELIVERY_DATE).status(UPDATED_STATUS);
        CommandeDTO commandeDTO = commandeMapper.toDto(updatedCommande);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, commandeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(commandeDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll().collectList().block();
        assertThat(commandeList).hasSize(databaseSizeBeforeUpdate);
        Commande testCommande = commandeList.get(commandeList.size() - 1);
        assertThat(testCommande.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testCommande.getDeliveryDate()).isEqualTo(UPDATED_DELIVERY_DATE);
        assertThat(testCommande.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    void putNonExistingCommande() throws Exception {
        int databaseSizeBeforeUpdate = commandeRepository.findAll().collectList().block().size();
        commande.setId(count.incrementAndGet());

        // Create the Commande
        CommandeDTO commandeDTO = commandeMapper.toDto(commande);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, commandeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(commandeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll().collectList().block();
        assertThat(commandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCommande() throws Exception {
        int databaseSizeBeforeUpdate = commandeRepository.findAll().collectList().block().size();
        commande.setId(count.incrementAndGet());

        // Create the Commande
        CommandeDTO commandeDTO = commandeMapper.toDto(commande);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(commandeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll().collectList().block();
        assertThat(commandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCommande() throws Exception {
        int databaseSizeBeforeUpdate = commandeRepository.findAll().collectList().block().size();
        commande.setId(count.incrementAndGet());

        // Create the Commande
        CommandeDTO commandeDTO = commandeMapper.toDto(commande);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(commandeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll().collectList().block();
        assertThat(commandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCommandeWithPatch() throws Exception {
        // Initialize the database
        commandeRepository.save(commande).block();

        int databaseSizeBeforeUpdate = commandeRepository.findAll().collectList().block().size();

        // Update the commande using partial update
        Commande partialUpdatedCommande = new Commande();
        partialUpdatedCommande.setId(commande.getId());

        partialUpdatedCommande.creationDate(UPDATED_CREATION_DATE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCommande.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCommande))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll().collectList().block();
        assertThat(commandeList).hasSize(databaseSizeBeforeUpdate);
        Commande testCommande = commandeList.get(commandeList.size() - 1);
        assertThat(testCommande.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testCommande.getDeliveryDate()).isEqualTo(DEFAULT_DELIVERY_DATE);
        assertThat(testCommande.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    void fullUpdateCommandeWithPatch() throws Exception {
        // Initialize the database
        commandeRepository.save(commande).block();

        int databaseSizeBeforeUpdate = commandeRepository.findAll().collectList().block().size();

        // Update the commande using partial update
        Commande partialUpdatedCommande = new Commande();
        partialUpdatedCommande.setId(commande.getId());

        partialUpdatedCommande.creationDate(UPDATED_CREATION_DATE).deliveryDate(UPDATED_DELIVERY_DATE).status(UPDATED_STATUS);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCommande.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCommande))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll().collectList().block();
        assertThat(commandeList).hasSize(databaseSizeBeforeUpdate);
        Commande testCommande = commandeList.get(commandeList.size() - 1);
        assertThat(testCommande.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testCommande.getDeliveryDate()).isEqualTo(UPDATED_DELIVERY_DATE);
        assertThat(testCommande.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    void patchNonExistingCommande() throws Exception {
        int databaseSizeBeforeUpdate = commandeRepository.findAll().collectList().block().size();
        commande.setId(count.incrementAndGet());

        // Create the Commande
        CommandeDTO commandeDTO = commandeMapper.toDto(commande);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, commandeDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(commandeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll().collectList().block();
        assertThat(commandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCommande() throws Exception {
        int databaseSizeBeforeUpdate = commandeRepository.findAll().collectList().block().size();
        commande.setId(count.incrementAndGet());

        // Create the Commande
        CommandeDTO commandeDTO = commandeMapper.toDto(commande);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(commandeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll().collectList().block();
        assertThat(commandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCommande() throws Exception {
        int databaseSizeBeforeUpdate = commandeRepository.findAll().collectList().block().size();
        commande.setId(count.incrementAndGet());

        // Create the Commande
        CommandeDTO commandeDTO = commandeMapper.toDto(commande);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(commandeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Commande in the database
        List<Commande> commandeList = commandeRepository.findAll().collectList().block();
        assertThat(commandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCommande() {
        // Initialize the database
        commandeRepository.save(commande).block();

        int databaseSizeBeforeDelete = commandeRepository.findAll().collectList().block().size();

        // Delete the commande
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, commande.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Commande> commandeList = commandeRepository.findAll().collectList().block();
        assertThat(commandeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

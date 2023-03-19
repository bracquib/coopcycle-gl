package bracquib.coopcycle.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import bracquib.coopcycle.IntegrationTest;
import bracquib.coopcycle.domain.Livreur;
import bracquib.coopcycle.repository.EntityManager;
import bracquib.coopcycle.repository.LivreurRepository;
import bracquib.coopcycle.service.dto.LivreurDTO;
import bracquib.coopcycle.service.mapper.LivreurMapper;
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
 * Integration tests for the {@link LivreurResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class LivreurResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SURNAME = "AAAAAAAAAA";
    private static final String UPDATED_SURNAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "YEG+@-q1.rhoaTC";
    private static final String UPDATED_EMAIL = "Z6U45y@TeC5.w.Esl";

    private static final String DEFAULT_PHONENUMBER = "56 6416 0296";
    private static final String UPDATED_PHONENUMBER = "34 36719670";

    private static final String DEFAULT_VEHICLE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_VEHICLE_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_COMMAND_STATE = "AAAAAAAAAA";
    private static final String UPDATED_COMMAND_STATE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/livreurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LivreurRepository livreurRepository;

    @Autowired
    private LivreurMapper livreurMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Livreur livreur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Livreur createEntity(EntityManager em) {
        Livreur livreur = new Livreur()
            .name(DEFAULT_NAME)
            .surname(DEFAULT_SURNAME)
            .email(DEFAULT_EMAIL)
            .phonenumber(DEFAULT_PHONENUMBER)
            .vehicleType(DEFAULT_VEHICLE_TYPE)
            .commandState(DEFAULT_COMMAND_STATE);
        return livreur;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Livreur createUpdatedEntity(EntityManager em) {
        Livreur livreur = new Livreur()
            .name(UPDATED_NAME)
            .surname(UPDATED_SURNAME)
            .email(UPDATED_EMAIL)
            .phonenumber(UPDATED_PHONENUMBER)
            .vehicleType(UPDATED_VEHICLE_TYPE)
            .commandState(UPDATED_COMMAND_STATE);
        return livreur;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Livreur.class).block();
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
        livreur = createEntity(em);
    }

    @Test
    void createLivreur() throws Exception {
        int databaseSizeBeforeCreate = livreurRepository.findAll().collectList().block().size();
        // Create the Livreur
        LivreurDTO livreurDTO = livreurMapper.toDto(livreur);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(livreurDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Livreur in the database
        List<Livreur> livreurList = livreurRepository.findAll().collectList().block();
        assertThat(livreurList).hasSize(databaseSizeBeforeCreate + 1);
        Livreur testLivreur = livreurList.get(livreurList.size() - 1);
        assertThat(testLivreur.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testLivreur.getSurname()).isEqualTo(DEFAULT_SURNAME);
        assertThat(testLivreur.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testLivreur.getPhonenumber()).isEqualTo(DEFAULT_PHONENUMBER);
        assertThat(testLivreur.getVehicleType()).isEqualTo(DEFAULT_VEHICLE_TYPE);
        assertThat(testLivreur.getCommandState()).isEqualTo(DEFAULT_COMMAND_STATE);
    }

    @Test
    void createLivreurWithExistingId() throws Exception {
        // Create the Livreur with an existing ID
        livreur.setId(1L);
        LivreurDTO livreurDTO = livreurMapper.toDto(livreur);

        int databaseSizeBeforeCreate = livreurRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(livreurDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Livreur in the database
        List<Livreur> livreurList = livreurRepository.findAll().collectList().block();
        assertThat(livreurList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = livreurRepository.findAll().collectList().block().size();
        // set the field null
        livreur.setName(null);

        // Create the Livreur, which fails.
        LivreurDTO livreurDTO = livreurMapper.toDto(livreur);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(livreurDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Livreur> livreurList = livreurRepository.findAll().collectList().block();
        assertThat(livreurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkSurnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = livreurRepository.findAll().collectList().block().size();
        // set the field null
        livreur.setSurname(null);

        // Create the Livreur, which fails.
        LivreurDTO livreurDTO = livreurMapper.toDto(livreur);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(livreurDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Livreur> livreurList = livreurRepository.findAll().collectList().block();
        assertThat(livreurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = livreurRepository.findAll().collectList().block().size();
        // set the field null
        livreur.setEmail(null);

        // Create the Livreur, which fails.
        LivreurDTO livreurDTO = livreurMapper.toDto(livreur);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(livreurDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Livreur> livreurList = livreurRepository.findAll().collectList().block();
        assertThat(livreurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkVehicleTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = livreurRepository.findAll().collectList().block().size();
        // set the field null
        livreur.setVehicleType(null);

        // Create the Livreur, which fails.
        LivreurDTO livreurDTO = livreurMapper.toDto(livreur);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(livreurDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Livreur> livreurList = livreurRepository.findAll().collectList().block();
        assertThat(livreurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCommandStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = livreurRepository.findAll().collectList().block().size();
        // set the field null
        livreur.setCommandState(null);

        // Create the Livreur, which fails.
        LivreurDTO livreurDTO = livreurMapper.toDto(livreur);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(livreurDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Livreur> livreurList = livreurRepository.findAll().collectList().block();
        assertThat(livreurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllLivreursAsStream() {
        // Initialize the database
        livreurRepository.save(livreur).block();

        List<Livreur> livreurList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(LivreurDTO.class)
            .getResponseBody()
            .map(livreurMapper::toEntity)
            .filter(livreur::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(livreurList).isNotNull();
        assertThat(livreurList).hasSize(1);
        Livreur testLivreur = livreurList.get(0);
        assertThat(testLivreur.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testLivreur.getSurname()).isEqualTo(DEFAULT_SURNAME);
        assertThat(testLivreur.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testLivreur.getPhonenumber()).isEqualTo(DEFAULT_PHONENUMBER);
        assertThat(testLivreur.getVehicleType()).isEqualTo(DEFAULT_VEHICLE_TYPE);
        assertThat(testLivreur.getCommandState()).isEqualTo(DEFAULT_COMMAND_STATE);
    }

    @Test
    void getAllLivreurs() {
        // Initialize the database
        livreurRepository.save(livreur).block();

        // Get all the livreurList
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
            .value(hasItem(livreur.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].surname")
            .value(hasItem(DEFAULT_SURNAME))
            .jsonPath("$.[*].email")
            .value(hasItem(DEFAULT_EMAIL))
            .jsonPath("$.[*].phonenumber")
            .value(hasItem(DEFAULT_PHONENUMBER))
            .jsonPath("$.[*].vehicleType")
            .value(hasItem(DEFAULT_VEHICLE_TYPE))
            .jsonPath("$.[*].commandState")
            .value(hasItem(DEFAULT_COMMAND_STATE));
    }

    @Test
    void getLivreur() {
        // Initialize the database
        livreurRepository.save(livreur).block();

        // Get the livreur
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, livreur.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(livreur.getId().intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.surname")
            .value(is(DEFAULT_SURNAME))
            .jsonPath("$.email")
            .value(is(DEFAULT_EMAIL))
            .jsonPath("$.phonenumber")
            .value(is(DEFAULT_PHONENUMBER))
            .jsonPath("$.vehicleType")
            .value(is(DEFAULT_VEHICLE_TYPE))
            .jsonPath("$.commandState")
            .value(is(DEFAULT_COMMAND_STATE));
    }

    @Test
    void getNonExistingLivreur() {
        // Get the livreur
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingLivreur() throws Exception {
        // Initialize the database
        livreurRepository.save(livreur).block();

        int databaseSizeBeforeUpdate = livreurRepository.findAll().collectList().block().size();

        // Update the livreur
        Livreur updatedLivreur = livreurRepository.findById(livreur.getId()).block();
        updatedLivreur
            .name(UPDATED_NAME)
            .surname(UPDATED_SURNAME)
            .email(UPDATED_EMAIL)
            .phonenumber(UPDATED_PHONENUMBER)
            .vehicleType(UPDATED_VEHICLE_TYPE)
            .commandState(UPDATED_COMMAND_STATE);
        LivreurDTO livreurDTO = livreurMapper.toDto(updatedLivreur);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, livreurDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(livreurDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Livreur in the database
        List<Livreur> livreurList = livreurRepository.findAll().collectList().block();
        assertThat(livreurList).hasSize(databaseSizeBeforeUpdate);
        Livreur testLivreur = livreurList.get(livreurList.size() - 1);
        assertThat(testLivreur.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testLivreur.getSurname()).isEqualTo(UPDATED_SURNAME);
        assertThat(testLivreur.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testLivreur.getPhonenumber()).isEqualTo(UPDATED_PHONENUMBER);
        assertThat(testLivreur.getVehicleType()).isEqualTo(UPDATED_VEHICLE_TYPE);
        assertThat(testLivreur.getCommandState()).isEqualTo(UPDATED_COMMAND_STATE);
    }

    @Test
    void putNonExistingLivreur() throws Exception {
        int databaseSizeBeforeUpdate = livreurRepository.findAll().collectList().block().size();
        livreur.setId(count.incrementAndGet());

        // Create the Livreur
        LivreurDTO livreurDTO = livreurMapper.toDto(livreur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, livreurDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(livreurDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Livreur in the database
        List<Livreur> livreurList = livreurRepository.findAll().collectList().block();
        assertThat(livreurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchLivreur() throws Exception {
        int databaseSizeBeforeUpdate = livreurRepository.findAll().collectList().block().size();
        livreur.setId(count.incrementAndGet());

        // Create the Livreur
        LivreurDTO livreurDTO = livreurMapper.toDto(livreur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(livreurDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Livreur in the database
        List<Livreur> livreurList = livreurRepository.findAll().collectList().block();
        assertThat(livreurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamLivreur() throws Exception {
        int databaseSizeBeforeUpdate = livreurRepository.findAll().collectList().block().size();
        livreur.setId(count.incrementAndGet());

        // Create the Livreur
        LivreurDTO livreurDTO = livreurMapper.toDto(livreur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(livreurDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Livreur in the database
        List<Livreur> livreurList = livreurRepository.findAll().collectList().block();
        assertThat(livreurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateLivreurWithPatch() throws Exception {
        // Initialize the database
        livreurRepository.save(livreur).block();

        int databaseSizeBeforeUpdate = livreurRepository.findAll().collectList().block().size();

        // Update the livreur using partial update
        Livreur partialUpdatedLivreur = new Livreur();
        partialUpdatedLivreur.setId(livreur.getId());

        partialUpdatedLivreur.surname(UPDATED_SURNAME).phonenumber(UPDATED_PHONENUMBER).vehicleType(UPDATED_VEHICLE_TYPE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedLivreur.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedLivreur))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Livreur in the database
        List<Livreur> livreurList = livreurRepository.findAll().collectList().block();
        assertThat(livreurList).hasSize(databaseSizeBeforeUpdate);
        Livreur testLivreur = livreurList.get(livreurList.size() - 1);
        assertThat(testLivreur.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testLivreur.getSurname()).isEqualTo(UPDATED_SURNAME);
        assertThat(testLivreur.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testLivreur.getPhonenumber()).isEqualTo(UPDATED_PHONENUMBER);
        assertThat(testLivreur.getVehicleType()).isEqualTo(UPDATED_VEHICLE_TYPE);
        assertThat(testLivreur.getCommandState()).isEqualTo(DEFAULT_COMMAND_STATE);
    }

    @Test
    void fullUpdateLivreurWithPatch() throws Exception {
        // Initialize the database
        livreurRepository.save(livreur).block();

        int databaseSizeBeforeUpdate = livreurRepository.findAll().collectList().block().size();

        // Update the livreur using partial update
        Livreur partialUpdatedLivreur = new Livreur();
        partialUpdatedLivreur.setId(livreur.getId());

        partialUpdatedLivreur
            .name(UPDATED_NAME)
            .surname(UPDATED_SURNAME)
            .email(UPDATED_EMAIL)
            .phonenumber(UPDATED_PHONENUMBER)
            .vehicleType(UPDATED_VEHICLE_TYPE)
            .commandState(UPDATED_COMMAND_STATE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedLivreur.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedLivreur))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Livreur in the database
        List<Livreur> livreurList = livreurRepository.findAll().collectList().block();
        assertThat(livreurList).hasSize(databaseSizeBeforeUpdate);
        Livreur testLivreur = livreurList.get(livreurList.size() - 1);
        assertThat(testLivreur.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testLivreur.getSurname()).isEqualTo(UPDATED_SURNAME);
        assertThat(testLivreur.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testLivreur.getPhonenumber()).isEqualTo(UPDATED_PHONENUMBER);
        assertThat(testLivreur.getVehicleType()).isEqualTo(UPDATED_VEHICLE_TYPE);
        assertThat(testLivreur.getCommandState()).isEqualTo(UPDATED_COMMAND_STATE);
    }

    @Test
    void patchNonExistingLivreur() throws Exception {
        int databaseSizeBeforeUpdate = livreurRepository.findAll().collectList().block().size();
        livreur.setId(count.incrementAndGet());

        // Create the Livreur
        LivreurDTO livreurDTO = livreurMapper.toDto(livreur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, livreurDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(livreurDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Livreur in the database
        List<Livreur> livreurList = livreurRepository.findAll().collectList().block();
        assertThat(livreurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchLivreur() throws Exception {
        int databaseSizeBeforeUpdate = livreurRepository.findAll().collectList().block().size();
        livreur.setId(count.incrementAndGet());

        // Create the Livreur
        LivreurDTO livreurDTO = livreurMapper.toDto(livreur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(livreurDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Livreur in the database
        List<Livreur> livreurList = livreurRepository.findAll().collectList().block();
        assertThat(livreurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamLivreur() throws Exception {
        int databaseSizeBeforeUpdate = livreurRepository.findAll().collectList().block().size();
        livreur.setId(count.incrementAndGet());

        // Create the Livreur
        LivreurDTO livreurDTO = livreurMapper.toDto(livreur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(livreurDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Livreur in the database
        List<Livreur> livreurList = livreurRepository.findAll().collectList().block();
        assertThat(livreurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteLivreur() {
        // Initialize the database
        livreurRepository.save(livreur).block();

        int databaseSizeBeforeDelete = livreurRepository.findAll().collectList().block().size();

        // Delete the livreur
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, livreur.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Livreur> livreurList = livreurRepository.findAll().collectList().block();
        assertThat(livreurList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

package bracquib.coopcycle.repository;

import bracquib.coopcycle.domain.PlateformePaiement;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the PlateformePaiement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlateformePaiementRepository
    extends ReactiveCrudRepository<PlateformePaiement, Long>, PlateformePaiementRepositoryInternal {
    @Override
    Mono<PlateformePaiement> findOneWithEagerRelationships(Long id);

    @Override
    Flux<PlateformePaiement> findAllWithEagerRelationships();

    @Override
    Flux<PlateformePaiement> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM plateforme_paiement entity WHERE entity.commande_id = :id")
    Flux<PlateformePaiement> findByCommande(Long id);

    @Query("SELECT * FROM plateforme_paiement entity WHERE entity.commande_id IS NULL")
    Flux<PlateformePaiement> findAllWhereCommandeIsNull();

    @Override
    <S extends PlateformePaiement> Mono<S> save(S entity);

    @Override
    Flux<PlateformePaiement> findAll();

    @Override
    Mono<PlateformePaiement> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface PlateformePaiementRepositoryInternal {
    <S extends PlateformePaiement> Mono<S> save(S entity);

    Flux<PlateformePaiement> findAllBy(Pageable pageable);

    Flux<PlateformePaiement> findAll();

    Mono<PlateformePaiement> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<PlateformePaiement> findAllBy(Pageable pageable, Criteria criteria);

    Mono<PlateformePaiement> findOneWithEagerRelationships(Long id);

    Flux<PlateformePaiement> findAllWithEagerRelationships();

    Flux<PlateformePaiement> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}

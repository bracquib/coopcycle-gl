package bracquib.coopcycle.repository;

import bracquib.coopcycle.domain.Panier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Panier entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PanierRepository extends ReactiveCrudRepository<Panier, Long>, PanierRepositoryInternal {
    @Override
    Mono<Panier> findOneWithEagerRelationships(Long id);

    @Override
    Flux<Panier> findAllWithEagerRelationships();

    @Override
    Flux<Panier> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM panier entity WHERE entity.client_id = :id")
    Flux<Panier> findByClient(Long id);

    @Query("SELECT * FROM panier entity WHERE entity.client_id IS NULL")
    Flux<Panier> findAllWhereClientIsNull();

    @Query("SELECT * FROM panier entity WHERE entity.commande_id = :id")
    Flux<Panier> findByCommande(Long id);

    @Query("SELECT * FROM panier entity WHERE entity.commande_id IS NULL")
    Flux<Panier> findAllWhereCommandeIsNull();

    @Override
    <S extends Panier> Mono<S> save(S entity);

    @Override
    Flux<Panier> findAll();

    @Override
    Mono<Panier> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface PanierRepositoryInternal {
    <S extends Panier> Mono<S> save(S entity);

    Flux<Panier> findAllBy(Pageable pageable);

    Flux<Panier> findAll();

    Mono<Panier> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Panier> findAllBy(Pageable pageable, Criteria criteria);

    Mono<Panier> findOneWithEagerRelationships(Long id);

    Flux<Panier> findAllWithEagerRelationships();

    Flux<Panier> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}

package bracquib.coopcycle.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import bracquib.coopcycle.domain.Panier;
import bracquib.coopcycle.repository.rowmapper.ClientRowMapper;
import bracquib.coopcycle.repository.rowmapper.CommandeRowMapper;
import bracquib.coopcycle.repository.rowmapper.PanierRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.BiFunction;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC custom repository implementation for the Panier entity.
 */
@SuppressWarnings("unused")
class PanierRepositoryInternalImpl extends SimpleR2dbcRepository<Panier, Long> implements PanierRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ClientRowMapper clientMapper;
    private final CommandeRowMapper commandeMapper;
    private final PanierRowMapper panierMapper;

    private static final Table entityTable = Table.aliased("panier", EntityManager.ENTITY_ALIAS);
    private static final Table clientTable = Table.aliased("client", "client");
    private static final Table commandeTable = Table.aliased("commande", "commande");

    public PanierRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ClientRowMapper clientMapper,
        CommandeRowMapper commandeMapper,
        PanierRowMapper panierMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Panier.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.clientMapper = clientMapper;
        this.commandeMapper = commandeMapper;
        this.panierMapper = panierMapper;
    }

    @Override
    public Flux<Panier> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Panier> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = PanierSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ClientSqlHelper.getColumns(clientTable, "client"));
        columns.addAll(CommandeSqlHelper.getColumns(commandeTable, "commande"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(clientTable)
            .on(Column.create("client_id", entityTable))
            .equals(Column.create("id", clientTable))
            .leftOuterJoin(commandeTable)
            .on(Column.create("commande_id", entityTable))
            .equals(Column.create("id", commandeTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Panier.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Panier> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Panier> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<Panier> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<Panier> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<Panier> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private Panier process(Row row, RowMetadata metadata) {
        Panier entity = panierMapper.apply(row, "e");
        entity.setClient(clientMapper.apply(row, "client"));
        entity.setCommande(commandeMapper.apply(row, "commande"));
        return entity;
    }

    @Override
    public <S extends Panier> Mono<S> save(S entity) {
        return super.save(entity);
    }
}

package bracquib.coopcycle.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import bracquib.coopcycle.domain.Societaire;
import bracquib.coopcycle.repository.rowmapper.ClientRowMapper;
import bracquib.coopcycle.repository.rowmapper.LivreurRowMapper;
import bracquib.coopcycle.repository.rowmapper.RestaurantRowMapper;
import bracquib.coopcycle.repository.rowmapper.SocietaireRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the Societaire entity.
 */
@SuppressWarnings("unused")
class SocietaireRepositoryInternalImpl extends SimpleR2dbcRepository<Societaire, Long> implements SocietaireRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final ClientRowMapper clientMapper;
    private final RestaurantRowMapper restaurantMapper;
    private final LivreurRowMapper livreurMapper;
    private final SocietaireRowMapper societaireMapper;

    private static final Table entityTable = Table.aliased("societaire", EntityManager.ENTITY_ALIAS);
    private static final Table clientTable = Table.aliased("client", "client");
    private static final Table restaurantTable = Table.aliased("restaurant", "restaurant");
    private static final Table livreurTable = Table.aliased("livreur", "livreur");

    public SocietaireRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        ClientRowMapper clientMapper,
        RestaurantRowMapper restaurantMapper,
        LivreurRowMapper livreurMapper,
        SocietaireRowMapper societaireMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Societaire.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.clientMapper = clientMapper;
        this.restaurantMapper = restaurantMapper;
        this.livreurMapper = livreurMapper;
        this.societaireMapper = societaireMapper;
    }

    @Override
    public Flux<Societaire> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Societaire> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = SocietaireSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(ClientSqlHelper.getColumns(clientTable, "client"));
        columns.addAll(RestaurantSqlHelper.getColumns(restaurantTable, "restaurant"));
        columns.addAll(LivreurSqlHelper.getColumns(livreurTable, "livreur"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(clientTable)
            .on(Column.create("client_id", entityTable))
            .equals(Column.create("id", clientTable))
            .leftOuterJoin(restaurantTable)
            .on(Column.create("restaurant_id", entityTable))
            .equals(Column.create("id", restaurantTable))
            .leftOuterJoin(livreurTable)
            .on(Column.create("livreur_id", entityTable))
            .equals(Column.create("id", livreurTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Societaire.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Societaire> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Societaire> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<Societaire> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<Societaire> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<Societaire> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private Societaire process(Row row, RowMetadata metadata) {
        Societaire entity = societaireMapper.apply(row, "e");
        entity.setClient(clientMapper.apply(row, "client"));
        entity.setRestaurant(restaurantMapper.apply(row, "restaurant"));
        entity.setLivreur(livreurMapper.apply(row, "livreur"));
        return entity;
    }

    @Override
    public <S extends Societaire> Mono<S> save(S entity) {
        return super.save(entity);
    }
}

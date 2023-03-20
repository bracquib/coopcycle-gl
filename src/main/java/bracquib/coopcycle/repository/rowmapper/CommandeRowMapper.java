package bracquib.coopcycle.repository.rowmapper;

import bracquib.coopcycle.domain.Commande;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Commande}, with proper type conversions.
 */
@Service
public class CommandeRowMapper implements BiFunction<Row, String, Commande> {

    private final ColumnConverter converter;

    public CommandeRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Commande} stored in the database.
     */
    @Override
    public Commande apply(Row row, String prefix) {
        Commande entity = new Commande();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setCreationDate(converter.fromRow(row, prefix + "_creation_date", String.class));
        entity.setDeliveryDate(converter.fromRow(row, prefix + "_delivery_date", String.class));
        entity.setStatus(converter.fromRow(row, prefix + "_status", String.class));
        entity.setClientId(converter.fromRow(row, prefix + "_client_id", Long.class));
        entity.setRestaurantId(converter.fromRow(row, prefix + "_restaurant_id", Long.class));
        entity.setLivreurId(converter.fromRow(row, prefix + "_livreur_id", Long.class));
        return entity;
    }
}

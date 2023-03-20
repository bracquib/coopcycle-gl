package bracquib.coopcycle.repository.rowmapper;

import bracquib.coopcycle.domain.Societaire;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Societaire}, with proper type conversions.
 */
@Service
public class SocietaireRowMapper implements BiFunction<Row, String, Societaire> {

    private final ColumnConverter converter;

    public SocietaireRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Societaire} stored in the database.
     */
    @Override
    public Societaire apply(Row row, String prefix) {
        Societaire entity = new Societaire();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNameclient(converter.fromRow(row, prefix + "_nameclient", String.class));
        entity.setNamerestaurant(converter.fromRow(row, prefix + "_namerestaurant", String.class));
        entity.setNamelivreur(converter.fromRow(row, prefix + "_namelivreur", String.class));
        entity.setClientId(converter.fromRow(row, prefix + "_client_id", Long.class));
        entity.setRestaurantId(converter.fromRow(row, prefix + "_restaurant_id", Long.class));
        entity.setLivreurId(converter.fromRow(row, prefix + "_livreur_id", Long.class));
        return entity;
    }
}

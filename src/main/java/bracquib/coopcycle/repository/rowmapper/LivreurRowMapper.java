package bracquib.coopcycle.repository.rowmapper;

import bracquib.coopcycle.domain.Livreur;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Livreur}, with proper type conversions.
 */
@Service
public class LivreurRowMapper implements BiFunction<Row, String, Livreur> {

    private final ColumnConverter converter;

    public LivreurRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Livreur} stored in the database.
     */
    @Override
    public Livreur apply(Row row, String prefix) {
        Livreur entity = new Livreur();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setSurname(converter.fromRow(row, prefix + "_surname", String.class));
        entity.setEmail(converter.fromRow(row, prefix + "_email", String.class));
        entity.setPhonenumber(converter.fromRow(row, prefix + "_phonenumber", String.class));
        entity.setVehicleType(converter.fromRow(row, prefix + "_vehicle_type", String.class));
        entity.setCommandState(converter.fromRow(row, prefix + "_command_state", String.class));
        return entity;
    }
}

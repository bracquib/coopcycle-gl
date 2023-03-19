package bracquib.coopcycle.repository.rowmapper;

import bracquib.coopcycle.domain.PlateformePaiement;
import bracquib.coopcycle.domain.enumeration.TypePaiement;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link PlateformePaiement}, with proper type conversions.
 */
@Service
public class PlateformePaiementRowMapper implements BiFunction<Row, String, PlateformePaiement> {

    private final ColumnConverter converter;

    public PlateformePaiementRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link PlateformePaiement} stored in the database.
     */
    @Override
    public PlateformePaiement apply(Row row, String prefix) {
        PlateformePaiement entity = new PlateformePaiement();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setAmount(converter.fromRow(row, prefix + "_amount", String.class));
        entity.setPaymentType(converter.fromRow(row, prefix + "_payment_type", TypePaiement.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setCommandeId(converter.fromRow(row, prefix + "_commande_id", Long.class));
        return entity;
    }
}

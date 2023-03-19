package bracquib.coopcycle.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class SocietaireSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("client", table, columnPrefix + "_client"));
        columns.add(Column.aliased("restaurant", table, columnPrefix + "_restaurant"));
        columns.add(Column.aliased("livreur", table, columnPrefix + "_livreur"));

        columns.add(Column.aliased("client_id", table, columnPrefix + "_client_id"));
        columns.add(Column.aliased("restaurant_id", table, columnPrefix + "_restaurant_id"));
        columns.add(Column.aliased("livreur_id", table, columnPrefix + "_livreur_id"));
        return columns;
    }
}

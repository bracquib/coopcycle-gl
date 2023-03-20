package bracquib.coopcycle.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class CommandeSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("creation_date", table, columnPrefix + "_creation_date"));
        columns.add(Column.aliased("delivery_date", table, columnPrefix + "_delivery_date"));
        columns.add(Column.aliased("status", table, columnPrefix + "_status"));

        columns.add(Column.aliased("client_id", table, columnPrefix + "_client_id"));
        columns.add(Column.aliased("restaurant_id", table, columnPrefix + "_restaurant_id"));
        columns.add(Column.aliased("livreur_id", table, columnPrefix + "_livreur_id"));
        return columns;
    }
}

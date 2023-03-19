package bracquib.coopcycle.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class LivreurSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("name", table, columnPrefix + "_name"));
        columns.add(Column.aliased("surname", table, columnPrefix + "_surname"));
        columns.add(Column.aliased("email", table, columnPrefix + "_email"));
        columns.add(Column.aliased("phonenumber", table, columnPrefix + "_phonenumber"));
        columns.add(Column.aliased("vehicle_type", table, columnPrefix + "_vehicle_type"));
        columns.add(Column.aliased("command_state", table, columnPrefix + "_command_state"));

        return columns;
    }
}

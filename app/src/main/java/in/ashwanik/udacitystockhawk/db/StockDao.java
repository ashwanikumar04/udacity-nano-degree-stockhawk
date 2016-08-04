package in.ashwanik.udacitystockhawk.db;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;


/**
 * Created by AshwaniK on 7/31/2016.
 */
public class StockDao {

    public static StockModel getStock(long id) {

        return SQLite
                .select()
                .from(StockModel.class)
                .where(StockModel_Table.id.eq(id))
                .querySingle();
    }

    public static StockModel getStock(String symbol) {

        return SQLite
                .select()
                .from(StockModel.class)
                .where(StockModel_Table.symbol.eq(symbol))
                .querySingle();
    }

    public static List<StockModel> getStocks() {

        return SQLite
                .select()
                .from(StockModel.class)
                .orderBy(StockHistoryModel_Table.id, true)
                .queryList();
    }


    public static List<StockHistoryModel> getStockHistory(long stockId) {

        return SQLite
                .select()
                .from(StockHistoryModel.class)
                .where(StockHistoryModel_Table.stockId.eq(stockId))
                .orderBy(StockHistoryModel_Table.id, false)
                .limit(30)
                .queryList();
    }

    public static void deleteById(long id) {
        SQLite
                .delete()
                .from(StockModel.class)
                .where(StockModel_Table.id.eq(id))
                .async()
                .execute();
        SQLite
                .delete()
                .from(StockHistoryModel.class)
                .where(StockHistoryModel_Table.stockId.eq(id))
                .async()
                .execute();
    }
}

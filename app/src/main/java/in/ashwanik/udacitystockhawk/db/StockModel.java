package in.ashwanik.udacitystockhawk.db;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.annotation.Unique;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by AshwaniK on 7/31/2016.
 */
@Table(database = AppDatabase.class)
public class StockModel extends BaseModel {
    @PrimaryKey(autoincrement = true)
    private long id;

    @Column
    @Unique
    private String symbol;

    @Column
    private String name;

    @Column
    private String change;

    @Column
    private String bid;

    public String getChangeInPercent() {
        return changeInPercent;
    }

    public boolean isShowChange = true;

    public boolean isUP() {
        return change.charAt(0) != '-';
    }

    public void setChangeInPercent(String changeInPercent) {
        this.changeInPercent = changeInPercent;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column
    private String changeInPercent;

}

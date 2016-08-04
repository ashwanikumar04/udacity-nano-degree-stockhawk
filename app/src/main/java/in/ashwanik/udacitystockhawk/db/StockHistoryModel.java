package in.ashwanik.udacitystockhawk.db;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.Date;

/**
 * Created by AshwaniK on 7/31/2016.
 */
@Table(database = AppDatabase.class)
public class StockHistoryModel extends BaseModel {
    @PrimaryKey(autoincrement = true)
    private long id;

    @Column
    private long stockId;

    @Column
    private String change;

    @Column
    private String bid;

    @Column
    private String changeInPercent;

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getChangeInPercent() {
        return changeInPercent;
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

    public long getStockId() {
        return stockId;
    }

    public void setStockId(long stockId) {
        this.stockId = stockId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column
    private Date createdAt;

}

package in.ashwanik.udacitystockhawk.events;

import in.ashwanik.udacitystockhawk.db.StockModel;

/**
 * Created by AshwaniK on 2/28/2016.
 */
public class StockResultEvent {


    public int getIndex() {
        return index;
    }

    public int getStatus() {
        return status;
    }

    private int index;
    private int status;

    public boolean isValid() {
        return isValid;
    }

    private boolean isValid;


    public StockModel getStockModel() {
        return stockModel;
    }


    private StockModel stockModel;

    public StockResultEvent(StockModel stockModel, int status, int index, boolean isValid) {
        this.stockModel = stockModel;
        this.index = index;
        this.status = status;
        this.isValid = isValid;
    }
}

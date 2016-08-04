package in.ashwanik.udacitystockhawk.services;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import in.ashwanik.retroclient.service.RetroClientServiceGenerator;
import in.ashwanik.udacitystockhawk.common.BaseApplication;
import in.ashwanik.udacitystockhawk.db.StockDao;
import in.ashwanik.udacitystockhawk.db.StockHistoryModel;
import in.ashwanik.udacitystockhawk.db.StockModel;
import in.ashwanik.udacitystockhawk.entities.Quote;
import in.ashwanik.udacitystockhawk.events.StockResultEvent;
import in.ashwanik.udacitystockhawk.utils.Helpers;
import in.ashwanik.udacitystockhawk.web.clients.StockClient;

/**
 * Created by AshwaniK on 7/30/2016.
 */
public class
StockTaskService extends GcmTaskService {
    private Context mContext;
    public static String SEARCH_KEY = "searchKey";
    public static String STATUS = "status";
    public static String INDEX = "index";

    public static final int INIT = 1;
    public static final int ADD = 2;
    public static final int UPDATE = 3;

    void logDebug(String message) {
        in.ashwanik.retroclient.utils.Helpers.d(BaseApplication.LOG, message);
    }

    @Override
    public void onInitializeTasks() {
        super.onInitializeTasks();
    }

    public StockTaskService() {
    }

    public StockTaskService(Context context) {
        mContext = context;
    }

    List<Quote> fetchData(String... symbols) throws IOException {
        RetroClientServiceGenerator personServiceGenerator = new RetroClientServiceGenerator(mContext, true);
        StockClient stockClient = personServiceGenerator.getService(StockClient.class, "stockClient");
        if (symbols.length == 1) {
            return Collections.singletonList(stockClient.getQuote(Helpers.getSearchQuery(symbols), "json", "store://datatables.org/alltableswithkeys").execute().body().query.results.quote);
        } else {
            return stockClient.getQuotes(Helpers.getSearchQuery(symbols), "json", "store://datatables.org/alltableswithkeys").execute().body().query.results.quote;
        }
    }

    StockModel getStockModel(Quote quote) {
        StockModel stockModel = StockDao.getStock(quote.symbol);
        if (stockModel == null) {
            stockModel = new StockModel();
        }
        stockModel.setSymbol(quote.symbol);
        stockModel.setChangeInPercent(quote.ChangeinPercent);
        stockModel.setBid(quote.Bid);
        stockModel.setName(quote.Name);
        stockModel.setChange(quote.Change);
        return stockModel;
    }

    @Override
    public int onRunTask(TaskParams params) {
        if (mContext == null) {
            mContext = this;
        }
        logDebug("Called 1");
        int result = GcmNetworkManager.RESULT_FAILURE;

        Bundle bundle = params.getExtras();
        if (params.getTag().equals("periodic")) {
            bundle = new Bundle();
            bundle.putString(StockTaskService.SEARCH_KEY, TextUtils.join(",", BaseApplication.getInstance().getStocks()));
            bundle.putInt(StockTaskService.STATUS, INIT);
            bundle.putInt(StockTaskService.INDEX, -1);
        }
        logDebug("Called 2");


        if (bundle == null) {
            return result;
        }

        String toSearch = bundle.getString(SEARCH_KEY);
        int status = bundle.getInt(STATUS);
        int index = bundle.getInt(INDEX);

        if (TextUtils.isEmpty(toSearch)) {
            return result;
        }

        try {
            List<Quote> quotes = fetchData(toSearch.split(","));
            logDebug("Called 3");

            StockModel toPass;
            Quote quote;
            switch (status) {
                case INIT:
                    for (Quote lQuote : quotes) {
                        StockModel stockModel = getStockModel(lQuote);
                        stockModel.save();
                        saveHistory(stockModel);
                    }
                    EventBus.getDefault().post(new StockResultEvent(null, status, index, false));
                    break;

                case ADD:
                    quote = quotes.get(0);
                    if (TextUtils.isEmpty(quote.Name)) {
                        toPass = new StockModel();
                        toPass.setSymbol(quote.symbol);
                        EventBus.getDefault().post(new StockResultEvent(toPass, status, index, false));
                    } else {
                        toPass = getStockModel(quote);
                        toPass.save();
                        saveHistory(toPass);
                        EventBus.getDefault().post(new StockResultEvent(toPass, status, index, true));
                    }
                    break;
                case UPDATE:
                    quote = quotes.get(0);
                    toPass = getStockModel(quote);
                    toPass.save();
                    saveHistory(toPass);
                    EventBus.getDefault().post(new StockResultEvent(toPass, status, index, true));
                    break;

            }

            result = GcmNetworkManager.RESULT_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private void saveHistory(StockModel stockModel) {
        StockHistoryModel stockHistoryModel = new StockHistoryModel();
        stockHistoryModel.setBid(stockModel.getBid());
        stockHistoryModel.setChangeInPercent(stockModel.getChangeInPercent());
        stockHistoryModel.setStockId(stockModel.getId());
        stockHistoryModel.setChange(stockModel.getChange());
        stockHistoryModel.setCreatedAt(new Date());
        stockHistoryModel.save();
    }
}

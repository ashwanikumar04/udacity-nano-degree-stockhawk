package in.ashwanik.udacitystockhawk.common;

import android.app.Application;
import android.os.Build;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.MaterialModule;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import in.ashwanik.retroclient.RetroClientServiceInitializer;
import in.ashwanik.udacitystockhawk.R;
import in.ashwanik.udacitystockhawk.db.StockDao;
import in.ashwanik.udacitystockhawk.db.StockModel;
import in.ashwanik.udacitystockhawk.utils.Helpers;
import in.ashwanik.udacitystockhawk.web.ApiUrls;

/**
 * Created by AshwaniK on 2/28/2016.
 */
public class BaseApplication extends Application {
    private static BaseApplication sInstance;
    int progressViewColor;

    public static String LOG = "StockHawk";

    public static BaseApplication getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        FlowManager.init(new FlowConfig.Builder(this)
                .openDatabasesOnInit(true).build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            progressViewColor = getResources().getColor(R.color.accent, null);
        } else {
            progressViewColor = getResources().getColor(R.color.accent);
        }
        Iconify
                .with(new MaterialModule());
        RetroClientServiceInitializer.getInstance().initialize(ApiUrls.BASE_API_URL, getApplicationContext(), progressViewColor, true);
        RetroClientServiceInitializer.getInstance().setLogCategoryName(LOG);
    }

    List<String> stocks;

    public List<String> getStocks() {
        int isFirst = Helpers.getIntegerAsPreference(Constants.PREFS_NAME_MAIN, Constants.IS_FIRST, 0);
        if (isFirst == 0) {
            Helpers.saveIntegerAsPreference(Constants.PREFS_NAME_MAIN, Constants.IS_FIRST, 1);
            stocks = Arrays.asList("'YHOO'", "'GOOG'", "'AAPL'", "'MSFT'");
        } else {
            List<StockModel> stockModels = StockDao.getStocks();
            stocks = new ArrayList<>();
            if (Helpers.isNotEmpty(stockModels)) {
                for (StockModel stockModel : stockModels) {
                    stocks.add("'" + stockModel.getSymbol() + "'");
                }
            }
        }
        return stocks;
    }

}

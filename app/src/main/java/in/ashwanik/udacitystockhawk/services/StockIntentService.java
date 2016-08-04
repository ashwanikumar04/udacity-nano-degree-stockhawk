package in.ashwanik.udacitystockhawk.services;

import android.app.IntentService;
import android.content.Intent;

import com.google.android.gms.gcm.TaskParams;

/**
 * Created by AshwaniK on 7/30/2016.
 */
public class StockIntentService extends IntentService {

    public StockIntentService() {
        super(StockIntentService.class.getName());
    }

    public StockIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        StockTaskService stockTaskService = new StockTaskService(this);
        TaskParams taskParams = new TaskParams("", intent.getExtras());
        stockTaskService.onRunTask(taskParams);
    }
}


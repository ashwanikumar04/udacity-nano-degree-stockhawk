package in.ashwanik.udacitystockhawk.services;

import android.content.Intent;
import android.widget.RemoteViewsService;

import in.ashwanik.udacitystockhawk.adapters.WidgetDataProvider;

/**
 * Created by AshwaniK on 8/4/2016.
 */
public class WidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        return new WidgetDataProvider(
                getApplicationContext(), intent);
    }

}
package in.ashwanik.udacitystockhawk.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;

import in.ashwanik.udacitystockhawk.R;
import in.ashwanik.udacitystockhawk.db.StockDao;
import in.ashwanik.udacitystockhawk.db.StockModel;

/**
 * Created by AshwaniK on 8/4/2016.
 */
///Taken from http://dharmangsoni.blogspot.in/2014/03/collection-widget-with-event-handling.html
public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {

    List<StockModel> mCollections = new ArrayList<>();

    Context mContext = null;

    public WidgetDataProvider(Context context, Intent intent) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return mCollections.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public RemoteViews getViewAt(int position) {

        StockModel stockModel = mCollections.get(position);

        RemoteViews mView = new RemoteViews(mContext.getPackageName(),
                R.layout.r_stock_widget);
        mView.setTextViewText(R.id.stockSymbol, stockModel.getSymbol());
        mView.setTextViewText(R.id.name, stockModel.getName());
        mView.setTextViewText(R.id.bid, stockModel.getBid());
        mView.setTextColor(R.id.stockSymbol, Color.BLACK);
        mView.setTextColor(R.id.name, Color.BLACK);
        mView.setTextColor(R.id.bid, Color.BLACK);
        return mView;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onCreate() {
        initData();
    }

    @Override
    public void onDataSetChanged() {
        initData();
    }

    private void initData() {
        mCollections.clear();
        mCollections.addAll(StockDao.getStocks());
    }

    @Override
    public void onDestroy() {

    }

}

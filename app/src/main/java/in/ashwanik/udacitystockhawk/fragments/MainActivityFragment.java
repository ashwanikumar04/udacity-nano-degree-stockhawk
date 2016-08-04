package in.ashwanik.udacitystockhawk.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.Task;
import com.joanzapata.iconify.fonts.MaterialIcons;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.ashwanik.retroclient.service.RetroClientServiceGenerator;
import in.ashwanik.retroclient.utils.Json;
import in.ashwanik.udacitystockhawk.R;
import in.ashwanik.udacitystockhawk.activity.GraphActivity;
import in.ashwanik.udacitystockhawk.activity.MainActivity;
import in.ashwanik.udacitystockhawk.adapters.StockAdapter;
import in.ashwanik.udacitystockhawk.common.BaseApplication;
import in.ashwanik.udacitystockhawk.common.Constants;
import in.ashwanik.udacitystockhawk.db.StockDao;
import in.ashwanik.udacitystockhawk.db.StockModel;
import in.ashwanik.udacitystockhawk.events.DisplayChangeEvent;
import in.ashwanik.udacitystockhawk.events.StockResultEvent;
import in.ashwanik.udacitystockhawk.interfaces.IClickHandler;
import in.ashwanik.udacitystockhawk.services.StockIntentService;
import in.ashwanik.udacitystockhawk.services.StockTaskService;
import in.ashwanik.udacitystockhawk.utils.FontIconHelper;
import in.ashwanik.udacitystockhawk.utils.Helpers;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends BaseFragment {

    View view;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    List<StockModel> stockModels;

    @Bind(R.id.noData)
    TextView noData;
    boolean isConnected;

    IClickHandler handler;
    StockAdapter adapter;
    RetroClientServiceGenerator serviceGenerator;
    FloatingActionButton floatingActionButton;
    Snackbar snackbar;

    void initializeRecyclerView() {
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
    }

    @Subscribe
    public void onEvent(final DisplayChangeEvent event) {
        for (StockModel quote : stockModels) {
            quote.isShowChange = event.isShowChange();
        }
        adapter.notifyDataSetChanged();
    }

    @Subscribe
    public void onEvent(final StockResultEvent event) {
        logDebug(Json.serialize(event));
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                StockModel stockModel;
                switch (event.getStatus()) {
                    case StockTaskService.INIT:
                        List<StockModel> latest = StockDao.getStocks();
                        stockModels.clear();
                        stockModels.addAll(latest);
                        if (in.ashwanik.udacitystockhawk.utils.Helpers.isEmpty(stockModels)) {
                            noData.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        } else {
                            noData.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                        adapter.notifyDataSetChanged();
                        break;

                    case StockTaskService.ADD:
                        stockModel = event.getStockModel();
                        if (!event.isValid()) {
                            showSnackBar(stockModel.getSymbol() + " " + getString(R.string.not_valid_stock));
                            return;
                        }
                        if (stockModels.size() == 0) {
                            noData.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                        stockModels.add(stockModel);
                        adapter.notifyItemInserted(stockModels.size() - 1);
                        recyclerView.scrollToPosition(stockModels.size() - 1);
                        break;
                    case StockTaskService.UPDATE:
                        stockModel = stockModels.get(event.getIndex());
                        stockModel.setBid(event.getStockModel().getBid());
                        stockModel.setChange(event.getStockModel().getChange());
                        stockModel.setChangeInPercent(event.getStockModel().getChangeInPercent());
                        stockModel.setBid(event.getStockModel().getBid());
                        adapter.notifyItemChanged(event.getIndex());
                        break;
                }
            }
        });
    }

    private boolean isExists(String symbol) {
        StockModel stockModel = StockDao.getStock(symbol.toUpperCase());
        return stockModel != null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_main, container, false);
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this, view);
        stockModels = StockDao.getStocks();
        serviceGenerator = new RetroClientServiceGenerator(MainActivityFragment.this.getActivity(),
                false);
        handler = new IClickHandler() {
            @Override
            public void onItemClicked(View view, int position) {
                Intent intent = new Intent(getActivity(), GraphActivity.class);
                intent.putExtra("stockId", stockModels.get(position).getId());
                startActivity(intent);
            }

            @Override
            public void onItemLongClicked(View view, int position) {

            }

            @Override
            public void onButtonAction(View view, int position) {
                switch (view.getId()) {
                    case R.id.refresh:
                        fetchData("'" + stockModels.get(position).getSymbol() + "'", position, StockTaskService.UPDATE);
                        break;
                    case R.id.delete:
                        StockDao.deleteById(stockModels.get(position).getId());
                        stockModels.remove(position);
                        adapter.notifyItemRemoved(position);
                        if (in.ashwanik.udacitystockhawk.utils.Helpers.isEmpty(stockModels)) {
                            noData.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }
                        break;

                }

            }
        };
        adapter = new StockAdapter(this.getActivity(), stockModels, handler);
        EventBus.getDefault().register(this);
        initializeRecyclerView();

        floatingActionButton = ((MainActivity) getActivity()).getFloatActionButton();
        floatingActionButton.setContentDescription(getString(R.string.add_new_stock));
        floatingActionButton.setImageDrawable(FontIconHelper.getFontDrawable(getActivity(), MaterialIcons.md_add));
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(getActivity()).title(R.string.symbol_search)
                        .content(R.string.content_test)
                        .inputType(InputType.TYPE_CLASS_TEXT)
                        .inputRangeRes(1, 20, R.color.red)
                        .input(R.string.input_hint, R.string.input_prefill, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                String inputText = input.toString();
                                if (isExists(input.toString())) {
                                    showSnackBar(getString(R.string.stock_already_saved));
                                } else {
                                    fetchData("'" + inputText.toUpperCase() + "'", -1, StockTaskService.ADD);
                                }
                            }
                        })
                        .show();
            }
        });
        checkInternetAndFetchOnInit();

        if (isConnected) {
            long period = 1800L;
            long flex = 10L;
            String periodicTag = "periodic";

            PeriodicTask periodicTask = new PeriodicTask.Builder()
                    .setService(StockTaskService.class)
                    .setPeriod(period)
                    .setFlex(flex)
                    .setTag(periodicTag)
                    .setRequiredNetwork(Task.NETWORK_STATE_CONNECTED)
                    .setRequiresCharging(false)
                    .setPersisted(true)
                    .build();
            GcmNetworkManager.getInstance(getActivity()).schedule(periodicTask);
        }

        return view;
    }

    private void fetchData(String searchKey, int index, int status) {
        Intent mServiceIntent = new Intent(getContext(), StockIntentService.class);
        mServiceIntent.putExtra(StockTaskService.SEARCH_KEY, searchKey);
        mServiceIntent.putExtra(StockTaskService.STATUS, status);
        mServiceIntent.putExtra(StockTaskService.INDEX, index);
        getActivity().startService(mServiceIntent);
    }

    private void checkInternetAndFetchOnInit() {
        if (snackbar != null) {
            snackbar.dismiss();
        }
        isConnected = in.ashwanik.retroclient.utils.Helpers.isOnline(getActivity());
        recyclerView.setVisibility(View.GONE);
        noData.setVisibility(View.GONE);
        floatingActionButton.setVisibility(View.GONE);
        if (isConnected) {
            floatingActionButton.setVisibility(View.VISIBLE);
            if (Helpers.getIntegerAsPreference(Constants.PREFS_NAME_MAIN, Constants.IS_FIRST, 0) != 0 && in.ashwanik.udacitystockhawk.utils.Helpers.isEmpty(BaseApplication.getInstance().getStocks())) {
                noData.setVisibility(View.VISIBLE);
                return;
            }
            fetchData(TextUtils.join(",", BaseApplication.getInstance().getStocks()), -1, StockTaskService.INIT);
        } else {
            snackbar = showSnackBar(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkInternetAndFetchOnInit();
                }
            });
            if (snackbar != null) {
                snackbar.show();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

}

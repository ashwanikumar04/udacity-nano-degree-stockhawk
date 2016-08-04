package in.ashwanik.udacitystockhawk.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joanzapata.iconify.fonts.MaterialIcons;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.ashwanik.udacitystockhawk.R;
import in.ashwanik.udacitystockhawk.db.StockModel;
import in.ashwanik.udacitystockhawk.interfaces.IClickHandler;
import in.ashwanik.udacitystockhawk.utils.FontIconHelper;
import in.ashwanik.udacitystockhawk.utils.Helpers;

/**
 * Created by AshwaniK on 7/30/2016.
 */

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.ItemViewHolder> {

    List<StockModel> arrayList;
    IClickHandler handler;
    private LayoutInflater inflater;
    int green;
    int red;
    String refresh;
    String delete;

    Drawable refreshImage;
    Drawable deleteImage;

    public StockAdapter(Context context, List<StockModel> quotes, IClickHandler handler) {
        inflater = LayoutInflater.from(context);
        this.handler = handler;
        this.arrayList = quotes;
        green = context.getResources().getColor(R.color.green);
        red = context.getResources().getColor(R.color.red);
        refresh = context.getResources().getString(R.string.refresh);
        delete = context.getResources().getString(R.string.delete);
        refreshImage = FontIconHelper.getFontDrawable(context, MaterialIcons.md_refresh);
        deleteImage = FontIconHelper.getFontDrawable(context, MaterialIcons.md_delete);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.r_stock, parent, false);
        ItemViewHolder holder = new ItemViewHolder(view);
        holder.setClickHandler(handler);
        return holder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        StockModel quote = arrayList.get(position);
        holder.stockSymbol.setText(quote.getSymbol());
        holder.stockSymbol.setContentDescription(quote.getName());

        holder.bid.setText(quote.getBid());
        holder.bid.setContentDescription(quote.getBid());
        if (quote.isShowChange) {
            holder.change.setText(quote.getChange());
            holder.change.setContentDescription(quote.getChange());

        } else {
            holder.change.setText(quote.getChangeInPercent());
            holder.change.setContentDescription(quote.getChangeInPercent());
        }
        if (quote.isUP()) {
            holder.change.setTextColor(green);
        } else {
            holder.change.setTextColor(red);
        }

        holder.name.setText(quote.getName());
        holder.name.setContentDescription(quote.getName());

        holder.refresh.setContentDescription(refresh + " " + quote.getName());
        holder.delete.setContentDescription(delete + " " + quote.getName());
        holder.refresh.setImageDrawable(refreshImage);
        holder.delete.setImageDrawable(deleteImage);
        Helpers.recyclerAnim(holder.itemView);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        @Bind(R.id.llWrapper)
        RelativeLayout llWrapper;
        IClickHandler clickHandler;

        @Bind(R.id.stockSymbol)
        TextView stockSymbol;
        @Bind(R.id.bid)
        TextView bid;
        @Bind(R.id.change)
        TextView change;
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.refresh)
        ImageButton refresh;
        @Bind(R.id.delete)
        ImageButton delete;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            llWrapper.setOnClickListener(this);
            llWrapper.setOnLongClickListener(this);
            refresh.setOnClickListener(this);
            delete.setOnClickListener(this);
        }

        public IClickHandler getClickHandler() {
            return clickHandler;
        }

        public void setClickHandler(IClickHandler clickHandler) {
            this.clickHandler = clickHandler;
        }

        @Override
        public void onClick(View view) {
            if (getClickHandler() != null) {
                if (view instanceof ImageButton) {
                    getClickHandler().onButtonAction(view, getLayoutPosition());
                    return;
                }
                getClickHandler().onItemClicked(view, getLayoutPosition());
            }
        }

        @Override
        public boolean onLongClick(View view) {
            if (getClickHandler() != null) {
                getClickHandler().onItemLongClicked(view, getLayoutPosition());
            }
            return true;
        }
    }

}
package com.finalAssignment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.finalAssignment.R;
import com.finalAssignment.objects.OrderRes;
import com.finalAssignment.objects.ProductList;
import com.finalAssignment.utils.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class OrderListingAdapter extends RecyclerView.Adapter<OrderListingAdapter.MyViewHolder> {

    ImageLoader imageLoader;
    public List<OrderRes.Datum> mainData = new ArrayList<>();
    Eventlistener mEventlistener;
    Context context;

    public OrderListingAdapter(Context c) {
        this.context = c;
        imageLoader = Utils.initImageLoader(context);
    }

    public void addAll(List<OrderRes.Datum> mData) {
        mainData.addAll(mData);
        notifyDataSetChanged();
    }

    public OrderRes.Datum getItem(int position) {

        return mainData.get(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.item_order_list, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final OrderRes.Datum item = mainData.get(position);
        holder.tvOrderProductId.setText(Utils.nullSafe("" + item.productId));
        holder.tvProductOrderPrice.setText(Utils.nullSafe("$" + item.productPrice));
        holder.tvShippingCost.setText(Utils.nullSafe("$" + item.shippingCost));
        holder.tvProductQTY.setText(Utils.nullSafe("" + item.quantity));
        holder.tvOrderPrice.setText(Utils.nullSafe("$" + item.totalAmount));

//        if (!StringUtils.isEmpty(item.productImage)) {
//            imageLoader.displayImage(item.productImage, holder.imgProduct);
//        }
//        holder.container.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (mEventlistener != null) {
//
//                    mEventlistener.onItemviewClick(position);
//                }
//            }
//        });
//
    }

    @Override
    public int getItemCount() {
        return mainData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvOrderProductId)
        TextView tvOrderProductId;
        @BindView(R.id.tvOrderPrice)
        TextView tvOrderPrice;
        @BindView(R.id.tvShippingCost)
        TextView tvShippingCost;
        @BindView(R.id.tvProductOrderPrice)
        TextView tvProductOrderPrice;
        @BindView(R.id.tvProductQTY)
        TextView tvProductQTY;
        @BindView(R.id.tvOrderStatus)
        TextView tvOrderStatus;
        @BindView(R.id.container)
        View container;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }

    public interface Eventlistener {
        void onItemviewClick(int position);
    }

    public void setEventlistener(Eventlistener eventlistener) {

        this.mEventlistener = eventlistener;
    }

}

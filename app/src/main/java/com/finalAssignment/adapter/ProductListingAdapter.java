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
import com.finalAssignment.objects.ProductList;
import com.finalAssignment.utils.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductListingAdapter extends RecyclerView.Adapter<ProductListingAdapter.MyViewHolder> {

    ImageLoader imageLoader;
    public List<ProductList.Datum> mainData = new ArrayList<>();
    Eventlistener mEventlistener;
    Context context;

    public ProductListingAdapter(Context c) {
        this.context = c;
        imageLoader = Utils.initImageLoader(context);
    }

    public void addAll(List<ProductList.Datum> mData) {
        mainData.addAll(mData);
        notifyDataSetChanged();
    }

    public ProductList.Datum getItem(int position) {

        return mainData.get(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.item_product_list, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final ProductList.Datum item = mainData.get(position);
        holder.tvProductName.setText(Utils.nullSafe("" + item.productName));
        holder.tvProductPrice.setText(Utils.nullSafe("$" + item.productPrice));

        if (!StringUtils.isEmpty(item.productImage)) {
            imageLoader.displayImage(item.productImage, holder.imgProduct);
        }
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEventlistener != null) {

                    mEventlistener.onItemviewClick(position);
                }
            }
        });
        holder.llAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEventlistener != null) {
                    mEventlistener.onItemCartClick(position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mainData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvProductName)
        TextView tvProductName;
        @BindView(R.id.imgProduct)
        ImageView imgProduct;
        @BindView(R.id.tvProductPrice)
        TextView tvProductPrice;
        @BindView(R.id.llAddToCart)
        LinearLayout llAddToCart;
        @BindView(R.id.container)
        View container;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }

    public interface Eventlistener {
        void onItemviewClick(int position);
        void onItemCartClick(int position);
    }

    public void setEventlistener(Eventlistener eventlistener) {

        this.mEventlistener = eventlistener;
    }

}

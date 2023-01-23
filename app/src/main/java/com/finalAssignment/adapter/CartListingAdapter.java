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
import com.finalAssignment.objects.CartRes;
import com.finalAssignment.objects.ProductList;
import com.finalAssignment.utils.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.travijuu.numberpicker.library.Enums.ActionEnum;
import com.travijuu.numberpicker.library.Interface.ValueChangedListener;
import com.travijuu.numberpicker.library.NumberPicker;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CartListingAdapter extends RecyclerView.Adapter<CartListingAdapter.MyViewHolder> {

    ImageLoader imageLoader;
    public List<CartRes.Datum> mainData = new ArrayList<>();
    Eventlistener mEventlistener;
    Context context;

    public CartListingAdapter(Context c) {
        this.context = c;
        imageLoader = Utils.initImageLoader(context);
    }

    public void addAll(List<CartRes.Datum> mData) {
        mainData.clear();
        mainData.addAll(mData);
        notifyDataSetChanged();
    }
    public void clear() {
        mainData.clear();
        notifyDataSetChanged();
    }

    public CartRes.Datum getItem(int position) {
        return mainData.get(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.item_cart_list, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final CartRes.Datum item = mainData.get(position);
        holder.tvCartProductName.setText(Utils.nullSafe("" + item.productName));
        holder.tvCartProductPrice.setText("$" + Utils.nullSafe("" + (Integer.parseInt(item.productPrice) * Integer.parseInt(item.quantity))));

        holder.numberPicker.setValue(Integer.parseInt(item.quantity));

        if (!StringUtils.isEmpty(item.productImage)) {
            imageLoader.displayImage(item.productImage, holder.imgCartProduct);
        }
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEventlistener != null) {
                    mEventlistener.onItemViewClicked(position);
                }
            }
        });

        holder.numberPicker.setValueChangedListener(new ValueChangedListener() {
            @Override
            public void valueChanged(int value, ActionEnum action) {
                if (mEventlistener != null) {
                    mEventlistener.onItemViewNumberClicked(position, value);
                }
            }
        });

        holder.imgRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEventlistener != null) {
                    mEventlistener.onItemViewDeleteClicked(position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mainData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvCartProductName)
        TextView tvCartProductName;
        @BindView(R.id.imgRemove)
        ImageView imgRemove;
        @BindView(R.id.imgCartProduct)
        ImageView imgCartProduct;
        @BindView(R.id.tvCartProductPrice)
        TextView tvCartProductPrice;
        @BindView(R.id.number_picker)
        NumberPicker numberPicker;
        @BindView(R.id.container)
        View container;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }

    public interface Eventlistener {
        void onItemViewClicked(int position);

        void onItemViewNumberClicked(int position, int i);

        void onItemViewDeleteClicked(int position);
    }

    public void setEventlistener(Eventlistener eventlistener) {

        this.mEventlistener = eventlistener;
    }

}

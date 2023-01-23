package com.finalAssignment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.common.view.SimpleListDividerDecorator;
import com.finalAssignment.adapter.OrderListingAdapter;
import com.finalAssignment.objects.OrderRes;
import com.finalAssignment.objects.ProductList;
import com.finalAssignment.objects.Response;
import com.finalAssignment.utils.AsyncHttpRequest;
import com.finalAssignment.utils.AsyncResponseHandler;
import com.finalAssignment.utils.Debug;
import com.finalAssignment.utils.RequestParamsUtils;
import com.finalAssignment.utils.URLs;
import com.finalAssignment.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.FormBody;

public class ProductDetailsActivity extends BaseActivity {

    //  bind views
    @BindView(R.id.tvProductName)
    TextView tvProductName;
    @BindView(R.id.imgProduct)
    ImageView imgProduct;
    @BindView(R.id.tvProductDesc)
    TextView tvProductDesc;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.imgAddCart)
    ImageView imgAddCart;
    ProductList.Datum itemData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
//      bind views to activity
        ButterKnife.bind(this);

//      visible backbutton on topbar
        initBack(true);
//      get data from intent params
        initIntentParams();
        init();
    }

    private void init() {

//      add cart image click
        imgAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToCart(itemData.productId, 1);
            }
        });
        fillData();
    }

    //  fill data from intent param
    private void fillData() {
        if (itemData != null) {
            setTitleText(Utils.nullSafe(itemData.productName));
            tvProductName.setText(Utils.nullSafe(itemData.productName));
            tvPrice.setText(Utils.nullSafe(itemData.productPrice));
            tvProductDesc.setText(Utils.nullSafe(itemData.productDescription));
            imageLoader = Utils.initImageLoader(getActivity());
            if (itemData.productImage != null) {
                imageLoader.displayImage(itemData.productImage, imgProduct);
            }
        }
    }

    //  get data from intentparam
    private void initIntentParams() {
        try {
            if (getIntent().getExtras() != null) {
                if (getIntent().getExtras().containsKey("data")) {
                    String data = getIntent().getStringExtra("data");
                    itemData = new Gson().fromJson(data,
                            new TypeToken<ProductList.Datum>() {
                            }.getType());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //  add to cart api call
    public void addToCart(String productId, int qty) {
        try {
            showDialog("");

            FormBody.Builder params = RequestParamsUtils.newRequestFormBody(getActivity());
            params.addEncoded("productId", productId);
            params.addEncoded("quantity", "" + qty);

            Call call = AsyncHttpRequest.newRequestPost(getActivity(), params.build(), URLs.ADD_TO_CART());
            call.enqueue(new addToCartDataHandler(getActivity()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class addToCartDataHandler extends AsyncResponseHandler {

        public addToCartDataHandler(Activity context) {
            super(context);
        }

        @Override
        public void onStart() {

        }

        @Override
        public void onFinish() {
            try {

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onSuccess(String response) {
            try {
                Debug.e("addToCartDataHandler-->>", response);
                if (response != null && response.length() > 0) {

                    Response res = new Gson().fromJson(response, new TypeToken<Response>() {
                    }.getType());
                    if (res.status.equalsIgnoreCase("200")) {
//                      redirect to cart activity if status code is 200
                        Intent i = new Intent(getActivity(), CartActivity.class);
                        startActivity(i);
                    }
                    showToast(res.message, Toast.LENGTH_SHORT);
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dismissDialog();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(Throwable e, String content) {
            Debug.e("onFailure", content);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dismissDialog();
                }
            });
        }
    }
}

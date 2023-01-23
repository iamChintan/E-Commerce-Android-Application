package com.finalAssignment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.common.view.SimpleListDividerDecorator;
import com.finalAssignment.adapter.CartListingAdapter;
import com.finalAssignment.objects.CartRes;
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

public class CartActivity extends BaseActivity {

//  bind views
    @BindView(R.id.rvCartList)
    RecyclerView rvCartList;
    @BindView(R.id.llPlaceholder)
    View llPlaceholder;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;
    CartListingAdapter cartListingAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
//      bind views to activity
        ButterKnife.bind(this);

//      visible backbutton on topbar
        initBack(true);
        init();
    }

    private void init() {
//      settitletext on topbar
        setTitleText("Cart");

//      adapter and recyclerview initialization
        cartListingAdapter = new CartListingAdapter(getActivity());
        rvCartList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvCartList.setLayoutAnimation(Utils.getRowFadeSpeedAnimation(getActivity()));
        rvCartList.addItemDecoration(new SimpleListDividerDecorator(getResources().getDrawable(R.drawable.list_divider), true));
        rvCartList.setAdapter(cartListingAdapter);

//      initialization of Eventlistener for cartListingAdapter
        cartListingAdapter.setEventlistener(new CartListingAdapter.Eventlistener() {
            @Override
            public void onItemViewClicked(int position) {

            }

            @Override
            public void onItemViewNumberClicked(int position, int i) {
                CartRes.Datum data = cartListingAdapter.getItem(position);
                addToCart(data.productId, i);
            }

            @Override
            public void onItemViewDeleteClicked(int position) {
                CartRes.Datum data = cartListingAdapter.getItem(position);
                addToCart(data.productId, 0);
            }
        });

//      submit button click
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeOrder();
            }
        });

//      make api call for cart data
        getCart();
    }

//  get cart api call
    public void getCart() {
        try {
            showDialog("");

            FormBody.Builder params = RequestParamsUtils.newRequestFormBody(getActivity());

            Call call = AsyncHttpRequest.newRequestPost(getActivity(), params.build(), URLs.GET_CART());
            call.enqueue(new getCartDataHandler(getActivity()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class getCartDataHandler extends AsyncResponseHandler {

        public getCartDataHandler(Activity context) {
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
                Debug.e("getCartDataHandler-->>", response);
                if (response != null && response.length() > 0) {

                    CartRes cartList = new Gson().fromJson(response, new TypeToken<CartRes>() {
                    }.getType());

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (cartList.status.equalsIgnoreCase("200")) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
//                                      add data in cartListingAdapter and refresh view
                                        cartListingAdapter.addAll(cartList.data);
                                        refreshPlaceHolder();
                                    }
                                });
                            } else {
                                cartListingAdapter.clear();
                                refreshPlaceHolder();
                                showToast(cartList.message, Toast.LENGTH_SHORT);
                            }
                        }
                    });
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dismissDialog();
                                refreshPlaceHolder();
                            }
                        }, 1000);
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
                    refreshPlaceHolder();
                }
            });
        }
    }

//  show no data found if cart is empty
    private void refreshPlaceHolder() {
        if (rvCartList.getAdapter().getItemCount() > 0) {
            llPlaceholder.setVisibility(View.GONE);
            rvCartList.setVisibility(View.VISIBLE);
            btnSubmit.setVisibility(View.VISIBLE);
        } else {
            llPlaceholder.setVisibility(View.VISIBLE);
            rvCartList.setVisibility(View.GONE);
            btnSubmit.setVisibility(View.GONE);
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

                    showToast(res.message, Toast.LENGTH_SHORT);
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dismissDialog();
                    }
                });
                getCart();
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

    public void makeOrder() {
        try {
            showDialog("");

            FormBody.Builder params = RequestParamsUtils.newRequestFormBody(getActivity());
//            params.addEncoded("productId", productId);
//            params.addEncoded("quantity", "" + qty);

            Call call = AsyncHttpRequest.newRequestPost(getActivity(), params.build(), URLs.MAKE_ORDER());
            call.enqueue(new makeOrderDataHandler(getActivity()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class makeOrderDataHandler extends AsyncResponseHandler {

        public makeOrderDataHandler(Activity context) {
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
                        Intent i = new Intent(getActivity(), HomeActivity.class);
                        startActivity(i);
                        finishAffinity();
                    }
                    showToast(res.message, Toast.LENGTH_SHORT);
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dismissDialog();
                    }
                });
                getCart();
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

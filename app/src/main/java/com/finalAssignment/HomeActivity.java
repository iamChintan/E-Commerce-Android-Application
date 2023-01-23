package com.finalAssignment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.common.view.SimpleListDividerDecorator;
import com.finalAssignment.adapter.ProductListingAdapter;
import com.finalAssignment.objects.Login;
import com.finalAssignment.objects.ProductList;
import com.finalAssignment.objects.Response;
import com.finalAssignment.utils.AsyncHttpRequest;
import com.finalAssignment.utils.AsyncResponseHandler;
import com.finalAssignment.utils.Constant;
import com.finalAssignment.utils.Debug;
import com.finalAssignment.utils.ExitStrategy;
import com.finalAssignment.utils.RequestParamsUtils;
import com.finalAssignment.utils.URLs;
import com.finalAssignment.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.FormBody;

public class HomeActivity extends BaseActivity {

    //  bind views
    @BindView(R.id.rvHome)
    RecyclerView rvHome;
    ProductListingAdapter productListingAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
//      bind views to activity
        ButterKnife.bind(this);

//      drawer initialization
        initDrawer();
        init();
    }

    private void init() {
        setTitleText("Home");
//      adapter and recyclerview initialization
        productListingAdapter = new ProductListingAdapter(getActivity());
        rvHome.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvHome.setLayoutAnimation(Utils.getRowFadeSpeedAnimation(getActivity()));
        rvHome.addItemDecoration(new SimpleListDividerDecorator(getResources().getDrawable(R.drawable.list_divider), true));
        rvHome.setAdapter(productListingAdapter);

//      initialization of Eventlistener for productListingAdapter
        productListingAdapter.setEventlistener(new ProductListingAdapter.Eventlistener() {
            @Override
            public void onItemviewClick(int position) {
//              fetch clicked item data and redirect to product detail page
                ProductList.Datum data = productListingAdapter.getItem(position);
                Intent i = new Intent(getActivity(), ProductDetailsActivity.class);
                i.putExtra("data", new Gson().toJson(data));
                startActivity(i);
            }

            @Override
            public void onItemCartClick(int position) {
//              get clicked item data from adapter
                ProductList.Datum data = productListingAdapter.getItem(position);
//              make api call for add to cart
                addToCart(data.productId, 1);
            }
        });
        getProducts();
    }


    @Override
    public void onBackPressed() {
//      handle onbackpressed for home page
//      if drawer is open then close drawer on first back button click
        try {
            if (result.isDrawerOpen()) {
                result.closeDrawer();
            } else {
                if (ExitStrategy.canExit()) {
                    super.onBackPressed();
                } else {
                    ExitStrategy.startExitDelay(2000);
                    Toast.makeText(getActivity(), getString(R.string.exit_msg),
                            Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //  fetch procuct api call
    public void getProducts() {
        try {
            showDialog("");

            FormBody.Builder params = RequestParamsUtils.newRequestFormBody(getActivity());
//            params.addEncoded("emailAddress", editEmail.getText().toString().trim());
//            params.addEncoded("password", editPassword.getText().toString().trim());

            Call call = AsyncHttpRequest.newRequestPost(getActivity(), params.build(), URLs.GET_PRODUCTS());
            call.enqueue(new getHomeDataHandler(getActivity()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class getHomeDataHandler extends AsyncResponseHandler {

        public getHomeDataHandler(Activity context) {
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
                Debug.e("getHomeDataHandler-->>", response);
                if (response != null && response.length() > 0) {

                    ProductList productList = new Gson().fromJson(response, new TypeToken<ProductList>() {
                    }.getType());

                    if (productList.status.equalsIgnoreCase("200")) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                              clear product list and add new data to adapter
                                productListingAdapter.addAll(productList.data);
                            }
                        });
                    } else {
                        showToast(productList.message, Toast.LENGTH_SHORT);
                    }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//      call initdrawer function for update user data if request code is 100 and result code is resultok
//      100 request code is for update profile
        if (requestCode == 100 && resultCode == RESULT_OK) {
            initDrawer();
        }
    }
}

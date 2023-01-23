package com.finalAssignment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.common.view.SimpleListDividerDecorator;
import com.finalAssignment.adapter.OrderListingAdapter;
import com.finalAssignment.objects.CartRes;
import com.finalAssignment.objects.OrderRes;
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

public class MyOrderActivity extends BaseActivity {

//  bind views
    @BindView(R.id.tvPlaceholder)
    TextView tvPlaceholder;
    @BindView(R.id.llPlaceholder)
    View llPlaceholder;
    @BindView(R.id.llRecyclerView)
    View llRecyclerView;
    @BindView(R.id.rvOrderList)
    RecyclerView rvOrderList;
    OrderListingAdapter orderListingAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myorder);
//      bind views to activity
        ButterKnife.bind(this);

//      visible backbutton on topbar
        initBack(true);
        init();
    }

    private void init() {
//      settitletext on topbar
        setTitleText("My Orders");

//      adapter and recyclerview initialization
        orderListingAdapter = new OrderListingAdapter(getActivity());
        rvOrderList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvOrderList.setLayoutAnimation(Utils.getRowFadeSpeedAnimation(getActivity()));
        rvOrderList.addItemDecoration(new SimpleListDividerDecorator(getResources().getDrawable(R.drawable.list_divider), true));
        rvOrderList.setAdapter(orderListingAdapter);
//      make api call for order list
        getOrder();
    }

//  orderlist api call
    public void getOrder() {
        try {
            showDialog("");

            FormBody.Builder params = RequestParamsUtils.newRequestFormBody(getActivity());
//            params.addEncoded("emailAddress", editEmail.getText().toString().trim());
//            params.addEncoded("password", editPassword.getText().toString().trim());

            Call call = AsyncHttpRequest.newRequestPost(getActivity(), params.build(), URLs.GET_ORDER());
            call.enqueue(new getOrderDataHandler(getActivity()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class getOrderDataHandler extends AsyncResponseHandler {

        public getOrderDataHandler(Activity context) {
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
                Debug.e("getOrderDataHandler-->>", response);
                if (response != null && response.length() > 0) {

                    OrderRes orderList = new Gson().fromJson(response, new TypeToken<OrderRes>() {
                    }.getType());

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (orderList.status.equalsIgnoreCase("200")) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        orderListingAdapter.addAll(orderList.data);
                                        refreshPlaceHolder();
                                    }
                                });
                            } else {
                                showToast(orderList.message, Toast.LENGTH_SHORT);
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

//  show no data found if orderlist is empty
    private void refreshPlaceHolder() {
        if (rvOrderList.getAdapter().getItemCount() > 0) {
            llPlaceholder.setVisibility(View.GONE);
            llRecyclerView.setVisibility(View.VISIBLE);
        } else {
            llPlaceholder.setVisibility(View.VISIBLE);
            llRecyclerView.setVisibility(View.GONE);
        }
    }
}

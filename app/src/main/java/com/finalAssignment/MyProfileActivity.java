package com.finalAssignment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.finalAssignment.objects.Login;
import com.finalAssignment.objects.Response;
import com.finalAssignment.utils.AsyncHttpRequest;
import com.finalAssignment.utils.AsyncResponseHandler;
import com.finalAssignment.utils.Constant;
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

public class MyProfileActivity extends BaseActivity {

//  bind views
    @BindView(R.id.btnProfileSubmit)
    Button btnProfileSubmit;
    @BindView(R.id.editProfileUserName)
    EditText editProfileUserName;
    @BindView(R.id.editProfileEmail)
    EditText editProfileEmail;
    @BindView(R.id.editProfileShipping)
    EditText editProfileShipping;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
//      bind views to activity
        ButterKnife.bind(this);

//      visible backbutton on topbar
        initBack(true);
        init();
    }

    private void init() {
        setTitleText("My Profile");

//      submit profile button click
        btnProfileSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//              checking validation before update api call
                if (validate()) {
//                  call update profile
                    updateProfile();
                }
            }
        });
//      make call for profile data
        getProfile(false);
    }

//  update profile api call
    public void updateProfile() {
        try {
            showDialog("");

            FormBody.Builder params = RequestParamsUtils.newRequestFormBody(getActivity());
            params.addEncoded("username", editProfileUserName.getText().toString().trim());
            params.addEncoded("emailAddress", editProfileEmail.getText().toString().trim());
            params.addEncoded("shippingAddress", editProfileShipping.getText().toString().trim());

            Call call = AsyncHttpRequest.newRequestPost(getActivity(), params.build(), URLs.UPDATE_PROFILE());
            call.enqueue(new updateProfileDataHandler(getActivity()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class updateProfileDataHandler extends AsyncResponseHandler {

        public updateProfileDataHandler(Activity context) {
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
                Debug.e("updateProfile-->>", response);
                if (response != null && response.length() > 0) {
                    Response signUpRes = new Gson().fromJson(response, new TypeToken<Response>() {
                    }.getType());
                    showToast(signUpRes.message, Toast.LENGTH_SHORT);
                    getProfile(true);
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

//  get profile api call
    public void getProfile(boolean isFinishActivity) {
        try {
            showDialog("");

            FormBody.Builder params = RequestParamsUtils.newRequestFormBody(getActivity());
            params.addEncoded("username", editProfileUserName.getText().toString().trim());
            params.addEncoded("emailAddress", editProfileEmail.getText().toString().trim());
            params.addEncoded("shippingAddress", editProfileShipping.getText().toString().trim());

            Call call = AsyncHttpRequest.newRequestPost(getActivity(), params.build(), URLs.GET_PROFILE());
            call.enqueue(new getProfileDataHandler(getActivity(), isFinishActivity));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class getProfileDataHandler extends AsyncResponseHandler {

        boolean isFinishActivity = false;

        public getProfileDataHandler(Activity context, boolean isFinishActivity) {
            super(context);
            this.isFinishActivity = isFinishActivity;
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
                Debug.e("getProfile-->>", response);
                if (response != null && response.length() > 0) {

                    Login loginRes = new Gson().fromJson(response, new TypeToken<Login>() {
                    }.getType());

                    if (loginRes.status.equalsIgnoreCase("200")) {
                        Utils.setPref(getActivity(), Constant.LOGIN_INFO, response);
//                        Utils.setPref(getActivity(), RequestParamsUtils.SESSION_ID, signUpRes.data.session);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                editProfileUserName.setText(loginRes.message.username);
                                editProfileEmail.setText(loginRes.message.emailAddress);
                                if (isFinishActivity) {
                                    setResult(RESULT_OK);
                                    finish();
                                }
                            }
                        });
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

//  check form validation
    private Boolean validate() {
        if (editProfileUserName.getText().toString().trim().length() <= 0) {
            showToast(getString(R.string.err_username), Toast.LENGTH_SHORT);
            return false;
        }
        return true;
    }
}

package com.finalAssignment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.finalAssignment.objects.SignUpRes;
import com.finalAssignment.utils.AsyncHttpRequest;
import com.finalAssignment.utils.AsyncResponseHandler;
import com.finalAssignment.utils.Debug;
import com.finalAssignment.utils.RequestParamsUtils;
import com.finalAssignment.utils.URLs;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.FormBody;

public class SignupActivity extends BaseActivity {

//  bind views
    @BindView(R.id.tvlogin)
    TextView tvlogin;
    @BindView(R.id.editSignupUser)
    EditText editSignupUser;
    @BindView(R.id.editSignUpEmail)
    EditText editSignUpEmail;
    @BindView(R.id.editSignUpPassword)
    EditText editSignUpPassword;
    @BindView(R.id.btnSignUp)
    Button btnSignUp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
//      bind views to activity
        ButterKnife.bind(this);

        init();
    }

    private void init() {
//      signup Button click handler
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//              checking validation before login api call
                if (validate()) {
//                  call signUp Api
                    signUp();
                }
            }
        });
//      signup Button click handler
        tvlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

//  signup api call
    public void signUp() {
        try {
            showDialog("");

//          create Builder Params to send params in signup post api
            FormBody.Builder params = RequestParamsUtils.newRequestFormBody(getActivity());
            params.addEncoded("username", editSignupUser.getText().toString().trim());
            params.addEncoded("emailAddress", editSignUpEmail.getText().toString().trim());
            params.addEncoded("password", editSignUpPassword.getText().toString().trim());
            params.addEncoded("shippingAddress", "canada");

//          make request for signup api
            Call call = AsyncHttpRequest.newRequestPost(getActivity(), params.build(), URLs.SIGN_UP());
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
                Debug.e("signup-->>", response);
                if (response != null && response.length() > 0) {

                    SignUpRes signUpRes = new Gson().fromJson(response, new TypeToken<SignUpRes>() {
                    }.getType());

//                  if status code is 200 then redirect to loginactivity
                    if (signUpRes.status.equalsIgnoreCase("200")) {
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        finishAffinity();
                    } else {
                        showToast(signUpRes.message, Toast.LENGTH_SHORT);
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

//  validate form details
    private Boolean validate() {
        if (editSignupUser.getText().toString().trim().length() <= 0) {
            showToast(getString(R.string.err_email), Toast.LENGTH_SHORT);
            return false;
        }
        if (editSignUpEmail.getText().toString().trim().length() <= 0) {
            showToast(getString(R.string.err_email), Toast.LENGTH_SHORT);
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(editSignUpEmail.getText()).matches()) {
            showToast(getString(R.string.err_email_invalid), Toast.LENGTH_SHORT);
            return false;
        }
        if (editSignUpPassword.getText().toString().trim().length() <= 0) {
            showToast(getString(R.string.err_password), Toast.LENGTH_SHORT);
            return false;
        }
        return true;
    }
}

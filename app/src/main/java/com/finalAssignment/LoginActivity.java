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

import com.finalAssignment.objects.Login;
import com.finalAssignment.utils.AsyncHttpRequest;
import com.finalAssignment.utils.AsyncResponseHandler;
import com.finalAssignment.utils.Constant;
import com.finalAssignment.utils.Debug;
import com.finalAssignment.utils.RequestParamsUtils;
import com.finalAssignment.utils.URLs;
import com.finalAssignment.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.FormBody;

public class LoginActivity extends BaseActivity {

//  bind views
    @BindView(R.id.editEmail)
    EditText editEmail;
    @BindView(R.id.editPassword)
    EditText editPassword;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.tvSignup)
    TextView tvSignup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//      bind views to activity
        ButterKnife.bind(this);
        init();
    }

    private void init() {
//      login Button click handler
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//              checking validation before login api call
                if (validate()) {
//                  call login Api
                    login();
                }
            }
        });

//      signup Button click handler
        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), SignupActivity.class);
                startActivity(i);
            }
        });
    }

//  validate form details
    private Boolean validate() {
        if (editEmail.getText().toString().trim().length() <= 0) {
            showToast(getString(R.string.err_email), Toast.LENGTH_SHORT);
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(editEmail.getText()).matches()) {
            showToast(getString(R.string.err_email_invalid), Toast.LENGTH_SHORT);
            return false;
        }
        if (editPassword.getText().toString().trim().length() <= 0) {
            showToast(getString(R.string.err_password), Toast.LENGTH_SHORT);
            return false;
        }
        return true;
    }

//  login api call
    public void login() {
        try {
            showDialog("");

//          create Builder Params to send params in login post api
            FormBody.Builder params = RequestParamsUtils.newRequestFormBody(getActivity());
            params.addEncoded("emailAddress", editEmail.getText().toString().trim());
            params.addEncoded("password", editPassword.getText().toString().trim());

//          make request for login api
            Call call = AsyncHttpRequest.newRequestPost(getActivity(), params.build(), URLs.LOG_IN());
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
                Debug.e("login-->>", response);
                if (response != null && response.length() > 0) {
//                  fetch response from api and check status code
                    JSONObject obj = new JSONObject(response);
                    String resCode = obj.getString("status");
                    if (resCode.equalsIgnoreCase("200")) {
                        Login login = new Gson().fromJson(response, new TypeToken<Login>() {
                        }.getType());
//                      if status code is 200 then save response in pref and redirect to home page
                        if (login.status.equalsIgnoreCase("200")) {
                            Utils.setPref(getActivity(), Constant.LOGIN_INFO, response);
                            Utils.setPref(getActivity(), RequestParamsUtils.SESSION_ID, login.message.token);
                            Intent intent = new Intent(getActivity(), HomeActivity.class);
                            startActivity(intent);
                            finishAffinity();
                        } else {
                            showToast(login.message.token, Toast.LENGTH_SHORT);
                        }
                    } else {
                        String resMsg = obj.getString("message");
                        showToast(resMsg, Toast.LENGTH_SHORT);
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

}

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

public class ChangePasswordActivity extends BaseActivity {

//  bind views
    @BindView(R.id.btnChangePass)
    Button btnChangePass;
    @BindView(R.id.editProfileOldPassword)
    EditText editProfileOldPassword;
    @BindView(R.id.editProfileNewPassword)
    EditText editProfileNewPassword;
    @BindView(R.id.editProfileRetypePassword)
    EditText editProfileRetypePassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);
//      bind views to activity
        ButterKnife.bind(this);

//      visible backbutton on topbar
        initBack(true);
        init();
    }

    private void init() {
//      settitletext on topbar
        setTitleText("Change Password");

//      change password button click
        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    changePassword();
                }
            }
        });
    }

//  change password api call
    public void changePassword() {
        try {
            showDialog("");

            FormBody.Builder params = RequestParamsUtils.newRequestFormBody(getActivity());
            params.addEncoded("oldPassword", editProfileOldPassword.getText().toString().trim());
            params.addEncoded("newPassword", editProfileNewPassword.getText().toString().trim());

            Call call = AsyncHttpRequest.newRequestPost(getActivity(), params.build(), URLs.CHANGE_PASSWORD());
            call.enqueue(new updatePasswordDataHandler(getActivity()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class updatePasswordDataHandler extends AsyncResponseHandler {

        public updatePasswordDataHandler(Activity context) {
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
                Debug.e("getProfile-->>", response);
                if (response != null && response.length() > 0) {

                    Response loginRes = new Gson().fromJson(response, new TypeToken<Response>() {
                    }.getType());

                    if (loginRes.status.equalsIgnoreCase("200")) {
//                        Utils.setPref(getActivity(), RequestParamsUtils.SESSION_ID, signUpRes.data.session);
                        showToast(loginRes.message, Toast.LENGTH_SHORT);
                        finish();
                    } else {
                        showToast(loginRes.message, Toast.LENGTH_SHORT);
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
        if (editProfileOldPassword.getText().toString().trim().length() <= 0) {
            showToast(getString(R.string.err_oldpass), Toast.LENGTH_SHORT);
            return false;
        }
        if (editProfileNewPassword.getText().toString().trim().length() <= 0) {
            showToast(getString(R.string.err_newpass), Toast.LENGTH_SHORT);
            return false;
        }
        if (editProfileRetypePassword.getText().toString().trim().length() <= 0) {
            showToast(getString(R.string.err_retypepass), Toast.LENGTH_SHORT);
            return false;
        }
        if (!editProfileNewPassword.getText().toString().equals(editProfileRetypePassword.getText().toString())) {
            showToast(getString(R.string.err_password_match), Toast.LENGTH_SHORT);
            return false;
        }
        return true;
    }
}

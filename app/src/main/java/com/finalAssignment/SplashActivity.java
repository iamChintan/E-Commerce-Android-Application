package com.finalAssignment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.finalAssignment.utils.Constant;
import com.finalAssignment.utils.Debug;
import com.finalAssignment.utils.Utils;

import butterknife.ButterKnife;

public class SplashActivity extends BaseActivity {
    String TAG = "SplashActivity";
    Handler handler = new Handler();
    TextView splashMsg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

//      check first internet is connected or not
        if (Utils.isInternetConnected(getActivity())) {
            startApplication(1000);
        } else {
            handler.post(mPostInternetConDialog);
        }

    }

    private void startApplication(long sleepTime) {
        handler.postDelayed(startApp, sleepTime);
    }

    Runnable startApp = new Runnable() {
        @Override
        public void run() {
            handler.removeCallbacks(startApp);
            Debug.e(TAG, "startApp");
            if (!Utils.isUserLoggedIn(getActivity())) {
                Intent i = new Intent(getActivity(), LoginActivity.class);
                startActivity(i);
                finish();
            } else {
                Intent i = new Intent(getActivity(),
                        HomeActivity.class);
                startActivity(i);
                finish();
            }
        }

    };


    int count = 30;

    Runnable mPostInternetConDialog = new Runnable() {

        @Override
        public void run() {

            MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity())
                    .title(R.string.connection_title)
                    .content(R.string.connection_not_available)
                    .positiveText(R.string.btn_enable)
                    .negativeText(R.string.btn_cancel)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                            try {
                                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                                startActivity(intent);
                                finish();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {

                        }
                    });

            MaterialDialog dialog = builder.build();
            dialog.show();

        }
    };

    Runnable checkConnection = new Runnable() {
        @Override
        public void run() {
            Debug.e(TAG, "checkConnection");
            if (Utils.isInternetConnected(getActivity())) {

                splashMsg.setText(getString(R.string.connected));
                handler.removeCallbacks(checkConnection);

                if (Utils.isInternetConnected(getActivity())) {
                    startApplication(1000);
                } else {
                    handler.post(mPostInternetConDialog);
                }

            } else {
                if (count != 0) {
                    splashMsg.setText(String.format(
                            getString(R.string.retrying), "" + (count--)));
                    handler.postDelayed(checkConnection, 1000);
                } else {
                    splashMsg.setText("Finishing... ");
                    finish();
                }

            }
        }
    };

    @Override
    public void onDestroy() {
        try {
            handler.removeCallbacks(startApp);
            handler.removeCallbacks(checkConnection);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

}

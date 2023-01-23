package com.finalAssignment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.finalAssignment.objects.Login;
import com.finalAssignment.utils.AsyncProgressDialog;
import com.finalAssignment.utils.Constant;
import com.finalAssignment.utils.Utils;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.nostra13.universalimageloader.core.ImageLoader;

public class BaseActivity extends AppCompatActivity {
    AsyncProgressDialog ad;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        toast = Toast.makeText(getActivity(), "", Toast.LENGTH_LONG);
    }

    Drawer result;

    public void initDrawer() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        LayoutInflater inflater = getLayoutInflater();
        View myLayout = inflater.inflate(R.layout.nav_header_main, null, false);

//create the drawer and remember the `Drawer` result object
        result = new DrawerBuilder()
                .withActivity(this).withCloseOnClick(true).withSelectedItemByPosition(-1)
                .withDrawerGravity(Gravity.LEFT)
                .withHeader(myLayout)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Home").withSelectable(false),
                        new PrimaryDrawerItem().withName("My Profile").withSelectable(false),
                        new PrimaryDrawerItem().withName("Change Password").withSelectable(false),
                        new PrimaryDrawerItem().withName("Cart").withSelectable(false),
                        new PrimaryDrawerItem().withName("MyOrder").withSelectable(false),
                        new PrimaryDrawerItem().withName("Logout").withSelectable(false)

                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (position == 1) {
                            if (getActivity() instanceof HomeActivity) {
                                hideMenu(true);
                            } else {
                                Intent intent = new Intent(getActivity(),
                                        HomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(intent);
                                hideMenu(false);
                                finishActivity();
                            }
                        } else if (position == 2) {
                            if (getActivity() instanceof MyProfileActivity) {
                                hideMenu(true);
                            } else {
                                Intent intent = new Intent(getActivity(),
                                        MyProfileActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivityForResult(intent, 100);
                                hideMenu(false);
                                finishActivity();
                            }
                        } else if (position == 3) {
                            if (getActivity() instanceof ChangePasswordActivity) {
                                hideMenu(true);
                            } else {
                                Intent intent = new Intent(getActivity(),
                                        ChangePasswordActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(intent);
                                hideMenu(false);
                                finishActivity();
                            }
                        } else if (position == 4) {
                            if (getActivity() instanceof CartActivity) {
                                hideMenu(true);
                            } else {
                                Intent intent = new Intent(getActivity(),
                                        CartActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(intent);
                                hideMenu(false);
                                finishActivity();
                            }
                        } else if (position == 5) {
                            if (getActivity() instanceof MyOrderActivity) {
                                hideMenu(true);
                            } else {
                                Intent intent = new Intent(getActivity(),
                                        MyOrderActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(intent);
                                hideMenu(false);
                                finishActivity();
                            }
                        } else if (position == 6) {
                            confirmLogout();
                        }
                        return true;
                    }
                })
                .build();

//      if initDrawer function called from anyactivity then only show imgmenu button and cart button
        ImageView imgMenu = (ImageView) findViewById(R.id.imgMenu);
        ImageView imgCart = (ImageView) findViewById(R.id.imgCart);
        if (imgCart != null) {
            imgCart.setVisibility(View.VISIBLE);
            imgCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getActivity() instanceof CartActivity) {
                        hideMenu(true);
                    } else {
                        Intent intent = new Intent(getActivity(),
                                CartActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        hideMenu(false);
                        finishActivity();
                    }
                }
            });
        }
        if (imgMenu != null) {
            imgMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (result.isDrawerOpen()) {
                        result.closeDrawer();
                    } else {
                        result.openDrawer();
                    }
                }
            });
            imgMenu.setVisibility(View.VISIBLE);
        }

//      refresh profile data from preferences
        fillProfileData(myLayout);
    }

    //  if initback is true then only show back button
    public void initBack(boolean b) {
        ImageView imgBack = (ImageView) findViewById(R.id.imgback);
        if (imgBack != null) {
            if (b) {
                imgBack.setVisibility(View.VISIBLE);
                imgBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onBackPressed();
                    }
                });
            } else {
                imgBack.setVisibility(View.GONE);
            }
        }
    }

    //  fill profile data on drawer
    private void fillProfileData(View myLayout) {
        Login userData = Utils.getLoginDetails(getActivity());
        TextView tvMenuFullName = (TextView) myLayout.findViewById(R.id.tvMenuFullName);
        TextView tvUserEmail = (TextView) myLayout.findViewById(R.id.tvUserEmail);
        tvMenuFullName.setText(userData.message.username);
        tvUserEmail.setText(userData.message.emailAddress);
    }

    //  confirm logout popup
    private void confirmLogout() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity())
                .title(R.string.logout_title)
                .content(R.string.logout_msg)
                .positiveText(R.string.btn_yes)
                .negativeText(R.string.btn_no)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                        showToast("Logged Out", Toast.LENGTH_SHORT);
//                      clear all login details from pref and redirect to loginactivity
                        Utils.clearLoginCredetials(getActivity());
                        Intent i = new Intent(getActivity(), LoginActivity.class);
                        startActivity(i);
                        finishAffinity();
                    }
                }).onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                        dismissDialog();
                    }
                });

        MaterialDialog dialog = builder.build();
        dialog.show();
    }

    private void hideMenu(boolean b) {
        try {
//            if (b)
            result.closeDrawer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void finishActivity() {
        if ((getActivity() instanceof HomeActivity)) {

        } else {
            getActivity().finish();
        }
    }

    ImageLoader imageLoader;

    public BaseActivity getActivity() {
        return this;
    }

    private TextView tvTitleText;

    public void setTitleText(String text) {
        try {

            if (tvTitleText == null)
                tvTitleText = (TextView) findViewById(R.id.tvTitleText);
            tvTitleText.setText(text);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showDialog(String msg) {
        try {
            if (ad != null && ad.isShowing()) {
                return;
            }

            ad = AsyncProgressDialog.getInstant(getActivity());
            ad.show(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMessage(String msg) {
        try {
            ad.setMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dismissDialog() {
        try {
            if (ad != null) {
                ad.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean handled = super.onKeyDown(keyCode, event);

        // Eat the long press event so the keyboard doesn't come up.
        if (keyCode == KeyEvent.KEYCODE_MENU && event.isLongPress()) {
            return true;
        }

        return handled;
    }

    Toast toast;

    public void showToast(final String text, final int duration) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                toast.setText(text);
                toast.setDuration(duration);
                toast.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        try {
            if (result.isDrawerOpen()) {
                result.closeDrawer();
            } else {
                super.onBackPressed();
            }
        } catch (Exception e) {
            super.onBackPressed();
        }
    }
}
package com.devband.tronwalletforandroid.common;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.devband.tronwalletforandroid.tron.AccountManager;
import com.devband.tronwalletforandroid.tron.WalletManager;
import com.devband.tronwalletforandroid.ui.intro.IntroActivity;
import com.devband.tronwalletforandroid.ui.login.LoginActivity;
import com.devband.tronwalletforandroid.ui.mvp.BasePresenter;

public class CommonActivity extends AppCompatActivity {

    private static final int EXTERNAL_STORAGE_PERMISSION_REQ = 9293;
    private static final int CAMERA_PERMISSION_REQ = 9929;

    protected BasePresenter mPresenter;

    protected MaterialDialog mMaterialDialog;

    protected void startActivity(Class<? extends Activity> activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

    protected void checkCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                } else {
                    requestPermissions(new String[] { Manifest.permission.CAMERA },
                            CAMERA_PERMISSION_REQ);
                }
            }
        }
    }

    protected void checkStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                } else {
                    requestPermissions(new String[] { Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE },
                            EXTERNAL_STORAGE_PERMISSION_REQ);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (checkAllPermissionGranted(grantResults)) {
            if (requestCode == EXTERNAL_STORAGE_PERMISSION_REQ) {

            } else if (requestCode == CAMERA_PERMISSION_REQ) {

            }
        } else {
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_CANCELED, returnIntent);
            finishActivity();
        }
    }

    private boolean checkAllPermissionGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }

        return true;
    }

    protected void checkLogin() {
        if (!AccountManager.getInstance(CommonActivity.this).isLogin()) {
            startActivity(IntroActivity.class);
            finishActivity();
        }
    }

    @Override
    protected void onDestroy() {
        hideDialog();
        super.onDestroy();
    }

    protected void hideDialog() {
        if (mMaterialDialog != null) {
            mMaterialDialog.dismiss();
            mMaterialDialog = null;
        }
    }

    public void finishActivity() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        } else {
            finish();
        }
    }

    public void showProgressDialog(@Nullable String title, @NonNull String msg) {
        hideDialog();

        MaterialDialog.Builder builder = new MaterialDialog.Builder(this);
        if (!TextUtils.isEmpty(title)) {
            builder.title(title);
        }

        mMaterialDialog = builder
                .content(msg)
                .progress(true,0)
                .show();
    }
}

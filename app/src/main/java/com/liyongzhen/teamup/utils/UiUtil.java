package com.liyongzhen.teamup.utils;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.liyongzhen.teamup.TeamUpApplication;

public class UiUtil {

    private static Toast mToast;

    public static void hideKeyboard(Context context, EditText view) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showShortToast(String message) {
        if (mToast == null) {
            mToast = Toast.makeText(TeamUpApplication.getSharedApplication(), message, Toast.LENGTH_SHORT);
        }
        mToast.setText(message);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }

    public static void showLongToast(String message) {
        if (mToast == null) {
            mToast = Toast.makeText(TeamUpApplication.getSharedApplication(), message, Toast.LENGTH_LONG);
        }
        mToast.setText(message);
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.show();
    }

    public static void showMessageDialog(Context context, String title,
                                         String message, String btnTitle) {
        Builder builder = new Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(btnTitle, null);
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }


    public static void showSingleChoiceDialog(Context context, String title,
                                              String[] data, int checkedItem,
                                              DialogInterface.OnClickListener listener) {
        Builder builder = new Builder(context);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        builder.setSingleChoiceItems(data, checkedItem, listener);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void setDrawable(View view, Drawable drawable) {
        int sdk = Build.VERSION.SDK_INT;
        if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackgroundDrawable(drawable);
        } else {
            view.setBackground(drawable);
        }
    }
}

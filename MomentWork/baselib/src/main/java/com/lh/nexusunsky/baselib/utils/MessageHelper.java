package com.lh.nexusunsky.baselib.utils;

import android.view.Gravity;
import android.widget.Toast;

import com.lh.nexusunsky.baselib.base.context.AppContext;

/**
 * @author Nexusunsky
 */
public class MessageHelper {

    public static void showMessage(String message) {
        Toast.makeText(AppContext.getAppContext(), message, Toast.LENGTH_LONG).show();
    }

    public static void showMessage(int messageResId) {
        Toast.makeText(AppContext.getAppContext(), messageResId, Toast.LENGTH_LONG).show();
    }

    public static void showMessage(String message, int duration) {
        Toast.makeText(AppContext.getAppContext(), message, duration).show();
    }

    public static void showMessageForShort(String message) {
        Toast.makeText(AppContext.getAppContext(), message, Toast.LENGTH_SHORT).show();
    }

    public static void showMessageInCenter(String message) {
        Toast toast = Toast.makeText(AppContext.getAppContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 10);
        toast.show();
    }
}

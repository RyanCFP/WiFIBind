package com.idx.wifibind.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by ryan on 18-3-2.
 * Email: Ryan_chan01212@yeah.net
 */

public class ToastUtils {
    public static Toast mToast = null;

    public static void showMessage(Context context,String message){
        mToast = mToast != null ? mToast :Toast.makeText(context,message,Toast.LENGTH_SHORT);
        mToast.show();
    }

    public static void showError(Context context,String error){
        mToast = mToast != null ? mToast :Toast.makeText(context,error,Toast.LENGTH_SHORT);
        mToast.show();
    }

    public static void release(){
        mToast = null;
    }
}

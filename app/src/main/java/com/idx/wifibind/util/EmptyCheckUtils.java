package com.idx.wifibind.util;

import android.text.TextUtils;

/**
 * Created by ryan on 18-3-3.
 * Email: Ryan_chan01212@yeah.net
 */

public class EmptyCheckUtils {

    public static boolean isEmptyOrNull(String key){
        if (key == null){
            return false;
        }
        if (TextUtils.isEmpty(key)){
            return false;
        }
        if (key.equals("")){
            return false;
        }
        return true;
    }
}

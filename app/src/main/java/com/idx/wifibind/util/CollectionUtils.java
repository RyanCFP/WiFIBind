package com.idx.wifibind.util;

/**
 * Created by ryan on 18-3-2.
 * Email: Ryan_chan01212@yeah.net
 */

import java.util.Collection;

public class CollectionUtils {
    /**
     * 判断集合是否为null or Empty
     * @parama
     * @return boolean
     * @error
     */
    public static boolean isNullOrEmpty(Collection c){
        if (c == null || c.isEmpty()){
            return true;
        }
        return false;
    }
}

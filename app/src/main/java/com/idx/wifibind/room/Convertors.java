package com.ryan.wifibind.room;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * Created by ryan on 18-3-5.
 * Email: Ryan_chan01212@yeah.net
 */

public class Convertors {
    //room database can recognize long but can't known Date
    //Room已经知道如何保存Long对象，所以可以使用这个转换器来保存类型Date的值。@TypeConverters的使用
    @TypeConverter
    public static Date fromTimeStamp(long valus){
        return Long.valueOf(valus) == null ? null : new Date(valus);
    }

    @TypeConverter
    public static long dateToTimeStamp(Date date){
        return date == null ? null : date.getTime();
    }
}

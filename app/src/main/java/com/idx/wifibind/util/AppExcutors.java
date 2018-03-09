package com.ryan.wifibind.util;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by ryan on 18-3-5.
 * Email: Ryan_chan01212@yeah.net
 */

public class AppExcutors {
    private static final int THREAD_COUNT = 3;
    public Executor mDiskIOExcuotr;
    public Executor mNetworkExcutor;
    public Executor mMainThreadExcutor;

    public AppExcutors(DiskIOExcutors mDiskIOExcuotr,
                       Executor mNetworkExcutor,MainThreadExecutor mMainThreadExcutor){

        this.mDiskIOExcuotr = mDiskIOExcuotr;
        this.mNetworkExcutor = mNetworkExcutor;
        this.mMainThreadExcutor = mMainThreadExcutor;
    }

    public AppExcutors(){
        this(new DiskIOExcutors(), Executors.newFixedThreadPool(THREAD_COUNT),new MainThreadExecutor());
    }

    public Executor getDiskIO(){
        return mDiskIOExcuotr;
    }

    public Executor getNetworkIO(){
        return mNetworkExcutor;
    }

    public Executor getMainIO(){
        return mMainThreadExcutor;
    }

    public static class MainThreadExecutor implements Executor{
        private Handler mHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mHandler.post(command);
        }
    }
}

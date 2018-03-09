package com.ryan.wifibind.util;

import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by ryan on 18-3-5.
 * Email: Ryan_chan01212@yeah.net
 */

public class DiskIOExcutors implements Executor{
    private final Executor mDiskIO;
    public DiskIOExcutors(){
        this.mDiskIO = Executors.newSingleThreadExecutor();
    }

    @Override
    public void execute(@NonNull Runnable command) {
        this.mDiskIO.execute(command);
    }
}

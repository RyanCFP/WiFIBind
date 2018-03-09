package com.idx.wifibind.util;


import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by ryan on 18-2-28.
 * Email: Ryan_chan01212@yeah.net
 */

public class ActivityUtils {

    public static void addFragmentInActivity(@NonNull FragmentManager fragmentManager,
                                   @NonNull Fragment fragment, int containerId){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(containerId,fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public static void replaceFragmentInActivity(@NonNull FragmentManager fragmentManager,
                                       @NonNull Fragment fragment, int containerId){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(containerId,fragment);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public static void removeFragmentInActivity(@NonNull FragmentManager fragmentManager,
                                                @NonNull Fragment fragment){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public static void showFragmentInActivity(@NonNull FragmentManager fragmentManager,
                                              @NonNull Fragment fragment){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.show(fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public static void hideFragmentInActivity(@NonNull FragmentManager fragmentManager,
                                              @NonNull Fragment fragment){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }
}

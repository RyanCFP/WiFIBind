package com.idx.wifibind;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by ryan on 18-2-28.
 * Email: Ryan_chan01212@yeah.net
 */

public class BaseFragment extends Fragment {
    private static final String TAG = BaseFragment.class.getSimpleName();
    public static InputMethodManager mInputMethodManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mInputMethodManager == null) {
            mInputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        }
    }

    //当前输入法正在显示就隐藏，隐藏就显示
    public void toggleSoftInputstatus(){
        Log.i(TAG, "toggleSoftInputstatus: ");
        mInputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

}

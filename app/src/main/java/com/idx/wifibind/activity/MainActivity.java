package com.idx.wifibind.activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import com.idx.wifibind.BaseActivity;
import com.idx.wifibind.R;
import com.idx.wifibind.fragment.DeviceFragment;
import com.idx.wifibind.fragment.SettingFragment;
import com.idx.wifibind.util.GlobalConstant;
import com.idx.wifibind.util.ToastUtils;
import com.idx.wifibind.wifi.WiFiSupport;

/**
 * Created by ryan on 18-2-28.
 * Email: Ryan_chan01212@yeah.net
 */
public class MainActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = MainActivity.class.getSimpleName();
    private LinearLayout mTab_home;
    private LinearLayout mTab_setting;
    private FloatingActionButton mFloatingButton;
    private DeviceFragment mDeviceFragment;
    private SettingFragment mSettingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        selectTab(0);
    }

    private void initView() {
        Log.i(TAG, "initView: ");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFloatingButton = findViewById(R.id.fab);
        mFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {startBindActivity();}
        });

        mTab_home = findViewById(R.id.tab_home);
        mTab_setting = findViewById(R.id.tab_setting);
        mTab_home.setOnClickListener(this);
        mTab_setting.setOnClickListener(this);
    }


    public void startBindActivity(){
        startActivity(new Intent(MainActivity.this,WiFiBindActivity.class));
    }
    //权限是否授权，Wifi是否打开
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                Log.i(TAG, "onRequestPermissionsResult: ");
                boolean hasAllPermission = true;
                if (grantResults.length > 0) {
                    for (int grantResult : grantResults) {
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            hasAllPermission = false;   //判断用户是否同意获取权限
                            break;
                        }
                    }
                }

                //如果同意权限
                if (hasAllPermission) { //已经获取权限
                    mHasPermission = true;
                    if (WiFiSupport.isOpenWifi(MainActivity.this) && mHasPermission) { //wifi开关是开
                        mHasPermismissionAndWiFiOn = true;
                    } else { //wifi开关是关
                        ToastUtils.showMessage(MainActivity.this,"WIFI处于关闭状态");
                        mHasPermismissionAndWiFiOn = false;
                    }
                    mSharePrefUtils.saveWiFiPermissionAndOnOff(
                            GlobalConstant.WiFiPermissionAndOnOff.WIFI_OPEN_OR_CLOSE,mHasPermismissionAndWiFiOn);
                } else {  //用户不同意权限
                    mHasPermission = false;
                    mHasPermismissionAndWiFiOn = false;
                    ToastUtils.showMessage(MainActivity.this,"获取权限失败");
                }
                mSharePrefUtils.saveWiFiPermissionAndOnOff(
                        GlobalConstant.WiFiPermissionAndOnOff.WIFI_HAS_PERMISSION,mHasPermission);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.device_add) {
            startBindActivity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSharePrefUtils != null){
            mSharePrefUtils = null;
        }
        mHasPermission = false;
        mHasPermismissionAndWiFiOn = false;
        mDeviceFragment = null;
        mDeviceFragment = null;
    }

    @Override
    public void onClick(View v) {
        Log.i(TAG, "onClick: ");
        switch (v.getId()){
            case R.id.tab_home:
                selectTab(0);
                break;
            case R.id.tab_setting:
                selectTab(1);
                break;
            default:break;
        }
    }

    private void selectTab(int position) {
        Log.i(TAG, "selectTab: ");
        /**
         *@exep
         *重置Ｔab颜色至最初
         */
        resetBackground();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction trasaction = fragmentManager.beginTransaction();

        /**
         *@exep
         *hide() all the fragments
         *先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
         */
        hideFragments(trasaction);

        switch (position){
            case 0:
                updateBackground(mTab_home);
                if (mDeviceFragment!= null) {
                    trasaction.show(mDeviceFragment);
                }else {
                    mDeviceFragment = new DeviceFragment();
                    trasaction.add(R.id.device_content,mDeviceFragment);
                }
                break;
            case 1:
                updateBackground(mTab_setting);
                if (mSettingFragment != null) {
                    trasaction.show(mSettingFragment);
                }else {
                    mSettingFragment = new SettingFragment();
                    trasaction.add(R.id.device_content,mSettingFragment);
                }
                break;
            default:break;
        }
        trasaction.commitAllowingStateLoss();
    }

    public Fragment getmFragment(Fragment fragment){
        Log.i(TAG, "getmFragment: ");
        if(fragment instanceof DeviceFragment){
            return new DeviceFragment();
        }
        if(fragment instanceof SettingFragment){
            return new SettingFragment();
        }
        return null;
    }

    private void hideFragments(FragmentTransaction trasaction) {
        Log.i(TAG, "hideFragments: ");
        if(mDeviceFragment != null){
            trasaction.hide(mDeviceFragment);
        }
        if(mSettingFragment != null){
            trasaction.hide(mSettingFragment);
        }
    }

    public void updateBackground(LinearLayout ll){
        Log.i(TAG, "updateBackground: ");
        ll.setBackgroundColor(Color.argb(255,0,255,0));
    }

    public void resetBackground(){
        Log.i(TAG, "resetBackground: ");
        mTab_home.setBackgroundColor(Color.argb(255,121,121,0));
        mTab_setting.setBackgroundColor(Color.argb(255,240,171,78));
    }
}

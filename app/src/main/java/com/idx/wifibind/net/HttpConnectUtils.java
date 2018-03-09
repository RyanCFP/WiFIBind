package com.idx.wifibind.net;

import android.os.AsyncTask;

/**
 * Created by ryan on 18-3-1.
 * Email: Ryan_chan01212@yeah.net
 */

public class HttpConnectUtils extends AsyncTask<String,Integer,String> {
    private static final String TAG = HttpConnectUtils.class.getSimpleName();
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected String doInBackground(String... strings) {
        return null;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

    public void onDestroy(){

    }
}

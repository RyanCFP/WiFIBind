package com.idx.wifibind.net;

/**
 * Created by ryan on 18-3-1.
 * Email: Ryan_chan01212@yeah.net
 */

public interface ConnStatusListener {
    void requsetError();
    void connSuccess();
    void connError();
}

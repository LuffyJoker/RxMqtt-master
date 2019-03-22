package com.peng.rxmqttlib.api;

/**
 * create by Mr.Q on 2019/3/19.
 * 类介绍：
 */
public interface RxMqttCallback {
    void connectionLost(Throwable cause);

    void connectComplete(boolean reconnect, String serverURI);

    void deliveryComplete(RxMqttToken token);

}

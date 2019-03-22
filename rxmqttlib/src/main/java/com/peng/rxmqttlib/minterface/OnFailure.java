package com.peng.rxmqttlib.minterface;

import com.peng.rxmqttlib.api.RxMqttToken;

/**
 * create by Mr.Q on 2019/3/22.
 * 类介绍：
 */
public interface OnFailure {
    void failure(RxMqttToken rxMqttToken, Throwable throwable);
}

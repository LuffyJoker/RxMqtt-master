package com.peng.rxmqttlib.minterface;

/**
 * create by Mr.Q on 2019/3/21.
 * 类介绍：
 */
public interface OnConnectionLost {
    void connectLost(Throwable throwable);
}

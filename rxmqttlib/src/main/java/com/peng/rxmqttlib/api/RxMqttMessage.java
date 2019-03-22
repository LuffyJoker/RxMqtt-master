package com.peng.rxmqttlib.api;

/**
 * create by Mr.Q on 2019/3/19.
 * 类介绍：
 */
public interface RxMqttMessage {
    String getTopic();

    int getId();

    byte[] getPayload();

    RxMqttQoS getQoS();

    boolean isRetained();
}

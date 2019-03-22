package com.peng.rxmqttlib.api;

import org.eclipse.paho.client.mqttv3.MqttException;

/**
 * create by Mr.Q on 2019/3/19.
 * 类介绍：
 */
public interface RxMqttToken {
    String getClientId();

    boolean isComplete();

    String[] getTopics();

    int getMessageId();

    int[] getGrantedQos();

    boolean getSessionPresent();

    RxMqttMessage getMessage() throws MqttException;

    RxMqttException getException();
}

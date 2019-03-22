package com.peng.rxmqttlib.minterface;

import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * create by Mr.Q on 2019/3/22.
 * 类介绍：
 */
public interface OnMessageArrived {
    void messageArrived(String str, MqttMessage mqttMessage);
}

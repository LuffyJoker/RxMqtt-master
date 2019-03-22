package com.peng.rxmqttlib.mqttservice;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * create by Mr.Q on 2019/3/19.
 * 类介绍：
 */
class MqttDeliveryTokenAndroid extends MqttTokenAndroid
        implements IMqttDeliveryToken {

    // The message which is being tracked by this token
    private MqttMessage message;

    MqttDeliveryTokenAndroid(MqttAndroidClient client,
                             Object userContext, IMqttActionListener listener, MqttMessage message) {
        super(client, userContext, listener);
        this.message = message;
    }

    /**
     * @see IMqttDeliveryToken#getMessage()
     */
    @Override
    public MqttMessage getMessage() throws MqttException {
        return message;
    }

    void setMessage(MqttMessage message) {
        this.message = message;
    }

    void notifyDelivery(MqttMessage delivered) {
        message = delivered;
        super.notifyComplete();
    }

}


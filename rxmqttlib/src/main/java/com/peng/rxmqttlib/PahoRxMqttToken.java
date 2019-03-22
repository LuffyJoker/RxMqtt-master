package com.peng.rxmqttlib;

import com.peng.rxmqttlib.api.RxMqttException;
import com.peng.rxmqttlib.api.RxMqttMessage;
import com.peng.rxmqttlib.api.RxMqttToken;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 * create by Mr.Q on 2019/3/21.
 * 类介绍：
 */
public class PahoRxMqttToken implements RxMqttToken {

    private IMqttToken token;

    public PahoRxMqttToken(IMqttToken token) {
        this.token = token;
    }

    @Override
    public String getClientId() {
        return token.getClient().getClientId();
    }

    @Override
    public boolean isComplete() {
        return token.isComplete();
    }

    @Override
    public String[] getTopics() {
        return token.getTopics();
    }

    @Override
    public int getMessageId() {
        return token.getMessageId();
    }

    @Override
    public int[] getGrantedQos() {
        return token.getGrantedQos();
    }

    @Override
    public boolean getSessionPresent() {
        return token.getSessionPresent();
    }

    @Override
    public RxMqttMessage getMessage() throws MqttException {
        if (token instanceof IMqttDeliveryToken) {
            IMqttDeliveryToken tempToken = (IMqttDeliveryToken) token;
            if (tempToken.getMessage() != null) {
                return PahoRxMqttMessage.create(tempToken.getMessage(), null);
            }
        }
        return null;
    }

    @Override
    public RxMqttException getException() {
        if(token.getException() != null){
            return new PahoRxMqttException(token);
        }
        return null;
    }
}

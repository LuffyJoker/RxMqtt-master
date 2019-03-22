package com.peng.rxmqttlib;

import com.peng.rxmqttlib.api.RxMqttCallback;
import com.peng.rxmqttlib.api.RxMqttToken;
import com.peng.rxmqttlib.minterface.OnComplete;
import com.peng.rxmqttlib.minterface.OnConnectionLost;
import com.peng.rxmqttlib.minterface.OnConnectComplete;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * create by Mr.Q on 2019/3/20.
 * 类介绍：
 */
public abstract class PahoRxMqttCallback implements RxMqttCallback, MqttCallbackExtended {

    public PahoRxMqttCallback create(OnConnectionLost onConnectionLost,
                                     OnConnectComplete onConnectComplete,
                                     OnComplete onDeliveryComplete) {
        return new PahoRxMqttCallbackImpl(onConnectionLost, onConnectComplete, onDeliveryComplete);
    }

    private class PahoRxMqttCallbackImpl extends PahoRxMqttCallback{
        private OnConnectionLost onConnectionLost;
        private OnConnectComplete onConnectComplete;
        private OnComplete onDeliveryComplete;

        public PahoRxMqttCallbackImpl(OnConnectionLost onConnectionLost, OnConnectComplete onConnectComplete, OnComplete onDeliveryComplete) {
            this.onConnectionLost = onConnectionLost;
            this.onConnectComplete = onConnectComplete;
            this.onDeliveryComplete = onDeliveryComplete;
        }


        @Override
        public void connectionLost(Throwable cause) {
            onConnectionLost.connectLost(cause);
        }

        @Override
        public void connectComplete(boolean reconnect, String serverURI) {
            onConnectComplete.connectComplete(reconnect,serverURI);
        }

        @Override
        public void deliveryComplete(RxMqttToken token) {
            onDeliveryComplete.complete(token);
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        deliveryComplete(new PahoRxMqttToken(token));
    }
}

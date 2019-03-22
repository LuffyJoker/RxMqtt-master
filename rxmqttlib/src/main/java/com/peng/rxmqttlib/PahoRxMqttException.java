package com.peng.rxmqttlib;

import com.peng.rxmqttlib.api.RxMqttException;

import org.eclipse.paho.client.mqttv3.IMqttToken;

/**
 * create by Mr.Q on 2019/3/21.
 * 类介绍：
 */
public class PahoRxMqttException extends RxMqttException {

    private static final long serialVersionUID = 4713713128270173625L;

    public PahoRxMqttException(String message, Throwable cause, IMqttToken token){
        super(message,cause);
    }

    public PahoRxMqttException(String message, Throwable cause) {
        super(message, cause);
    }

    public PahoRxMqttException(IMqttToken token){
        this(token.getException(), token);
    }

    public PahoRxMqttException(Throwable cause, IMqttToken token){
        this(cause.getMessage(), cause, token);
    }
}

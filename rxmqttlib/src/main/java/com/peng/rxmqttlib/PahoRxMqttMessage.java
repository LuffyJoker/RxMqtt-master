package com.peng.rxmqttlib;

import com.peng.rxmqttlib.api.RxMqttMessage;
import com.peng.rxmqttlib.api.RxMqttQoS;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * create by Mr.Q on 2019/3/21.
 * 类介绍：
 */
public class PahoRxMqttMessage implements RxMqttMessage {

    private MqttMessage mqttMessage;
    private String topic;

    public PahoRxMqttMessage(MqttMessage mqttMessage, String topic) {
        this.mqttMessage = mqttMessage;
        this.topic = topic;
    }

    public PahoRxMqttMessage(byte[] payload, RxMqttQoS qos, boolean retained, String topic) {
        mqttMessage = new MqttMessage(payload);
        mqttMessage.setQos(qos.getValue());
        mqttMessage.setRetained(retained);
        this.topic = topic;
    }

    public static PahoRxMqttMessage create(MqttMessage message, String topic) {
        return new PahoRxMqttMessage(message, topic);
    }


    public static PahoRxMqttMessage create(String payload) {
        return new PahoRxMqttMessage(payload.getBytes(StandardCharsets.UTF_8), RxMqttQoS.EXACTLY_ONCE, false, null);
    }

    public static PahoRxMqttMessage create(String payload,
                                           Charset charset,//StandardCharsets.UTF_8,
                                           RxMqttQoS qos,//RxMqttQoS.EXACTLY_ONCE,
                                           boolean retained,//false,
                                           String topic) {
        return new PahoRxMqttMessage(payload.getBytes(charset), qos, retained, topic);
    }

    public static PahoRxMqttMessage create(byte[] payload,
                                           RxMqttQoS qos,//=RxMqttQoS.EXACTLY_ONCE,
                                           boolean retained,//false,
                                           String topic) {
        return new PahoRxMqttMessage(payload, qos, retained, topic);
    }

    @Override
    public String getTopic() {
        return topic;
    }

    @Override
    public int getId() {
        return mqttMessage.getId();
    }

    @Override
    public byte[] getPayload() {
        return mqttMessage.getPayload();
    }

    @Override
    public RxMqttQoS getQoS() {
        return RxMqttQoS.values()[mqttMessage.getQos()];
    }

    @Override
    public boolean isRetained() {
        return mqttMessage.isRetained();
    }

    @Override
    public String toString() {
        return mqttMessage.toString();
    }
}

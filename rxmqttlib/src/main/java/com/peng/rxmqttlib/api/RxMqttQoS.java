package com.peng.rxmqttlib.api;

/**
 * create by Mr.Q on 2019/3/19.
 * 类介绍：
 */
public enum RxMqttQoS {

    /**
     * RxMqttQoS 0 - At most once delivery: With this setting, messages are delivered according to the
     * best effort of the underlying network. A response is not expected and no retry semantics are
     * defined in the protocol. This is the least level of Quality of Service and from a performance
     * perspective, adds value as it’s the fastest way to send a message using MQTT. A RxMqttQoS 0
     * message can get lost if the client unexpectedly disconnects or if the server fails.
     */
    AT_MOST_ONCE(0),

    /**
     * RxMqttQoS 1 - At least Once Delivery: For this level of service, the MQTT client or the server
     * would attempt to deliver the message at-least once. But there can be a duplicate message.
     */
    AT_LEAST_ONCE(1),

    /**
     * RxMqttQoS 2 - Exactly once delivery: This is the highest level of Quality of Service.
     * Additional protocol flows ensure that duplicate messages are not delivered to the receiving
     * application. The message is delivered once and only once when RxMqttQoS 2 is used.
     */
    EXACTLY_ONCE(2);

    private int value;

    RxMqttQoS(int value){
        this.value = value;
    }

    public int getValue(){
        return value;
    }
}

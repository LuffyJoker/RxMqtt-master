package com.peng.rxmqttlib.api;

import com.peng.rxmqttlib.minterface.OnComplete;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * create by Mr.Q on 2019/3/19.
 * 类介绍：
 */
public interface RxMqttClient {

    /**
     * 获取ClientId
     */
    Single<String> getClientId();

    /**
     * 获取ServerUri
     */
    Single<String> getServerUri();

    /**
     * 建立连接，一个AndroidApp，强烈建议只建立一个连接，订阅不同的 topic 以满足业务需求
     */
    Flowable<RxMqttToken> connect();

    /**
     * 是否已建立连接
     */
    Single<Boolean> isConnected();

    /**
     * 订阅topic
     */
    Flowable<RxMqttMessage> on(
            String topic,
            RxMqttQoS qos, // RxMqttQoS.EXACTLY_ONCE
            BackpressureStrategy strategy, // null
            OnComplete doOnComplete
    );

    /**
     * 订阅topic
     */
    Flowable<RxMqttMessage> on(
            String[] topics,
            RxMqttQoS[] qos,// Array<RxMqttQoS> = Array(topics.size) { _ -> RxMqttQoS.EXACTLY_ONCE },
            BackpressureStrategy strategy,// null,
            OnComplete doOnComplete);

    /**
     * 建立连接，并开始订阅topic
     */
    Flowable<RxMqttMessage> connectAndOn(
            String topic,
            RxMqttQoS qos, // RxMqttQoS.EXACTLY_ONCE
            BackpressureStrategy strategy, // null
            OnComplete doOnSubscribed,
            OnComplete doOnConnected);

    /**
     * 建立连接，并开始订阅topics
     */
    Flowable<RxMqttMessage> connectAndOn(
            String[] topics,
            RxMqttQoS[] qos,//  = Array(topics.size) { _ -> RxMqttQoS.EXACTLY_ONCE },
            BackpressureStrategy strategy,// null
            OnComplete doOnSubscribed,
            OnComplete doOnConnected);

    /**
     * 发布Message
     */
    Single<RxMqttToken> publish(String topic, RxMqttMessage message);

    /**
     * 解除Subscription
     */
    Single<RxMqttToken> off(String[] topic);

    /**
     * 关闭Connection
     */
    Single<RxMqttToken> disconnect();

    /**
     * 关闭Client
     */
    Completable close();


    /**
     * 解除Subscription，关闭Connection，关闭Client
     */
    Completable offAndClose(String[] topics);

    /**
     * 关闭Connection，关闭Client
     */
    Completable disconnectAndClose();
}

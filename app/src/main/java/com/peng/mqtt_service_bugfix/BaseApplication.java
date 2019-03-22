package com.peng.mqtt_service_bugfix;

import android.app.Application;

import com.peng.rxmqttlib.PahoRxMqttClient;
import com.peng.rxmqttlib.mqttservice.MqttAndroidClient;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * create by Mr.Q on 2019/3/22.
 * 类介绍：
 */
public class BaseApplication extends Application {

    private PahoRxMqttClient rxMqttClient;
    private static BaseApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        String clientId = "rx-mqtt";
//        String brokerUri = "tcp://192.168.100.135:1883";
        String brokerUri = "tcp://iot.eclipse.org";
        MemoryPersistence clientPersistence = new MemoryPersistence();
        MqttAndroidClient mqttAsyncClient = new MqttAndroidClient(this, brokerUri, clientId, clientPersistence);

        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        // 设置Mqtt版本
        mqttConnectOptions.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
        // 设置清空Session，false表示服务器会保留客户端的连接记录，true表示每次以新的身份连接到服务器
        mqttConnectOptions.setCleanSession(true);
        // 设置会话心跳时间，单位为秒。客户端每隔10秒向服务端发送心跳包判断客户端是否在线
        mqttConnectOptions.setKeepAliveInterval(10);
        mqttConnectOptions.setUserName("1234567");
        mqttConnectOptions.setPassword("1234567:1234567".toCharArray());
        mqttConnectOptions.setAutomaticReconnect(true);
        rxMqttClient = PahoRxMqttClient.builder(mqttAsyncClient).setConnectOptions(mqttConnectOptions).build();
    }

    public static BaseApplication getInstance(){
        return instance;
    }

    public PahoRxMqttClient getRxMqttClient() {
        return rxMqttClient;
    }
}

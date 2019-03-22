package com.peng.rxmqttlib.mqttservice;

import android.os.Binder;

/**
 * create by Mr.Q on 2019/3/19.
 * 类介绍：
 */
class MqttServiceBinder extends Binder {

    private MqttService mqttService;
    private String activityToken;

    MqttServiceBinder(MqttService mqttService) {
        this.mqttService = mqttService;
    }

    /**
     * @return a reference to the Service
     */
    public MqttService getService() {
        return mqttService;
    }

    void setActivityToken(String activityToken) {
        this.activityToken = activityToken;
    }

    /**
     * @return the activityToken provided when the Service was started
     */
    public String getActivityToken() {
        return activityToken;
    }

}

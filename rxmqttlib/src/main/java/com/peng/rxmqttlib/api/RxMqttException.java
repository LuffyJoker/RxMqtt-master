package com.peng.rxmqttlib.api;

/**
 * create by Mr.Q on 2019/3/19.
 * 类介绍：
 */
public class RxMqttException extends RuntimeException{

    public static long serialVersionUID = 6374001815972669318L;

    public RxMqttException(String message,Throwable cause){
        super(message,cause);
    }
}

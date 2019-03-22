package com.peng.rxmqttlib.minterface;

import com.peng.rxmqttlib.api.RxMqttToken;

/**
 * create by Mr.Q on 2019/3/21.
 * 类介绍：
 *      函数式接口，为了支持函数式编程
 */
public interface OnComplete {
    void complete(RxMqttToken rxMqttToken);
}

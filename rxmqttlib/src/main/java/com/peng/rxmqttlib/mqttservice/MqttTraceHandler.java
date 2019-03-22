package com.peng.rxmqttlib.mqttservice;

/**
 * create by Mr.Q on 2019/3/19.
 * 类介绍：
 */
public interface MqttTraceHandler {

    /**
     * Trace debugging information
     *
     * @param tag
     *            identifier for the source of the trace
     * @param message
     *            the text to be traced
     */
    void traceDebug(String tag, String message);

    /**
     * Trace error information
     *
     * @param tag
     *            identifier for the source of the trace
     * @param message
     *            the text to be traced
     */
    void traceError(String tag, String message);

    /**
     * trace exceptions
     *
     * @param tag
     *            identifier for the source of the trace
     * @param message
     *            the text to be traced
     * @param e
     *            the exception
     */
    void traceException(String tag, String message,
                        Exception e);

}
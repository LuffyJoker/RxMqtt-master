package com.peng.rxmqttlib;

import android.content.Context;

import com.peng.rxmqttlib.api.RxMqttClient;
import com.peng.rxmqttlib.api.RxMqttClientBuilder;
import com.peng.rxmqttlib.api.RxMqttMessage;
import com.peng.rxmqttlib.api.RxMqttQoS;
import com.peng.rxmqttlib.api.RxMqttToken;
import com.peng.rxmqttlib.minterface.OnComplete;
import com.peng.rxmqttlib.minterface.OnFailure;
import com.peng.rxmqttlib.minterface.OnMessageArrived;
import com.peng.rxmqttlib.mqttservice.MqttAndroidClient;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.reactivestreams.Publisher;

import java.util.concurrent.Callable;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.SingleSource;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;

/**
 * create by Mr.Q on 2019/3/21.
 * 类介绍：
 */
public class PahoRxMqttClient implements RxMqttClient {

    private IMqttAsyncClient client;

    private PahoRxMqttCallback callback;

    private MqttConnectOptions connectOptions;

    private BackpressureStrategy backpressureStrategy;

    private OnComplete doOnSubscribed = new OnComplete() {
        @Override
        public void complete(RxMqttToken rxMqttToken) {

        }
    };

    private OnComplete doOnConnected = new OnComplete() {
        @Override
        public void complete(RxMqttToken rxMqttToken) {

        }
    };


    public PahoRxMqttClient(Builder builder) {
        client = builder.getClient();
        callback = builder.getPahoCallback();
        connectOptions = builder.getConnectOptions();
        backpressureStrategy = builder.getBackpressureStrategy();
    }

    @Override
    public Single<String> getClientId() {
        return Single.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return client.getClientId();
            }
        });
    }

    @Override
    public Single<String> getServerUri() {
        return Single.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return client.getServerURI();
            }
        });
    }

    @Override
    public Flowable<RxMqttToken> connect() {
        return Flowable.create(new FlowableOnSubscribe() {
            @Override
            public void subscribe(final FlowableEmitter emitter) throws Exception {
                if (callback != null) {
                    client.setCallback(callback);
                }
                client.connect(connectOptions, null, newActionListener(emitter));
            }
        }, BackpressureStrategy.BUFFER);
    }

    @Override
    public Single<Boolean> isConnected() {
        return Single.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return client.isConnected();
            }
        });
    }

    @Override
    public Flowable<RxMqttMessage> on(String topic, RxMqttQoS qos, BackpressureStrategy strategy, OnComplete doOnComplete) {
        String[] topicArr = {topic};
        RxMqttQoS[] qosArr = {qos};
        if (strategy != null) {
            return on(topicArr, qosArr, strategy, doOnComplete);
        } else {
            return on(topicArr, qosArr, backpressureStrategy, doOnComplete);
        }
    }

    public Flowable<RxMqttMessage> on(String[] topic) {
        RxMqttQoS[] qosArr = new RxMqttQoS[topic.length];
        for (int i = 0; i < topic.length; i++) {
            qosArr[i] = RxMqttQoS.EXACTLY_ONCE;
        }
        return on(topic, qosArr, null, new OnComplete() {
            @Override
            public void complete(RxMqttToken rxMqttToken) {

            }
        });
    }


    @Override
    public Flowable<RxMqttMessage> on(final String[] topics, final RxMqttQoS[] qos, BackpressureStrategy strategy, final OnComplete doOnComplete) {

        if (strategy != null) {
            return Flowable.create(new FlowableOnSubscribe<RxMqttMessage>() {
                @Override
                public void subscribe(FlowableEmitter<RxMqttMessage> emitter) throws Exception {
                    int[] qosInt = new int[qos.length];
                    for (int i = 0; i < qos.length; i++) {
                        qosInt[i] = qos[i].getValue();
                    }

                    IMqttActionListener actionListener = newActionListener(emitter, doOnComplete);

                    IMqttMessageListener[] messageListeners = new IMqttMessageListener[topics.length];
                    for (int i = 0; i < topics.length; i++) {
                        messageListeners[i] = newMessageListener(emitter);
                    }
                    if (client.isConnected()) {
                        client.subscribe(topics, qosInt, null, actionListener, messageListeners);
                    } else {
                        emitter.onError(new PahoRxMqttException("unconnected", null));
                    }

                }
            }, strategy);
        } else {
            return Flowable.create(new FlowableOnSubscribe<RxMqttMessage>() {
                @Override
                public void subscribe(FlowableEmitter<RxMqttMessage> emitter) throws Exception {
                    int[] qosInt = new int[qos.length];
                    for (int i = 0; i < qos.length; i++) {
                        qosInt[i] = qos[i].getValue();
                    }

                    IMqttActionListener actionListener = newActionListener(emitter, doOnComplete);

                    IMqttMessageListener[] messageListeners = new IMqttMessageListener[topics.length];
                    for (int i = 0; i < topics.length; i++) {
                        messageListeners[i] = newMessageListener(emitter);
                    }
                    if (client.isConnected()) {
                        client.subscribe(topics, qosInt, null, actionListener, messageListeners);
                    } else {
                        emitter.onError(new PahoRxMqttException("unconnected", null));
                    }
                }
            }, backpressureStrategy);
        }
    }

    @Override
    public Flowable<RxMqttMessage> connectAndOn(final String topic, final RxMqttQoS qos, final BackpressureStrategy strategy, final OnComplete doOnSubscribed, final OnComplete doOnConnected) {

        return connect().flatMap(new Function<RxMqttToken, Publisher<RxMqttMessage>>() {
            @Override
            public Publisher<RxMqttMessage> apply(RxMqttToken rxMqttToken) throws Exception {
                doOnConnected.complete(rxMqttToken);
                return on(topic, qos, strategy, doOnSubscribed);
            }
        });
    }

    @Override
    public Flowable<RxMqttMessage> connectAndOn(
            final String[] topics,
            final RxMqttQoS[] qos,
            final BackpressureStrategy strategy,
            final OnComplete doOnSubscribed,
            final OnComplete doOnConnected) {
        return connect().flatMap(new Function<RxMqttToken, Publisher<RxMqttMessage>>() {
            @Override
            public Publisher<RxMqttMessage> apply(RxMqttToken rxMqttToken) throws Exception {
                doOnConnected.complete(rxMqttToken);
                return on(topics, qos, strategy, doOnSubscribed);
            }
        });
    }

    /**
     * 连接并订阅
     * @param topics
     * @return
     */
    public Flowable<RxMqttMessage> connectAndOn(final String[] topics) {

        RxMqttQoS[] qos = new RxMqttQoS[topics.length];
        for (int i = 0; i < qos.length; i++) {
            qos[i] = RxMqttQoS.EXACTLY_ONCE;
        }

        return connectAndOn(topics, qos, null, doOnSubscribed, doOnConnected);
    }

    /**
     * 连接并订阅
     * @param topics
     * @return
     */
    public Flowable<RxMqttMessage> connectAndOn(final String[] topics,OnComplete doOnSubscribed) {

        RxMqttQoS[] qos = new RxMqttQoS[topics.length];
        for (int i = 0; i < qos.length; i++) {
            qos[i] = RxMqttQoS.EXACTLY_ONCE;
        }

        return connectAndOn(topics, qos, null, doOnSubscribed, doOnConnected);
    }




    @Override
    public Single<RxMqttToken> publish(final String topic, final RxMqttMessage message) {
        return isConnected().flatMap(new Function<Boolean, SingleSource<RxMqttToken>>() {
            @Override
            public SingleSource<RxMqttToken> apply(Boolean aBoolean) throws Exception {
                if (!aBoolean) {
                    return Single.error(new PahoRxMqttException("unconnected", null));
                } else {
                    return Single.create(new SingleOnSubscribe<RxMqttToken>() {
                        @Override
                        public void subscribe(SingleEmitter<RxMqttToken> emitter) throws Exception {
                            client.publish(topic, message.getPayload(), message.getQoS().getValue(), message.isRetained(), null, newActionListener(emitter));
                        }
                    });
                }
            }
        });
    }

    @Override
    public Single<RxMqttToken> off(final String[] topic) {
        return isConnected().flatMap(new Function<Boolean, SingleSource<RxMqttToken>>() {
            @Override
            public SingleSource<RxMqttToken> apply(Boolean aBoolean) throws Exception {
                if (!aBoolean) {
                    return Single.error(new PahoRxMqttException("unconnected", null));
                } else {
                    return Single.create(new SingleOnSubscribe<RxMqttToken>() {
                        @Override
                        public void subscribe(SingleEmitter<RxMqttToken> emitter) throws Exception {
                            client.unsubscribe(
                                    topic,
                                    null,
                                    newActionListener(emitter)
                            );
                        }
                    });
                }
            }
        });
    }

    @Override
    public Single<RxMqttToken> disconnect() {
        return isConnected().flatMap(new Function<Boolean, SingleSource<RxMqttToken>>() {
            @Override
            public SingleSource<RxMqttToken> apply(Boolean aBoolean) throws Exception {
                if (!aBoolean) {
                    return Single.error(new PahoRxMqttException("unconnected", null));
                } else {
                    return Single.create(new SingleOnSubscribe<RxMqttToken>() {
                        @Override
                        public void subscribe(SingleEmitter<RxMqttToken> emitter) throws Exception {
                            client.disconnect(
                                    null,
                                    newActionListener(emitter)
                            );
                        }
                    });
                }
            }
        });
    }

    @Override
    public Completable close() {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                try {
                    client.close();
                } catch (MqttException me) {
                    throw new PahoRxMqttException(me, null);
                }
            }
        });
    }


    /**
     * 源码研究所需，所以不对外暴露
     */
    private Completable registerResources(final Context context) {
        if (client instanceof MqttAndroidClient) {
            final MqttAndroidClient mqttAndroidClient = (MqttAndroidClient) client;
            Completable.fromAction(new Action() {
                @Override
                public void run() throws Exception {
                    mqttAndroidClient.registerResources(context);
                }
            });
        }
        return Completable.complete();
    }

    /**
     * 不好控制，暂时不对外暴露
     * <p>
     * 当在activity中初次连接时，Receiver和Service都会被
     * client.unregisterResources()这个方法到底应该怎么用？！！client.registerResources()又该怎么用？！！
     * 黑人问号???
     * 不过我觉得应该是这样：
     * 使用applicationContext进行初始化和Service的绑定，
     * 然后activity的onResume()中执行client.registerResources(context)注册Receiver，
     * 然后activity的onPause()中执行client.unregisterResources()取消注册Receiver，[！！！实际上这儿是错误的，registerResources方法更改了Client里得myContext，然后unbindService就会抛出 service not registered 的异常]
     * 最后App退出，系统内部会解除Service的绑定，无需手动调用unbindService，即使调用也没用MqttAndroidClient#unregisterResources()中receiverRegistered为false，后续代码不会执行。
     * 综上所述，不要使用activity的Context初始化Client，不要用registerResources方法(会覆盖了application的context)
     */
    private Completable unregisterResources() {
        if (client instanceof MqttAndroidClient) {
            final MqttAndroidClient mqttAndroidClient = (MqttAndroidClient) client;
            return Completable.fromAction(new Action() {
                @Override
                public void run() throws Exception {
                    mqttAndroidClient.unregisterResources();
                }
            });
        }
        return Completable.complete();
    }

    /**
     * 不好控制，暂时不对外暴露
     * <p>
     * PahoRxMqttClient使用activity的context进行初始化，且执行了connect()时：注册了Receiver，start了Service，bind了Service
     * 因此需要调用unregisterResources执行Receiver的取消注册，Service的取消绑定。
     * 情景一：该方法在onStop中调用，点击Home键回到主页。该方法正确执行了释放资源的操作。
     * 情景二：该方法在onStop中调用，点击返回建退出App。此时会报Receiver和Service的泄漏异常，因为系统退出太快，等到执行unregisterResources方法时，unregisterReceiver和unbindService方法均已失效。
     * 总结：鉴于场景二导致的问题，不要使用activity的context进行初始化和连接！！！强烈建议在App中进行初始化！！！
     */
    private Completable offAndUnregisterResources(String[] topics) {
        final Completable close = close();
        Completable disconnect = disconnect().ignoreElement();
        Completable unregisterResources = unregisterResources();
        final Completable off = off(topics).ignoreElement().andThen(disconnect).andThen(unregisterResources);

        return isConnected().flatMapCompletable(new Function<Boolean, CompletableSource>() {
            @Override
            public CompletableSource apply(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    return off;
                } else {
                    return close;
                }
            }
        });
    }


    @Override
    public Completable offAndClose(String[] topics) {

        final Completable close = close();
        Completable disconnect = disconnect().ignoreElement();
        final Completable off = off(topics).ignoreElement().andThen(disconnect).andThen(close);
        return isConnected().flatMapCompletable(new Function<Boolean, CompletableSource>() {
            @Override
            public CompletableSource apply(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    return off;
                } else {
                    return close;
                }
            }
        });
    }

    @Override
    public Completable disconnectAndClose() {

        final Completable close = close();
        final Completable disconnect = disconnect().ignoreElement().andThen(close);

        return isConnected().flatMapCompletable(new Function<Boolean, CompletableSource>() {
            @Override
            public CompletableSource apply(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    return disconnect;
                } else {
                    return close;
                }
            }
        });
    }

    public static Builder builder(
            Context context,
            String brokerUri,
            String clientId,// = MqttAsyncClient.generateClientId(),
            MqttClientPersistence persistence// =MemoryPersistence()
    ) {
        return builder(new MqttAndroidClient(context, brokerUri, clientId, persistence));
    }

    public static Builder builder(IMqttAsyncClient client) {
        return new Builder(client);
    }

    public static class Builder implements RxMqttClientBuilder<PahoRxMqttClient> {

        PahoRxMqttCallback pahoCallback;

        MqttConnectOptions connectOptions = new MqttConnectOptions();

        BackpressureStrategy backpressureStrategy = BackpressureStrategy.BUFFER;

        IMqttAsyncClient client;

        public PahoRxMqttCallback getPahoCallback() {
            return pahoCallback;
        }

        public void setPahoCallback(PahoRxMqttCallback pahoCallback) {
            this.pahoCallback = pahoCallback;
        }

        public MqttConnectOptions getConnectOptions() {
            return connectOptions;
        }

        public BackpressureStrategy getBackpressureStrategy() {
            return backpressureStrategy;
        }

        public IMqttAsyncClient getClient() {
            return client;
        }

        public void setClient(IMqttAsyncClient client) {
            this.client = client;
        }

        public Builder(IMqttAsyncClient client) {
            this.client = client;
        }

        public Builder setCallbackListener(PahoRxMqttCallback callback) {
            this.pahoCallback = callback;
            return this;
        }

        public Builder setConnectOptions(MqttConnectOptions options) {
            this.connectOptions = options;
            return this;
        }

        public Builder setBackpressureStrategy(BackpressureStrategy backpressure) {
            this.backpressureStrategy = backpressure;
            return this;
        }

        @Override
        public PahoRxMqttClient build() {
            return new PahoRxMqttClient(this);
        }
    }

    private IMqttActionListener newActionListener(final FlowableEmitter<RxMqttMessage> emitter, OnComplete onSuccess) {

        OnFailure onFailure = new OnFailure() {
            @Override
            public void failure(RxMqttToken rxMqttToken, Throwable throwable) {
                if (!emitter.isCancelled()) {
                    emitter.onError(new PahoRxMqttException(throwable, null));
                }
            }
        };
        return newActionListener(onSuccess, onFailure);
    }

    private IMqttActionListener newActionListener(final FlowableEmitter<RxMqttToken> emitter) {
        OnComplete onSuccess = new OnComplete() {
            @Override
            public void complete(RxMqttToken rxMqttToken) {
                emitter.onNext(rxMqttToken);
            }
        };
        OnFailure onFailure = new OnFailure() {
            @Override
            public void failure(RxMqttToken rxMqttToken, Throwable throwable) {
                if (!emitter.isCancelled())
                    emitter.onError(new PahoRxMqttException(throwable, null));
            }
        };
        return newActionListener(onSuccess, onFailure);
    }

    private IMqttActionListener newActionListener(final SingleEmitter<RxMqttToken> emitter) {
        OnComplete onSuccess = new OnComplete() {
            @Override
            public void complete(RxMqttToken rxMqttToken) {
                emitter.onSuccess(rxMqttToken);
            }
        };

        OnFailure onFailure = new OnFailure() {
            @Override
            public void failure(RxMqttToken rxMqttToken, Throwable throwable) {
                if (!emitter.isDisposed())
                    emitter.onError(new PahoRxMqttException(throwable, null));
            }
        };
        return newActionListener(onSuccess, onFailure);
    }


    private IMqttActionListener newActionListener(OnComplete onSuccess, OnFailure onFailure) {
        return new PahoActionListener(onSuccess, onFailure);
    }

    private static IMqttMessageListener newMessageListener(final FlowableEmitter<RxMqttMessage> emitter) {
        return newMessageListener(new OnMessageArrived() {
            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) {
                emitter.onNext(PahoRxMqttMessage.create(mqttMessage, topic));
            }
        });
    }

    private static IMqttMessageListener newMessageListener(OnMessageArrived onMessageArrived) {
        return new PahoMessageListener(onMessageArrived);
    }

    private static class PahoActionListener implements IMqttActionListener {

        private OnComplete onSuccess;
        private OnFailure onFailure;

        public PahoActionListener(OnComplete onSuccess, OnFailure onFailure) {
            this.onSuccess = onSuccess;
            this.onFailure = onFailure;
        }

        @Override
        public void onSuccess(IMqttToken asyncActionToken) {
            onSuccess.complete(new PahoRxMqttToken(asyncActionToken));
        }

        @Override
        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
            onFailure.failure(new PahoRxMqttToken(asyncActionToken), exception);
        }
    }

    private static class PahoMessageListener implements IMqttMessageListener {

        private OnMessageArrived onMessageArrived;

        public PahoMessageListener(OnMessageArrived onMessageArrived) {
            this.onMessageArrived = onMessageArrived;
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            onMessageArrived.messageArrived(topic, message);
        }
    }

}

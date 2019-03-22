package com.peng.mqtt_service_bugfix;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.peng.rxmqttlib.PahoRxMqttMessage;
import com.peng.rxmqttlib.api.RxMqttMessage;
import com.peng.rxmqttlib.api.RxMqttToken;
import com.peng.rxmqttlib.minterface.OnComplete;

import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] strArr = new String[]{"test/1234567897/1", "test/1234567897/2"};

        BaseApplication.getInstance()
                .getRxMqttClient()
                .connectAndOn(strArr, new OnComplete() {
                    @Override
                    public void complete(RxMqttToken rxMqttToken) {
                        Log.d(TAG, "complete: 订阅成功");
                    }
                })
                .subscribe(new Consumer<RxMqttMessage>() {
                    @Override
                    public void accept(RxMqttMessage rxMqttMessage) throws Exception {
                        Log.d(TAG, "accept: " + rxMqttMessage);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "accept: 收到错误消息");
                        throwable.printStackTrace();
                    }
                });

        TextView tv_on_msg = findViewById(R.id.tv_on_msg);
        tv_on_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] strArr = new String[]{"12344/123456/print", "test/1234567897/4"};
                BaseApplication.getInstance()
                        .getRxMqttClient()
                        .on(strArr)
                        .subscribe(new Consumer<RxMqttMessage>() {
                            @Override
                            public void accept(RxMqttMessage rxMqttMessage) throws Exception {
                                Log.d(TAG, "accept: " + rxMqttMessage);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.d(TAG, "accept: 收到错误消息");
                                throwable.printStackTrace();
                            }
                        });
            }
        });

        TextView tv_send_msg = findViewById(R.id.tv_send_msg);
        tv_send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseApplication.getInstance()
                        .getRxMqttClient()
                        .publish("12344/123456/print", PahoRxMqttMessage.create("message"))
                        .subscribe(new Consumer<RxMqttToken>() {
                            @Override
                            public void accept(RxMqttToken rxMqttToken) throws Exception {
                                Log.d(TAG, "收到消息 accept: " + rxMqttToken.getMessageId());
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.d(TAG, "accept: " + throwable.getMessage() + "发送失败");
                            }
                        });
            }
        });
    }
}

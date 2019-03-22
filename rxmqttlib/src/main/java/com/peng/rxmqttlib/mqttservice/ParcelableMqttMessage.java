package com.peng.rxmqttlib.mqttservice;

import android.os.Parcel;
import android.os.Parcelable;

import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * create by Mr.Q on 2019/3/19.
 * 类介绍：
 */
public class ParcelableMqttMessage extends MqttMessage implements Parcelable {

    String messageId = null;

    ParcelableMqttMessage(MqttMessage original) {
        super(original.getPayload());
        setQos(original.getQos());
        setRetained(original.isRetained());
        setDuplicate(original.isDuplicate());
    }

    ParcelableMqttMessage(Parcel parcel) {
        super(parcel.createByteArray());
        setQos(parcel.readInt());
        boolean[] flags = parcel.createBooleanArray();
        setRetained(flags[0]);
        setDuplicate(flags[1]);
        messageId = parcel.readString();
    }

    /**
     * @return the messageId
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * Describes the contents of this object
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Writes the contents of this object to a parcel
     *
     * @param parcel
     *            The parcel to write the data to.
     * @param flags
     *            this parameter is ignored
     */
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeByteArray(getPayload());
        parcel.writeInt(getQos());
        parcel.writeBooleanArray(new boolean[]{isRetained(), isDuplicate()});
        parcel.writeString(messageId);
    }

    /**
     * A creator which creates the message object from a parcel
     */
    public static final Creator<ParcelableMqttMessage> CREATOR = new Creator<ParcelableMqttMessage>() {

        /**
         * Creates a message from the parcel object
         */
        @Override
        public ParcelableMqttMessage createFromParcel(Parcel parcel) {
            return new ParcelableMqttMessage(parcel);
        }

        /**
         * creates an array of type {@link ParcelableMqttMessage}[]
         *
         */
        @Override
        public ParcelableMqttMessage[] newArray(int size) {
            return new ParcelableMqttMessage[size];
        }
    };
}

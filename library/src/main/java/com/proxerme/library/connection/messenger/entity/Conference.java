package com.proxerme.library.connection.messenger.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import com.afollestad.bridge.annotations.Body;
import com.proxerme.library.interfaces.IdItem;
import com.proxerme.library.interfaces.ImageItem;
import com.proxerme.library.interfaces.TimeItem;

/**
 * TODO: Describe class
 *
 * @author Ruben Gees
 */

public class Conference implements Parcelable, IdItem, TimeItem, ImageItem {

    public static final Parcelable.Creator<Conference> CREATOR = new Parcelable.Creator<Conference>() {
        @Override
        public Conference createFromParcel(Parcel source) {
            return new Conference(source);
        }

        @Override
        public Conference[] newArray(int size) {
            return new Conference[size];
        }
    };

    @Body(name = "id")
    private String id;
    @Body(name = "topic")
    private String topic;
    @Body(name = "topic_custom")
    private String customTopic;
    @Body(name = "count")
    private int participantAmount;
    @Body(name = "image")
    private String image;
    @Body(name = "group")
    private boolean isGroup;
    @Body(name = "read")
    private boolean isRead;
    @Body(name = "timestamp_end")
    private long time;
    @Body(name = "read_count")
    private int unreadMessageAmount;
    @Body(name = "read_mid")
    private String lastReadMessageId;

    Conference() {
    }

    public Conference(@NonNull String id, @NonNull String topic, @NonNull String customTopic,
                      @IntRange(from = 2) int participantAmount, @NonNull String imageType,
                      @NonNull String imageId, boolean isGroup, boolean isRead, long time,
                      @IntRange(from = 0) int unreadMessageAmount,
                      @NonNull String lastReadMessageId) {
        this.id = id;
        this.topic = topic;
        this.customTopic = customTopic;
        this.participantAmount = participantAmount;
        this.image = imageType + ":" + imageId;
        this.isGroup = isGroup;
        this.isRead = isRead;
        this.time = time;
        this.unreadMessageAmount = unreadMessageAmount;
        this.lastReadMessageId = lastReadMessageId;
    }

    protected Conference(Parcel in) {
        this.lastReadMessageId = in.readString();
        this.image = in.readString();
        this.isRead = in.readByte() != 0;
        this.customTopic = in.readString();
        this.participantAmount = in.readInt();
        this.topic = in.readString();
        this.id = in.readString();
        this.time = in.readLong();
        this.isGroup = in.readByte() != 0;
        this.unreadMessageAmount = in.readInt();
    }

    @NonNull
    public String getLastReadMessageId() {
        return lastReadMessageId;
    }

    @NonNull
    public String getImageType() {
        String[] split = image.split(":");

        if (split.length == 2) {
            return split[0];
        } else {
            return "";
        }
    }

    @Override
    @NonNull
    public String getImageId() {
        String[] split = image.split(":");

        if (split.length == 2) {
            return split[1];
        } else {
            return "";
        }
    }

    public boolean isRead() {
        return isRead;
    }

    @NonNull
    public String getCustomTopic() {
        return customTopic;
    }

    @IntRange(from = 2)
    public int getParticipantAmount() {
        return participantAmount;
    }

    @NonNull
    public String getTopic() {
        return topic;
    }

    @Override
    @NonNull
    public String getId() {
        return id;
    }

    @Override
    public long getTime() {
        return time;
    }

    public boolean isGroup() {
        return isGroup;
    }

    @IntRange(from = 0)
    public int getUnreadMessageAmount() {
        return unreadMessageAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Conference that = (Conference) o;

        if (isRead != that.isRead) return false;
        if (participantAmount != that.participantAmount) return false;
        if (time != that.time) return false;
        if (isGroup != that.isGroup) return false;
        if (unreadMessageAmount != that.unreadMessageAmount) return false;
        if (!lastReadMessageId.equals(that.lastReadMessageId)) return false;
        if (!image.equals(that.image)) return false;
        if (!customTopic.equals(that.customTopic)) return false;
        if (!topic.equals(that.topic)) return false;
        return id.equals(that.id);

    }

    @Override
    public int hashCode() {
        int result = lastReadMessageId.hashCode();
        result = 31 * result + image.hashCode();
        result = 31 * result + (isRead ? 1 : 0);
        result = 31 * result + customTopic.hashCode();
        result = 31 * result + participantAmount;
        result = 31 * result + topic.hashCode();
        result = 31 * result + id.hashCode();
        result = 31 * result + (int) (time ^ (time >>> 32));
        result = 31 * result + (isGroup ? 1 : 0);
        result = 31 * result + unreadMessageAmount;
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.lastReadMessageId);
        dest.writeString(this.image);
        dest.writeByte(this.isRead ? (byte) 1 : (byte) 0);
        dest.writeString(this.customTopic);
        dest.writeInt(this.participantAmount);
        dest.writeString(this.topic);
        dest.writeString(this.id);
        dest.writeLong(this.time);
        dest.writeByte(this.isGroup ? (byte) 1 : (byte) 0);
        dest.writeInt(this.unreadMessageAmount);
    }
}

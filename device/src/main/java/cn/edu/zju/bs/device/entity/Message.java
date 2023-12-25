package cn.edu.zju.bs.device.entity;




import com.baomidou.mybatisplus.annotation.*;

import java.util.List;


public class Message {
    @TableId(value = "deviceId",type = IdType.INPUT)
    private int deviceId;
    //设备ID
    private String clientId;
    //上报信息
    private String info;
    //设备数据
    private int value;
    //是否告警，0-正常，1-告警
    private int alert;
    //设备位置，经度
    private double lng;
    //设备位置，纬度
    private double lat;
    //上报时间，ms,和deviceId一起作为主键
    @TableField(value = "timestamp", insertStrategy = FieldStrategy.IGNORED)
    private long timestamp;

    //上报时间，yyyy/MM/dd HH:mm:ss
    private String time;


    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getAlert() {
        return alert;
    }

    public void setAlert(int alert) {
        this.alert = alert;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}


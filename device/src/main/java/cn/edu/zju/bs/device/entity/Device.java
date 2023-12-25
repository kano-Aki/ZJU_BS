package cn.edu.zju.bs.device.entity;

import com.baomidou.mybatisplus.annotation.TableField;

import javax.xml.stream.events.DTD;

public class Device {
    private int id;
    private int userId;
    private String name;
    private String type;

    @TableField(exist = false)  //该注解表示这个成员不是表的列
    private String token;

    @Override
    public String toString() {
        return "Device{" +
                "id=" + id +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", token='" + token + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

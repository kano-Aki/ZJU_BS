package cn.edu.zju.bs.device.mqtt;

import lombok.Data;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.Vector;
@Data
public class IOTClient {
    int deviceId ;
    String mqttServer = "tcp://localhost:1883";
    String topic = "testapp";
    String clientPrefix = "device";
    private WorkerThread thread;


    public IOTClient(int deviceId){
        this.deviceId=deviceId;

        try{
            thread=new WorkerThread();
            thread.setDeviceId(this.deviceId);
            thread.setMqttServer(mqttServer);
            thread.setTopic(topic);
            thread.setClientPrefix(clientPrefix);
            thread.start();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void turnOff(){  //停止终端运行
        thread.setRunning(false);
    }
}

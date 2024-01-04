package cn.edu.zju.bs.device.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.Random;

public class IOTServer {
    private String mqttServer="tcp://broker-cn.emqx.io:1883";
    private String topic="testapp";
    private String clientId="server_4111";

    public IOTServer(){
        try {
            int qos = 2;
            int keepalive=20;
            MemoryPersistence persistence = new MemoryPersistence();
            MqttClient mqttClient = new MqttClient(mqttServer, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setKeepAliveInterval(keepalive);
            System.out.println("Server connecting to broker: " + mqttServer);
            mqttClient.connect(connOpts);
            System.out.println("Connected");

            mqttClient.subscribe(topic,qos);//订阅

            mqttClient.setCallback(new PushCallback());  //接收端注册一个回调函数，接收到消息之后处理
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}

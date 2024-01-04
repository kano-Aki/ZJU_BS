package cn.edu.zju.bs.device.mqtt;


import cn.edu.zju.bs.device.controller.DeviceController;
import cn.edu.zju.bs.device.entity.Message;
import cn.edu.zju.bs.device.mapper.MessageMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 主要用来接收和处理订阅主题的消息
 */
@Component
public class PushCallback implements MqttCallback {

    @Autowired
    public MessageMapper messageMapper;

    public  static PushCallback PushCallback ;

    @PostConstruct //通过@PostConstruct实现初始化bean之前进行的操作
    public void init() {
        System.out.println("初始化");
        PushCallback = this;
        PushCallback.messageMapper = this.messageMapper;
    }



    private static final Logger LOGGER = LoggerFactory.getLogger(PushCallback.class);



    public int insert(Message message){
        return messageMapper.insert(message);
    }

    public void connectionLost(Throwable cause) {
        // 连接丢失后，一般在这里面进行重连
        LOGGER.info("---------------------连接断开");
        try {
            int qos = 2;
            int keepalive=20;
            MemoryPersistence persistence = new MemoryPersistence();
            MqttClient mqttClient = new MqttClient("tcp://broker-cn.emqx.io:1883", "server_4111", persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setKeepAliveInterval(keepalive);
            System.out.println("Server connecting to broker: " + "tcp://broker-cn.emqx.io:1883");
            mqttClient.connect(connOpts);
            System.out.println("Connected");

            mqttClient.subscribe("testapp",qos);//订阅

            mqttClient.setCallback(new PushCallback());  //接收端注册一个回调函数，接收到消息之后处理
        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void deliveryComplete(IMqttDeliveryToken token) {    //发送消息，消息到达后处理方法
        System.out.println("deliveryComplete---------" + token.isComplete());
    }


    public void messageArrived(String topic, MqttMessage message) throws Exception {    //接收所订阅的主题的消息并处理
        // subscribe后得到的消息会执行到这里面
        String result = new String(message.getPayload(),"UTF-8");
//        System.out.println("接收消息主题 : " + topic);
//        System.out.println("接收消息Qos : " + message.getQos());
//        System.out.println("接收消息内容 : " + result);
        //这里可以针对收到的消息做处理，比如持久化
        try {
            JsonParser jp = new JsonParser();
            //将json字符串转化成json对象
            JsonObject jo = jp.parse(result).getAsJsonObject();
            //获取message对应的值
            int deviceId = jo.get("deviceId").getAsInt();
            String clientId = jo.get("clientId").getAsString();
            String info = jo.get("info").getAsString();
            int value = jo.get("value").getAsInt();
            int alert = jo.get("alert").getAsInt();
            double lng = jo.get("lng").getAsDouble();
            double lat = jo.get("lat").getAsDouble();
            String time = jo.get("time").getAsString();
            long timestamp=jo.get("timestamp").getAsLong();
            Message message1 = new Message();
            message1.setTime(time);
            message1.setAlert(alert);
            message1.setDeviceId(deviceId);
            message1.setInfo(info);
            message1.setClientId(clientId);
            message1.setLat(lat);
            message1.setLng(lng);
            message1.setValue(value);
            message1.setTimestamp(timestamp);
//        System.out.println("deviceId "+deviceId+"clientId "+clientId+"info "+info+"value "+value+"alert "+alert+"lng "+lng+"lat "+lat+"time "+time);
//        messageMapper.NewMessage(deviceId,clientId,info,value,alert,lng,lat,time);
            for(IOTClient client: DeviceController.clients){    //判断该设备是否还在运行，避免设备刚被删除，又有一条要插入的数据
                if(client.getDeviceId()==deviceId){
                    PushCallback.messageMapper.insert(message1);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}

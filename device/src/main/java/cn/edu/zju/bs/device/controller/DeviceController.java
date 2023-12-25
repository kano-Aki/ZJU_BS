package cn.edu.zju.bs.device.controller;
import cn.edu.zju.bs.device.entity.Device;
import cn.edu.zju.bs.device.entity.Message;
import cn.edu.zju.bs.device.entity.User;
import cn.edu.zju.bs.device.mapper.DeviceMapper;
import cn.edu.zju.bs.device.mapper.MessageMapper;
import cn.edu.zju.bs.device.mapper.UserMapper;
import cn.edu.zju.bs.device.mqtt.IOTClient;
import cn.edu.zju.bs.device.mqtt.IOTServer;
import cn.edu.zju.bs.device.utils.JwtUtils;
import cn.edu.zju.bs.device.utils.Result;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
@CrossOrigin    //使用默认的跨域通信配置

public class DeviceController {
    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MessageMapper messageMapper;

    public static List<IOTClient> clients;
    private IOTServer server;

    private int getUserId(String token){
        String username= JwtUtils.getClaimsByToken(token).getSubject();
        QueryWrapper<User> userQueryWrapper=new QueryWrapper<>();
        userQueryWrapper.eq("username",username);
        return userMapper.selectOne(userQueryWrapper).getId();
    }

    @GetMapping("/user/Navigation/homePage")
    Result HomePage(String token){
        int uid=getUserId(token);

        QueryWrapper<Device> deviceQueryWrapper=new QueryWrapper<>();
        deviceQueryWrapper.eq("userId",uid);
        List<Device> myDevices=deviceMapper.selectList(deviceQueryWrapper);//得到该用户的设备列表，以得到属于该用户的设备发出的信息
        int deviceCount=myDevices.size();

        QueryWrapper<Message> messageQueryWrapper=new QueryWrapper<>();
        int mesCount=0,alertCount=0;
        for(Device device:myDevices){
            messageQueryWrapper.clear();
            messageQueryWrapper.eq("deviceId",device.getId());
            mesCount+=messageMapper.selectCount(messageQueryWrapper);
            messageQueryWrapper.eq("alert",1);
            alertCount+=messageMapper.selectCount(messageQueryWrapper);
        }

        int data[]={deviceCount,mesCount,alertCount};

        return Result.ok().data("graphData",data);
    }

    @PostMapping("/user/Navigation/myDevice/newDevice")
    Result NewDevice(@RequestBody Device device){
        QueryWrapper<Device> deviceQueryWrapper=new QueryWrapper<>();
        deviceQueryWrapper.eq("name",device.getName());
        if(deviceMapper.selectCount(deviceQueryWrapper)!=0)
            return Result.error().message("name repeated");

        int uid=getUserId(device.getToken());
        device.setUserId(uid);

        deviceMapper.insert(device);
        deviceQueryWrapper.clear();
        deviceQueryWrapper.eq("name",device.getName());
        int id=deviceMapper.selectOne(deviceQueryWrapper).getId();
        IOTClient client=new IOTClient(id);
        clients.add(client);
        return Result.ok();
    }

    @PutMapping("/user/Navigation/myDevice/editDevice")
    Result EditDevice(@RequestBody Device device){
        int uid=getUserId(device.getToken());
        device.setUserId(uid);

        QueryWrapper<Device> deviceQueryWrapper=new QueryWrapper<>();
        deviceQueryWrapper.eq("name",device.getName());
        if(deviceMapper.selectCount(deviceQueryWrapper)!=0)
            return Result.error().message("name repeated");

        deviceQueryWrapper.clear();
        deviceQueryWrapper.eq("id",device.getId());
        int count=deviceMapper.update(device,deviceQueryWrapper);
        if(count==1) return Result.ok();
        else return Result.error();
    }

    @GetMapping("/user/Navigation/myDevice/devInformation")
    Result DevInformation(String token){
        int uid=getUserId(token);

        QueryWrapper<Device> deviceQueryWrapper=new QueryWrapper<>();
        deviceQueryWrapper.eq("userId",uid);
        List<Device> devices=deviceMapper.selectList(deviceQueryWrapper);
        return Result.ok().data("dev",devices);
    }

    @DeleteMapping("/user/Navigation/myDevice/delDevice")
    Result DeleteDevice(String token,int id){
        int uid=getUserId(token);
        QueryWrapper<Device> deviceQueryWrapper=new QueryWrapper<>();
        deviceQueryWrapper.eq("id",id);
        deviceQueryWrapper.eq("userId",uid);
        int count=deviceMapper.delete(deviceQueryWrapper);    //在数据库中删除，message设置级联删除

        if(count==1) {
            Iterator<IOTClient> iterator = clients.iterator();
            while (iterator.hasNext()) {
                IOTClient client = iterator.next();
                if (client.getDeviceId() == id) {
                    client.turnOff();   //停止运行
                    iterator.remove(); //从中删除
                }
            }
            return Result.ok();
        }
        else return Result.error();
    }


    @GetMapping("/init")    //启动数据库中记录的各device以及接受的client
    void Init(){
        if(clients==null){
            clients=new ArrayList<>();
            server=new IOTServer();
            List<Device> devices=deviceMapper.selectList(null);//得到所有设备
            for(Device device:devices){
                IOTClient client=new IOTClient(device.getId());
                clients.add(client);
            }
        }
    }

}

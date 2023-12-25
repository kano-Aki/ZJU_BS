package cn.edu.zju.bs.device.controller;

import cn.edu.zju.bs.device.entity.Device;
import cn.edu.zju.bs.device.entity.Message;
import cn.edu.zju.bs.device.entity.User;
import cn.edu.zju.bs.device.mapper.DeviceMapper;
import cn.edu.zju.bs.device.mapper.MessageMapper;
import cn.edu.zju.bs.device.mapper.UserMapper;
import cn.edu.zju.bs.device.utils.JwtUtils;
import cn.edu.zju.bs.device.utils.Result;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin    //使用默认的跨域通信配置
public class MessageController {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/user/Navigation/dataQuery")
    Result QueryMessage(String id,  String name,  String type,
                         String start_date,  String end_date,String token) {
        String username = JwtUtils.getClaimsByToken(token).getSubject();
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("username", username);
        int uid = userMapper.selectOne(userQueryWrapper).getId();
        Boolean idNotEmpty = !id.isEmpty();
        Boolean nameNotEmpty = !name.isEmpty();
        Boolean typeNotEmpty = !type.isEmpty();
        Boolean startNotEmpty = !start_date.isEmpty();

        if (idNotEmpty || nameNotEmpty) { //ID和name均能唯一确定设备，若不全为空
            QueryWrapper<Device> deviceQueryWrapper = new QueryWrapper<>();
            deviceQueryWrapper.eq("UserId", uid);//确保找到的消息属于该用户的设备
            if (idNotEmpty) deviceQueryWrapper.eq("id", Integer.parseInt(id));
            if (nameNotEmpty) deviceQueryWrapper.eq("name", name);
            if (typeNotEmpty) deviceQueryWrapper.eq("type", type);

            if (deviceMapper.selectCount(deviceQueryWrapper) == 0) {    //不存在这样的设备
                String data[] = {};
                return Result.ok().data("data", data);
            } else {
                QueryWrapper<Message> messageQueryWrapper = new QueryWrapper<>();
                if (idNotEmpty) messageQueryWrapper.eq("deviceId", Integer.parseInt(id));
                else {
                    int deviceid = deviceMapper.selectOne(deviceQueryWrapper).getId();
                    messageQueryWrapper.eq("deviceId", deviceid);
                }
                if (startNotEmpty) {  //若日期不为空
                    messageQueryWrapper.ge("time", start_date);
                    messageQueryWrapper.le("time", end_date);
                }
                List<Message> data = messageMapper.selectList(messageQueryWrapper);
                return Result.ok().data("data", data);
            }
        } else {
            QueryWrapper<Device> deviceQueryWrapper = new QueryWrapper<>();
            if (typeNotEmpty) deviceQueryWrapper.eq("type", type);//若type不为空
            deviceQueryWrapper.eq("userId", uid);
            List<Device> devices = deviceMapper.selectList(deviceQueryWrapper);
            List<Message> data = new ArrayList<>();

            for (Device device : devices) { //遍历deviceId，取出message
                QueryWrapper<Message> messageQueryWrapper = new QueryWrapper<>();
                messageQueryWrapper.eq("deviceId", device.getId());
                if (startNotEmpty) {  //若日期不为空
                    messageQueryWrapper.ge("time", start_date);
                    messageQueryWrapper.le("time", end_date);
                }
                data.addAll(messageMapper.selectList(messageQueryWrapper));
            }
            return Result.ok().data("data", data);
        }
    }

    @GetMapping("/user/Navigation/myDevice/devInformation/map")
    Result Map(int id,String token){
        String username = JwtUtils.getClaimsByToken(token).getSubject();
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("username", username);
        int uid = userMapper.selectOne(userQueryWrapper).getId();

        QueryWrapper<Device> deviceQueryWrapper=new QueryWrapper<>();
        deviceQueryWrapper.eq("userId",uid);
        if(deviceMapper.selectCount(deviceQueryWrapper)==0) return Result.error();

        QueryWrapper<Message> messageQueryWrapper=new QueryWrapper<>();
        messageQueryWrapper.eq("deviceId",id);
        List<Message> messages=messageMapper.selectList(messageQueryWrapper);
        return Result.ok().data("messages",messages);
    }
}

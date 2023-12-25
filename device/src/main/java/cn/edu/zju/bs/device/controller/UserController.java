package cn.edu.zju.bs.device.controller;

import cn.edu.zju.bs.device.entity.User;
import cn.edu.zju.bs.device.mapper.UserMapper;
import cn.edu.zju.bs.device.utils.JwtUtils;
import cn.edu.zju.bs.device.utils.Result;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin    //使用默认的跨域通信配置
public class UserController {
    @Autowired
    private UserMapper userMapper;//该注解会自动生产注入该mapper类的实现

    @GetMapping("/login")
    Result login(User user){
        String username= user.getUsername();
        String password=user.getPassword();
        QueryWrapper<User> queryOption=new QueryWrapper<>();
        queryOption.eq("username",username);

        if(userMapper.selectCount(queryOption)==1){ //存在该账户
            queryOption.eq("password",password);
            if(userMapper.selectCount(queryOption)==1){ //密码也正确
                String token=JwtUtils.generateToken(username);//生产token
                return Result.ok().data("token",token);
            }
            else{
                return Result.error().message("Wrong password");
            }
        }
        else{
            return Result.error().message("User not found");
        }
    }

    @PostMapping("/signup")
    Result signup(@RequestBody User user){
        QueryWrapper<User> queryOption=new QueryWrapper<>();
        queryOption.eq("email",user.getEmail());
        if(userMapper.selectCount(queryOption)!=0)  //邮箱重复
            return Result.error().message("Email already exists");
        queryOption.clear();
        queryOption.eq("username",user.getUsername());
        if(userMapper.selectCount(queryOption)!=0) //用户名重复
            return Result.error().message("Username repeated");

        userMapper.insert(user);
        String token=JwtUtils.generateToken(user.getUsername());
        return Result.ok().data("token",token);
    }

}

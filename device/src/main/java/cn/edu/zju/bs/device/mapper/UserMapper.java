package cn.edu.zju.bs.device.mapper;


import cn.edu.zju.bs.device.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {


}

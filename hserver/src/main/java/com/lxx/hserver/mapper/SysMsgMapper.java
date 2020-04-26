package com.lxx.hserver.mapper;

import com.lxx.hserver.entity.MsgContent;
import com.lxx.hserver.entity.SysMsg;
import com.lxx.hserver.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysMsgMapper {

    int sendMsg(MsgContent msg);

    List<SysMsg> getSysMsgs(@Param("userId")Integer userId);

    int markRead(@Param("flag") Integer flag, @Param("userId") Integer userId);

    int addMsg2AllUsers(@Param("users") List<User> users, @Param("mid") Integer mid);
}

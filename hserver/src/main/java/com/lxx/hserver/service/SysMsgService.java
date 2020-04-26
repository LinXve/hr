package com.lxx.hserver.service;

import com.lxx.hserver.entity.MsgContent;
import com.lxx.hserver.entity.SysMsg;
import com.lxx.hserver.entity.User;
import com.lxx.hserver.mapper.SysMsgMapper;
import com.lxx.hserver.utils.CurrentUserName;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SysMsgService {
    @Autowired
    private UserService userService;
    @Autowired
    private SysMsgMapper sysMsgMapper;
    @RequiresRoles("admin")
    public boolean snedMsg( String message,String title) {
        MsgContent msgContent = new MsgContent();
        msgContent.setMessage(message);
        msgContent.setCreateDate(new Date());
        msgContent.setTitle(title);
        sysMsgMapper.sendMsg(msgContent);
        List<User> allUsers = userService.getAllUsers();
        int result = sysMsgMapper.addMsg2AllUsers(allUsers, msgContent.getId());
        return result==allUsers.size();
    }

    public List<SysMsg> getSysMsgs() {
        User byName = userService.findByName(CurrentUserName.getUserName());
        return sysMsgMapper.getSysMsgs(byName.getId());
    }

    public boolean markRead(Integer flag) {
        User byName = userService.findByName(CurrentUserName.getUserName());
        if (flag!=-1){
            return sysMsgMapper.markRead(flag,byName.getId())==1;
        }
        sysMsgMapper.markRead(flag,byName.getId());
        return true;
    }
}

package com.lxx.hserver.service;

import com.lxx.hserver.entity.User;
import com.lxx.hserver.mapper.UserMapper;
import com.lxx.hserver.utils.CurrentUserName;
import com.lxx.hserver.utils.PwdUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Slf4j
public class UserService {
    @Autowired
    UserMapper userMapper;

    public User findByName(String username) {
        log.info("username:"+username);
        User byname = userMapper.findByname(username);
        log.info(byname.toString());
        return byname;
    }

    public User select(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Transactional
    public boolean deleteById(Integer id) {
        if (userMapper.deleteById(id) == 1) {
//           中间表不一定有记录所有>=0
            return userMapper.deleteUserRolesByUId(id) >= 0;
        }
        throw new RuntimeException("id不存在");
    }

    //   仅修改个人资料（根据用户名(id)，有选择性的修改），不涉及roles
    @Transactional
    public int update(User user) {
//        加密
        if (!StringUtils.isEmpty(user.getUsername())){
            if (!StringUtils.isEmpty(userMapper.findByname(user.getUsername()))){
                throw new RuntimeException("该用户名已存在，请重新设置！");
            }
        }
//        加密
        if (!StringUtils.isEmpty(user.getPassword())){
            user.setPassword(PwdUtils.getPwd(user.getPassword()));
        }
        return userMapper.updateByPrimaryKeySelective(user);
    }
//    模糊查询
    public List<User> getUserByKeyWords(String keywords){
        return userMapper.getUserByKeyWords(keywords);
    }
//注册
    public int register(String username, String password) {

        if (StringUtils.isEmpty(userMapper.findByname(username))) {
            String pwd = PwdUtils.getPwd(password);
            User user=new User();
            user.setPassword(pwd);
            user.setUsername(username);
            user.setName(username);
            user.setUserfase("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3730772664,138405132&fm=26&gp=0.jpg");
            userMapper.register(user);
            userMapper.insertUserRole(user.getId());
            return 1;
        }else {
            return 0;
        }

    }

    public int updateUserRoles(Integer uid, Integer[] rids) {
        userMapper.deleteUserRolesByUId(uid);
        return userMapper.addUserRoles(uid,rids);
    }

    public List<User> getAllUsers() {
        return userMapper.getAllFriend(null);
    }

    public List<User> getAllFriends()  {
        User byname = userMapper.findByname(CurrentUserName.getUserName());
        return userMapper.getAllFriend(byname.getId());
    }
}

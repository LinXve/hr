package com.lxx.hserver.mapper;

import com.lxx.hserver.entity.User;
import com.lxx.hserver.entity.UserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    long countByExample(UserExample example);

    int deleteByExample(UserExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    List<User> selectByExample(UserExample example);

    User selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") User record, @Param("example") UserExample example);

    int updateByExample(@Param("record") User record, @Param("example") UserExample example);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User findByname(@Param("username") String username);

    int deleteById(Integer id);
    int deleteUserRolesByUId(@Param("uid") int uid);

    List<User> getUserByKeyWords(@Param("keywords") String keywords);

    int register(User user);
    int insertUserRole(Integer uid);

    int addUserRoles(@Param("uid") Integer uid, @Param("rids") Integer[] rids);

    List<User> getAllFriend(@Param("currentId") Integer id);
}
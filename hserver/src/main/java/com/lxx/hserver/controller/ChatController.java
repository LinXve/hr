package com.lxx.hserver.controller;

import com.lxx.hserver.service.SysMsgService;
import com.lxx.hserver.service.UserService;
import com.lxx.hserver.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
public class ChatController {
    @Autowired
    private UserService userService;
    @Autowired
    private SysMsgService sysMsgService;

    @GetMapping("/users")
    public Result getAllUsers(){
        return Result.success(userService.getAllFriends());
    }

    @PostMapping("/nf")
    public Result snedNF(@RequestParam("message")String message,@RequestParam("title")String title ){
        if (sysMsgService.snedMsg(message,title)){
            return Result.success("发送成功！");
        }
        return Result.fail("发送失败！");
    }
    @GetMapping("/sysmsgs")
    public  Result getSysMsgs(){
        return Result.success(sysMsgService.getSysMsgs());
    }

    /**
     *
     * @param flag：消息id，-1时全部标记已读
     * @return
     */
    @PutMapping("/markread")
    public Result markRead(Integer flag){
        if (sysMsgService.markRead(flag)){
            if (flag==-1){
                return Result.success("multiple");
            }else {
                return Result.success("single");
            }
        }else {
            if (flag==-1){
                return Result.fail("multiple");
            }else {
                return Result.fail("single");
            }
        }
    }
}

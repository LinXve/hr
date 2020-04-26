package com.lxx.hserver.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysMsg {
    private Integer id;
    private Integer mid;
    private int type;
    private Integer userId;
    private boolean state;
    private MsgContent msgContent;
}

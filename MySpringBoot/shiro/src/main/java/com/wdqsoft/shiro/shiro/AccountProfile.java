package com.wdqsoft.shiro.shiro;

import lombok.Data;

import java.io.Serializable;
/**
数据实例  可根据自己业务需求修改
*/
@Data
public class AccountProfile implements Serializable {
    private String id;
    private String name;
    private String loginname;
    private String role;
    private int isavailable;

}

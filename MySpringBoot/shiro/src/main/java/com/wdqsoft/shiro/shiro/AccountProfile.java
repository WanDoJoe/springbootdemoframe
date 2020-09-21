package com.wdqsoft.shiro.shiro;

import lombok.Data;

import java.io.Serializable;

@Data
public class AccountProfile implements Serializable {
    private String id;
    private String name;
    private String loginname;
    private String role;
    private int isavailable;

}

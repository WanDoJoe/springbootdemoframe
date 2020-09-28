package com.wdqsoft.common.lang;

/**
 * 错误代码 信息管理
 *
 */
public class ResultCode {
    public static int SUCCESS=0;
    public static int HTTP_REQUEST_ERROR=8022;

    public static int LOGIN_ERROR=0;
    public static int LOGIN_SUCCESS=1;
    public static int LOGIN_USERNAME_PASSWORD_WRONG=90003;

    public static int UNKNOWERROR=99999;
    public static int ShiroException_CODE=90001;
    //Jwt token失效
    public static int token_invalid=90101;
    //Jwt token空
    public static int token_null=90102;
    public static int RuntimeException_CODE=404;
    public static int INTERNAL_SERVER_ERROR=500;
    public static int MethodArgumentNotValidException_CODE=402;
    public static int IllegalAccessException_CODE=403;
}

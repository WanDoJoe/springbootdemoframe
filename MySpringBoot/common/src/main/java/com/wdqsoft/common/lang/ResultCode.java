package com.wdqsoft.common.lang;

/**
 * 错误代码 信息管理
 * rc       rm
 *错误码    含义
 * 0	    正常
 * ————	——————–
 * 90000	user验证失败： 参数错误或user（用户时间）无效
 * 90001	user验证失败：无效token 或者用户被禁用
 * 90003	使用用户名密码登录时，用户名或密码错误（不包括第三方登录和手机验证码登录的情况）
 * 90009	用户账号暂未被验证激活或管理员未通过
 * 90010	用户密码已经修改，需要重新登录
 * 90011	用户账号不安全，可能被盗号
 * ————-	——
 * -8020	url不满足规范
 * -8021	接口未实现
 * -8022	接口实现不满足规范（返回的code，message，data不满足规范）
 * ————-	——————-
 * -8102	请求域名不正确
 * -8103	请求方法不正确
 * -8110	请求参数不满足定义
 * ————-	——————-
 * 99999	未知错误
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

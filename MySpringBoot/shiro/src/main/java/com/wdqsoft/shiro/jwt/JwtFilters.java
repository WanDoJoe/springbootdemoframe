package com.wdqsoft.shiro.jwt;

import com.alibaba.fastjson.JSONObject;
import com.wdqsoft.common.lang.HttpUtils;
import com.wdqsoft.common.lang.Result;
import com.wdqsoft.common.lang.ResultCode;
import com.wdqsoft.common.utils.IpUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * AuthenticatingFilter 提供基本方法类
 *
 * BasicHttpAuthenticationFilter 提供完整的分装好的方法
 *
 * 由于需要记录ip所以将 所有jwt移动到dao层
 *
 */
@Slf4j
@Component
public class JwtFilters extends AuthenticatingFilter {

    @Autowired
    JwtUtils jwtUtils;


    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest request= (HttpServletRequest) servletRequest;
        String jwt=request.getHeader(HttpUtils.Authorization);
//        log.info("获取头部中的秘钥");
        if(StringUtils.isEmpty(jwt)){
            return null;
        }
        log.info(jwt);
        return new JwtToken(jwt);
    }

    @Override
//    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest request= (HttpServletRequest) servletRequest;
        HttpServletResponse servletResponse1= (HttpServletResponse) servletResponse;
        String jwt=request.getHeader(HttpUtils.Authorization);
        log.info("onAccessDenied");
//        sysloghttprequestDao. addLog(IpUtil.getHttpRequestEntity(request)); //记录ip信息
        IpUtil.getIpAddr(request);


//        IpUtil.SomeThing.INSTANCE.getInstance().getHttpRequestInfo(request);
//        TCmsRequest tCmsRequest=new TCmsRequest();
//        TCmsRequestlog tCmsRequestlog=RequestNetUtil.getIpAddrOb(request);
//        //记录所有访问的reques信息
//        cmsRequestLogService.insert(tCmsRequestlog);
//        if (null!=cmsRequestService.selectByIp(tCmsRequestlog.getIp())){
//                if(cmsRequestService.selectByIp(tCmsRequestlog.getIp()).getIsavailable()==0) {
//                    throw new ExpiredCredentialsException("您的IP已被锁定！无法使用！");
//                }
//        }else {
//            //记录ip  存在则不再记录
//                tCmsRequest.setIp(tCmsRequestlog.getIp());
//                tCmsRequest.setIsavailable(1);
//                cmsRequestService.insert(tCmsRequest);
//        }




   //---------------------------------------------------------------------------
        if(StringUtils.isEmpty(jwt)){
            return true;
        }else{
            //校验jwt
            Claims claims=jwtUtils.getClaimByToken(jwt);
            if(claims==null||jwtUtils.isTokenExpired(claims.getExpiration())){
                log.info("Token !!已经失效!!");
                servletResponse1.setStatus(ResultCode.token_invalid);
//                servletResponse1.getWriter().append("Token invalid!!!");
//                throw new ExpiredCredentialsException("Token 已经失效");
                throw new NullPointerException("Token 已经失效");
            }
        }
        boolean islogin=executeLogin(servletRequest,servletResponse);
        if(islogin){
            log.info("shiro！登录！！成功");
        }else{
            log.info("shiro！登录！！失败");
        }
        return islogin;
    }

    //登录失败
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        HttpServletResponse httpServletResponse= (HttpServletResponse) response;
        Throwable throwable =e.getCause()==null?e:e.getCause();
        Result result=Result.fail(ResultCode.ShiroException_CODE,throwable.getMessage());
        String json =JSONObject.toJSONString(result);
        log.info("onLoginFailure");
        try{
            httpServletResponse.getWriter().print(json);
        }catch (Exception e1){

            e1.printStackTrace();
        }
        return false;
    }

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest= WebUtils.toHttp(request);
        HttpServletResponse httpServletResponse=WebUtils.toHttp(response);
        log.info("preHandle");
        httpServletResponse.setHeader(HttpUtils.Access_Control_Allow_Orign,httpServletRequest.getHeader(HttpUtils.Origin));
        httpServletResponse.setHeader(HttpUtils.Access_Control_Allow_Methods,HttpUtils.Allow_Methods);
        httpServletResponse.setHeader(HttpUtils.Access_Control_Allow_Headers,
                httpServletRequest.getHeader(HttpUtils.Access_Control_Allow_Headers));

        if(httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())){
            httpServletResponse.setStatus(HttpStatus.OK.value());
        }

        return super.preHandle(request,response);
    }

}

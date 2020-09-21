package com.wdqsoft.common.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Enumeration;

@Slf4j
public class IpUtil {
//    private static HttpRequestInfoCallBack callBack;
//    HttpRequestInfoCallBack callBack;

    public enum SomeThing {
        INSTANCE;
        private IpUtil instance;
        SomeThing() {
            instance = new IpUtil();
        }
        public IpUtil getInstance() {
            return instance;
        }
    }

    private static final String UNKNOWN = "unknown";
    private static final String LOCALHOST = "127.0.0.1";
    private static final String SEPARATOR = ",";

//
//    public static Sysloghttprequest getHttpRequestEntity(HttpServletRequest request) {
////        HttpRequestEntity httpRequestEntity=new HttpRequestEntity();
//        Sysloghttprequest sysloghttprequest=new Sysloghttprequest();
//        JSONObject httpRequestJsonOb=getIpAddr(request);
//
//        sysloghttprequest.setRequesturi( httpRequestJsonOb.getString("requestURI"));
//        sysloghttprequest.setMethod( httpRequestJsonOb.getString("method"));
//        sysloghttprequest.setContextpath(  httpRequestJsonOb.getString("contextPath"));
//        sysloghttprequest.setServletpath( httpRequestJsonOb.getString("servletPath"));
//        sysloghttprequest.setAuthtype(httpRequestJsonOb.getString("authType"));
//        sysloghttprequest.setQuerystring(httpRequestJsonOb.getString("queryString"));
//        sysloghttprequest.setPathtranslated(httpRequestJsonOb.getString("pathTranslated"));
//        sysloghttprequest.setRequestedsessionid(httpRequestJsonOb.getString("requesteSessionId"));
//        sysloghttprequest.setPathinfo(httpRequestJsonOb.getString("pathInfo"));
//        sysloghttprequest.setIpaddress(httpRequestJsonOb.getString("ipAddress"));
//        sysloghttprequest.setHeaders(httpRequestJsonOb.getString("headers"));
//
//        return sysloghttprequest;
//    }

    public static JSONObject getIpAddr(HttpServletRequest request) {
//        System.out.println(request);
        String ipAddress;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if (LOCALHOST.equals(ipAddress)) {
                    InetAddress inet = null;
                    try {
                        inet = InetAddress.getLocalHost();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                    ipAddress = inet.getHostAddress();
                }
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            // "***.***.***.***".length()
            if (ipAddress != null && ipAddress.length() > 15) {
                if (ipAddress.indexOf(SEPARATOR) > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            ipAddress = "";
        }

//        System.out.print(request.getHeaderNames());
        StringBuffer buffer=new StringBuffer();
        Enumeration<?> enum1 = request.getHeaderNames();
        while (enum1.hasMoreElements()) {
            String key = (String) enum1.nextElement();
            String value = request.getHeader(key);
//            System.out.println(key + "\t" + value);
//            log.info(key+"：求heander值："+value);
            buffer.append(key+":"+value+";");
        }
        JSONObject httpRequestJsonOb=new JSONObject();
        httpRequestJsonOb.put("requestURI",request.getRequestURI());
        httpRequestJsonOb.put("method",request.getMethod());
        httpRequestJsonOb.put("contextPath",request.getContextPath());
        httpRequestJsonOb.put("servletPath",request.getServletPath());
        httpRequestJsonOb.put("authType",request.getAuthType());
        httpRequestJsonOb.put("queryString",request.getQueryString());
        httpRequestJsonOb.put("pathTranslated",request.getPathTranslated());
        httpRequestJsonOb.put("requesteSessionId",request.getRequestedSessionId());
        httpRequestJsonOb.put("pathInfo",request.getPathInfo());
        httpRequestJsonOb.put("ipAddress",ipAddress);
        httpRequestJsonOb.put("headers",buffer.toString());
        log.info(httpRequestJsonOb.toJSONString());
        return httpRequestJsonOb;
    }

}

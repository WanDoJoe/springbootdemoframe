package com.wdqsoft.shiro.config;

import com.wdqsoft.shiro.jwt.JwtFilters;
import com.wdqsoft.shiro.shiro.AccountRealm;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.mgt.SessionsSecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 配置 shiro 及核心过滤器
 */
@Slf4j
@Configuration
public class ShiroConfig {

//    @Autowired
//    JwtFilters jwtFilters;
    @Autowired
    JwtFilters jwtFilters;

//    @Bean
//    public SecurityManager getSecurityManager() {
//        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
//        SecurityUtils.setSecurityManager(securityManager);
//        return securityManager;
//    }

    @Bean
    public SessionManager sessionManager(RedisSessionDAO redisSessionDAO){
        log.info("shiro工作流程---sessionManager");
        DefaultWebSessionManager sessionManager=new DefaultWebSessionManager();
        sessionManager.setSessionDAO(redisSessionDAO);
        return sessionManager;
    }


    /**
     * 安全管理器
     * @return
     */
    @Bean
    public SessionsSecurityManager securityManager(
            AccountRealm accountRealm, SessionManager sessionManager, RedisCacheManager redisCacheManager){
        log.info("shiro工作流程---securityManager");
        DefaultWebSecurityManager securityManager=new DefaultWebSecurityManager();
        securityManager.setSessionManager(sessionManager);
        securityManager.setCacheManager(redisCacheManager);
        securityManager.setRealm(accountRealm);

        SecurityUtils.setSecurityManager(securityManager);

        return securityManager;
    }
    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition(){
        log.info("shiro工作流程---shiroFilterChainDefinition");
        DefaultShiroFilterChainDefinition chainDefinition=new DefaultShiroFilterChainDefinition();
        Map<String,String> filterMap=new LinkedHashMap<>();
        filterMap.put("/**","jwt");
        chainDefinition.addPathDefinitions(filterMap);
        return chainDefinition;
    }
    @Bean("shiroFilterFactoryBean")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(
            SecurityManager securityManager,ShiroFilterChainDefinition shiroFilterChainDefinition){
        log.info("shiro工作流程---shiroFilterFactoryBean");
        ShiroFilterFactoryBean shiroFilter=new ShiroFilterFactoryBean();

        shiroFilter.setSecurityManager(securityManager);

        //配置拦截规则
        Map<String,Filter> filters=new HashMap<>();
//        filters.put("/login","anon");//配置登录请求不需要认证
//        filters.put("/logout","logout");//配置退出请求，同时会清空该用户内存
//        filters.put("/admin/**","authc"); //配置一个admin开头的都需要认证
//        filters.put("/user/**","authc"); //配置一个user开头的都需要认证

        filters.put("jwt",jwtFilters);
//        filters.put("/**",requestFilter);
//        filters.put("/**","authc"); //配置一个剩余所有请求都需要登录，（注意需要卸载最后，可选配置）



        shiroFilter.setFilters(filters);
        Map<String,String> filtersMap=shiroFilterChainDefinition.getFilterChainMap();
        shiroFilter.setFilterChainDefinitionMap(filtersMap);

        return shiroFilter;
    }


    @Bean
    public static DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator(){
        log.info("shiro工作流程---getDefaultAdvisorAutoProxyCreator");
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator=new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setUsePrefix(true);
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    /**
//     * 开启shiro注解支持
//     开启需要借助spring的AOP来实现
//     @RequiresRoles()
//
//     @RequiresPermissions()
//
//     * @return
//     */
//    @Bean
//    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator(){
//        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator=new DefaultAdvisorAutoProxyCreator();
//        advisorAutoProxyCreator.setProxyTargetClass(true);
//        return advisorAutoProxyCreator;
//    }
//
//    /**
//     * 开启AOP支持
//     * @param securityManager
//     * @return
//     */
//    @Bean
//    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SessionsSecurityManager securityManager){
//        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor =new AuthorizationAttributeSourceAdvisor();
//        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
//        return authorizationAttributeSourceAdvisor;
//    }
}

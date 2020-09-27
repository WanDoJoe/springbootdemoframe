package com.wdqsoft.shiro.shiro;

import com.alibaba.fastjson.JSONObject;
import com.wdqsoft.shiro.jwt.JwtToken;
import com.wdqsoft.shiro.jwt.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * 系统认证和授权，充当shiro与应用数据连接器
 */
@Slf4j
@Component
public class AccountRealm extends AuthorizingRealm {

    @Autowired
    JwtUtils jwtUtils;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

//    @Autowired
//    CmsUserService cmsUserService;
//    @Autowired
//    ShiroAccountUserServices shiroAccountUserServices;

    //    @Autowired
//    CMSUserService cmsUserService;
//    @Autowired
//    CmsSysUserMapper cmsUserMapper;
//    @Autowired
//    CmsSysRoleMapper cmsRoleMapper;


    /**
     * 用户授权方法，当用户认证通过，每次访问需要授权的请求时，都需要执行此代码来授权
     * 这是查询数据库或者缓存来获取用户当前角色和权限，并设置到shiro中
     *
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        //1、获取登录时输入的用户名
        AccountProfile accountProfile=(AccountProfile) principalCollection.fromRealm(getName()).iterator().next();
        log.info("获取登录时输入的用户名  ");
        log.info(accountProfile.getRole());
        Set<String> roleSet=new HashSet<>();
        roleSet.add(accountProfile.getRole());
        //2、到数据库查是否有此对象
        if(accountProfile!=null){
            //2.1、权限信息对象info,用来存放查出的用户的所有的角色（role）及权限（permission）
            SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
            //2.2、用户的角色集合
            info.addRoles(roleSet);
            //2.3、用户的角色对应的所有权限，如果只使用角色定义访问权限，下面的四行可以不要
//            List<Role> roleList=user.getRoleList();
//            for (Role role : roleList) {
//                info.addStringPermissions(role.getPermissionsName());
//            }
            return info;
        }


        return null;
    }

    /**
     *
     * @param token 用户身份 存放用户账号和密码
     * @return 认证
     * @throws AuthenticationException  返回认证错误失败信息
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        JwtToken jwtToken= (JwtToken) token;

        System.out.println("---start----jwt token -----start-----");
        System.out.println("-------认证开始 ----------");

        String userid=jwtUtils.getClaimByToken((String) jwtToken.getPrincipal()).getSubject();
        log.info(jwtUtils.getClaimByToken((String) jwtToken.getPrincipal()).getSubject());
//        JSONObject object= (JSONObject) treeOAHttpRequestDao.getUserinfoList(userid,"shiro认证").getData();
//        if (object==null){
//            throw new UnknownAccountException("登录异常！！！");
//        }
       //根据token中信息 判断 数据库中是否存在此对象
//        TCmsManageuser tCmsManageuser=cmsUserService.selectById(Integer.valueOf(userid));
//        CmsSysUser profile= cmsUserMapper.selectByPrimaryKey(Integer.valueOf(userid));
//        Result result= cmsUserService.selectByIdFromOne(Integer.valueOf(userid));
//        CmsUser user=new CmsUser();
//        if(result.getRc()==0){
//            if(result.getData()==null){
//                throw new UnknownAccountException("账户不存在");
//            }
//             user= (CmsUser) result.getData();
//            if(user.getStutas()==0){
//                throw new LockedAccountException("账户被锁定");
//            }
//        }else{
//            throw new UnknownAccountException("您没有权限!");
//        }
//        if(profile==null){
//            throw new UnknownAccountException("账户不存在");
//        }
//        if(profile.getStatus()==1){
//            throw new LockedAccountException("账户被锁定");
//        }
//        AccountProfile profile=new AccountProfile();
//        profile.setId(String.valueOf(user.getId()));
//        profile.setLoginname(user.getLoginname());
//        profile.setName(user.getName());
//        profile.setRole(String.valueOf(user.getRoleid()));
//        profile.setIsavailable(user.getStutas());

//        user.setOid(userid);
//        BeanUtils.copyProperties(tCmsManageuser,profile);
// start   demo示例  如不使用 请注释掉
        AccountProfile profile=new AccountProfile();
        profile.setId(userid);
        profile.setLoginname("admin");
        profile.setIsavailable(1);
// end   demo示例  如不使用 请注释掉   
        
//        System.out.println("-------"+profile.getId()+" -----"+profile.getRoleid()+"-----");
        System.out.println("-------认证结束 ----------");
        System.out.println("----end---jwt token ------end----");

        return new SimpleAuthenticationInfo(profile,jwtToken.getCredentials(),getName());
    }
}

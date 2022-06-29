package com.example.shirodemo.shiro;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

public class UserRealm  extends AuthorizingRealm {
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("执行授权逻辑");
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("执行认证逻辑");

        String dbusername = "vae";
        String dbpassword = "123456";

        //编写shiro判断逻辑，判断用户名和密码
        //1. 判断用户名
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        if (!token.getUsername().equals(dbusername)) {
            //用户名不存在shiro底层会抛出UnknownAccountException
            return null;
        }
        //2. 判断密码,参数1：需要返回给login方法的数据；参数2：数据库密码，shiro会自动判断
        return new SimpleAuthenticationInfo("", dbpassword, "");
    }

}

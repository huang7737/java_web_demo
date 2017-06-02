package com.sinosafe.login.shiro;

import java.util.HashSet;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SimpleRealm extends AuthorizingRealm {

	// 这里因为没有调用后台，直接默认只有一个用户
	@Value("${app.userName}")
	private String userName;
	@Value("${app.password}")
	private String password;

	/*
	 * 授权
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		Set<String> roleNames = new HashSet<String>();
		Set<String> permissions = new HashSet<String>();
		//角色权限可以改成从数据库读取
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roleNames);
		info.setStringPermissions(permissions);
		return info;
	}

	/*
	 * 登录验证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken)
			throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		if (token.getUsername().equals(userName)) {
			//用户名密码可以改成从数据库读取
			return new SimpleAuthenticationInfo(userName, password, getName());
		} else {
			throw new AuthenticationException();
		}
	}

}

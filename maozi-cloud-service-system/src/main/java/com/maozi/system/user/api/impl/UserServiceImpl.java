
/*
 * Copyright 2012-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package com.maozi.system.user.api.impl;

import com.github.yulichang.toolkit.MPJWrappers;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maozi.base.AbstractBaseDtomain;
import com.maozi.base.api.impl.BaseServiceImpl;
import com.maozi.base.error.code.SystemErrorCode;
import com.maozi.oauth.client.api.rpc.v1.RpcClientServiceV1;
import com.maozi.oauth.token.api.rpc.v1.RpcOauthTokenServiceV1;
import com.maozi.oauth.token.dto.platform.param.ClientUserParam;
import com.maozi.system.permission.api.PermissionService;
import com.maozi.system.permission.api.RolePermissionService;
import com.maozi.system.permission.api.UserRoleService;
import com.maozi.system.user.api.UserService;
import com.maozi.system.user.domain.UserDo;
import com.maozi.system.user.dto.v1.platform.UserSaveUpdateParam;
import com.maozi.system.user.mapper.UserMapper;
import java.util.List;
import javax.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends BaseServiceImpl<UserMapper,UserDo,AbstractBaseDtomain, SystemErrorCode> implements UserService {
	
	@Value("${security.oauth2.client.client-id}")
	protected String clientId;
	
	@Value("${security.oauth2.client.client-secret}")
	protected String clientSecret;
	
	@Override
	protected String getAbbreviationModelName() {return "【用户】";}
	
	protected PasswordEncoder passwordEncoder = new MessageDigestPasswordEncoder("MD5");
	
	
	@Resource(name = "userRoleServiceImpl")
	protected UserRoleService userRoleService;
	
	@Resource(name = "rolePermissionServiceImpl")
	protected RolePermissionService rolePermissionService;
	
	@Resource(name = "permissionServiceImpl")
	protected PermissionService permissionService;
	
	@DubboReference
	protected RpcOauthTokenServiceV1 rpcOauthTokenServiceV1;
	
	@DubboReference
	protected RpcClientServiceV1 rpcClientServiceV1;

	@Override
	public <D> D getByUsername(String username,Class<D> clazz,String ... columns) {
		
		isNullThrowError(username, getAbbreviationModelName());
		
		MPJLambdaWrapper<UserDo> wrapper = MPJWrappers.lambdaJoin(UserDo.builder().username(username).build());
		
		wrapper.select(columns);
		
		return getByParamThrowErrorRelation(wrapper,clazz);
		
	}
	
	protected Long getAvailableByUsername(String username) {
		
		isNullThrowError(username, getAbbreviationModelName());
		
		MPJLambdaWrapper<UserDo> wrapper = MPJWrappers.lambdaJoin(UserDo.builder().username(username).build());
		
		wrapper.select(UserDo::getId);
		
		UserDo domain = getAvailableByParam(wrapper);
		
		return domain.getId();
		
	}
	
	protected Long restSaveUpdate(Long id, UserSaveUpdateParam param) {
		
		if(isNotNull(id)) {
			
			param.setUsername(null);
			
			param.setClientId(null);
			
		}else {
			
			if(isNotNull(param.getClientId()) && isNotNull(param.getUsername())) {
				
				rpcClientServiceV1.checkAvailableResult(param.getClientId()).getResultDataThrowError();
				
				checkNotHas(MPJWrappers.lambdaJoin(UserDo.builder().clientId(param.getClientId()).username(param.getUsername()).build()));
				
			}
		
		}
		
		if(isNotNull(param.getPassword())) {
			param.setPassword("{MD5}"+passwordEncoder.encode(param.getPassword()));
		}
		
		id = saveUpdate(id,param);
		
		userRoleService.updateBind(id, param.getBindRoleIds(), param.getUnbindRoleIds());
		
		return id;
		
	}

	@Override
	public ClientUserParam getClientUser(Long id) {
		return getByIdThrowError(id,ClientUserParam.class,UserDo::getUsername,UserDo::getClientId);
	}

	@Override
	public List<ClientUserParam> getClientUsers(List<Long> ids) {
		
		collectionIsEmptyThrowError(ids, getAbbreviationModelName()+"列表");
		
		MPJLambdaWrapper<UserDo> wrapper = MPJWrappers.lambdaJoin();
		
		wrapper.select(UserDo::getUsername,UserDo::getClientId);
		
		wrapper.in(UserDo::getId, ids);
		
		return list(wrapper,ClientUserParam::new);
		
	}

	@Override
	public void unbind(Long id) {
		userRoleService.userUnbind(id);
	}
	
}

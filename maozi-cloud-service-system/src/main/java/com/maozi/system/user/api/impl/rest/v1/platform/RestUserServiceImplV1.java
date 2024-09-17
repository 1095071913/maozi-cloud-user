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

package com.maozi.system.user.api.impl.rest.v1.platform;

import com.maozi.base.annotation.RestService;
import com.maozi.base.enums.Status;
import com.maozi.base.param.PageParam;
import com.maozi.base.result.PageResult;
import com.maozi.common.result.AbstractBaseResult;
import com.maozi.oauth.token.dto.platform.dto.OauthToken;
import com.maozi.oauth.token.dto.platform.param.ClientParam;
import com.maozi.oauth.token.dto.platform.param.ClientUserParam;
import com.maozi.oauth.token.dto.platform.param.TokenInfoParam;
import com.maozi.system.user.api.impl.UserServiceImpl;
import com.maozi.system.user.api.rest.v1.platform.RestUserServiceV1;
import com.maozi.system.user.domain.UserDo;
import com.maozi.system.user.dto.v1.platform.UserAccountParam;
import com.maozi.system.user.dto.v1.platform.UserListParam;
import com.maozi.system.user.dto.v1.platform.UserSaveUpdateParam;
import com.maozi.system.user.vo.v1.platform.UserIndividualInfoVo;
import com.maozi.system.user.vo.v1.platform.UserInfoVo;
import com.maozi.system.user.vo.v1.platform.UserListVo;

@RestService
public class RestUserServiceImplV1 extends UserServiceImpl implements RestUserServiceV1 {

	@Override
	public AbstractBaseResult<OauthToken> restGetToken(UserAccountParam param) throws Exception {
		
		TokenInfoParam tokenParam = TokenInfoParam.builder().clientId(clientId).clientSecret(clientSecret).username(param.getUsername()).password(param.getPassword()).build();
	    
	    return success(rpcOauthTokenServiceV1.rpcGet(tokenParam).getResultDataThrowError());
	    
	}

	@Override
	public AbstractBaseResult<PageResult<UserListVo>> restList(PageParam<UserListParam> pageParam) {
		return success(listRelation(pageParam, UserListVo.class));
	}
	
	@Override
	public AbstractBaseResult<Long> restSave(UserSaveUpdateParam param) {
		return success(restSaveUpdate(null,param));
	}

	@RestService
	public class RestUserServiceUserImplV1 extends UserServiceImpl implements RestUserServiceCurrentV1{

		@Override
		public AbstractBaseResult<Void> restRemove(Long id) {
			
			UserDo user = getById(id, UserDo::getUsername,UserDo::getClientId);
			
			rpcOauthTokenServiceV1.rpcDestroy(new ClientUserParam(user.getClientId(),user.getUsername())).getResultDataThrowError();
			
			return removeByIdResult(id);
			
		}
		
		@Override
		public AbstractBaseResult<UserInfoVo> restGet(Long id) {
			return success(getByIdThrowErrorRelation(id, UserInfoVo.class));
		}
		
		@Override
		public AbstractBaseResult<Void> restUpdate(Long id, UserSaveUpdateParam param) {
			
			restSaveUpdate(id,param);
			
			UserDo user = getById(id, UserDo::getUsername,UserDo::getClientId);
			
			rpcOauthTokenServiceV1.rpcDestroy(new ClientUserParam(user.getClientId(),user.getUsername())).getResultDataThrowError();
			
			return success(null);
			
		}
		
		@Override
		public AbstractBaseResult<Void> restUpdateStatus(Long id){
			
			UserDo user = getByIdThrowError(id,UserDo::getUsername,UserDo::getClientId,UserDo::getStatus);
			
			updateById(UserDo.builder().id(id).status(user.getStatus() == Status.enable ? Status.disable : Status.enable).build());
			
			if(user.getStatus() == Status.enable) {
				rpcOauthTokenServiceV1.rpcDestroy(new ClientUserParam(user.getClientId(),user.getUsername())).getResultDataThrowError();
			}
			
			return updateStatus(id);
			
		}
		
	}
	
	@RestService
	public class RestUserServiceIndividualImplV1 extends UserServiceImpl implements RestUserServiceIndividualV1{
		
		@Override
		public AbstractBaseResult<UserIndividualInfoVo> restIndividualGet() {
			return success(getByUsername(getCurrentUserName(),
				UserIndividualInfoVo.class,getColumns(UserDo::getName,UserDo::getIcon)));
		}

		@Override
		public AbstractBaseResult<OauthToken> restIndividualRefreshToken() throws Exception {
			
			ClientParam tokenParam = ClientParam.builder().clientId(clientId).clientSecret(clientSecret).build();
			
			return rpcOauthTokenServiceV1.rpcRefresh(getRequest().getHeader("Authorization").replace("Bearer ", ""),tokenParam);
			
		}

		@Override
		public AbstractBaseResult<Void> restIndividualDestroyToken() {
			
			rpcOauthTokenServiceV1.rpcDestroy(getRequest().getHeader("Authorization").replace("Bearer ", "")).getResultDataThrowError();
			
			return success(null);
			
		}
		
	}

}
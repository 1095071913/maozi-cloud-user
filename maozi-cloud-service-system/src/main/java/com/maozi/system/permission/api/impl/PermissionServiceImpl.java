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

package com.maozi.system.permission.api.impl;

import com.github.yulichang.toolkit.MPJWrappers;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maozi.base.AbstractBaseDtomain;
import com.maozi.base.api.impl.BaseServiceImpl;
import com.maozi.base.error.code.SystemErrorCode;
import com.maozi.system.permission.api.PermissionService;
import com.maozi.system.permission.api.RolePermissionService;
import com.maozi.system.permission.domain.PermissionDo;
import com.maozi.system.permission.mapper.PermissionMapper;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;



@Service
public class PermissionServiceImpl extends BaseServiceImpl<PermissionMapper,PermissionDo,AbstractBaseDtomain, SystemErrorCode> implements PermissionService {
	
	@Resource(name = "rolePermissionServiceImpl")
	private RolePermissionService rolePermissionService;

	@Override
	protected String getAbbreviationModelName() {return "【权限】";}

	@Override
	protected void checkBind(Long id) {
		
		rolePermissionService.checkRoleBindPermissionByPermission(id);
		
		checkBoolThrowError(count(MPJWrappers.lambdaJoin(PermissionDo.builder().parentId(id).build())) < 1, getAbbreviationModelName()+"已有权限绑定");
		
	}

	@Override
	public boolean has(Long id) {
		return count(MPJWrappers.lambdaJoin(PermissionDo.builder().id(id).build())) > 0;
	}

	@Override
	public List<String> getMarks(Collection<Long> ids) {
		
		MPJLambdaWrapper<PermissionDo> wrapper = MPJWrappers.lambdaJoin();
		
		wrapper.select(PermissionDo::getMark);
		
		wrapper.in(PermissionDo::getId,ids);
		
		return list(wrapper).stream().map(PermissionDo::getMark).collect(Collectors.toList());
		
	}

}
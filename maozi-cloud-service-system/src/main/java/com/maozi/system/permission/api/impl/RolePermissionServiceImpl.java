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

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.yulichang.toolkit.MPJWrappers;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maozi.base.AbstractBaseDtomain;
import com.maozi.base.api.impl.BaseServiceImpl;
import com.maozi.base.error.code.SystemErrorCode;
import com.maozi.system.permission.api.PermissionService;
import com.maozi.system.permission.api.RolePermissionService;
import com.maozi.system.permission.domain.RolePermissionDo;
import com.maozi.system.permission.mapper.RolePermissionMapper;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class RolePermissionServiceImpl extends BaseServiceImpl<RolePermissionMapper,RolePermissionDo,AbstractBaseDtomain, SystemErrorCode> implements RolePermissionService {
	
	@Resource(name = "permissionServiceImpl")
	private PermissionService permissionService;
	
	@Override
	protected String getAbbreviationModelName() {return "【角色权限关系】";}

	@Override
	public void checkRoleBindPermissionByPermission(Long permissionId) {
		
		MPJLambdaWrapper<RolePermissionDo> wrapper = MPJWrappers.lambdaJoin(RolePermissionDo.builder().permissionId(permissionId).build());
		
		checkBoolThrowError(count(wrapper) < 1 , "【权限】已被角色绑定");
		
	}

	@Override
	public void updateBind(Long roleId, List<Long> bindPermissionIds, List<Long> unbindPermissionIds) {
		
		if(collectionIsNotEmpty(bindPermissionIds)) {

			Consumer<Long> consumer = wrapConsumer((permissionId)->{

				RolePermissionDo domainSave = RolePermissionDo.builder().roleId(roleId).permissionId(permissionId).build();

				if(count(MPJWrappers.lambdaJoin(domainSave)) < 1 && permissionService.has(permissionId)) {
					save(domainSave);
				}

			});
			
			bindPermissionIds.parallelStream().forEach(consumer);
			
		}
		
		if(collectionIsNotEmpty(unbindPermissionIds)) {

			Consumer<Long> consumer = wrapConsumer((permissionId)->{
				
				remove(MPJWrappers.lambdaJoin(RolePermissionDo.builder().roleId(roleId).permissionId(permissionId).build()));
				
			});

			unbindPermissionIds.parallelStream().forEach(consumer);
			
		}
		
	}

	@Override
	public Collection<Long> getPermissionsByRoles(List<Long> roleIds) {
		
		MPJLambdaWrapper<RolePermissionDo> wrapper = MPJWrappers.lambdaJoin();
		
		wrapper.select(RolePermissionDo::getPermissionId);
		
		wrapper.in(RolePermissionDo::getRoleId, roleIds);
		
		return list(wrapper).stream().map(RolePermissionDo::getPermissionId).collect(Collectors.toSet());
		
	}

	@Override
	public List<Long> getPermissionsByRole(Long roleId) {
		
		MPJLambdaWrapper<RolePermissionDo> wrapper = MPJWrappers.lambdaJoin();
		
		wrapper.select(RolePermissionDo::getPermissionId);
		
		wrapper.eq(RolePermissionDo::getRoleId, roleId);
		
		return list(wrapper).stream().map(RolePermissionDo::getPermissionId).collect(Collectors.toList());
		
	}

	@Override
	public void roleUnbind(Long roleId) {
		remove(Wrappers.lambdaQuery(RolePermissionDo.builder().roleId(roleId).build()));
	}

}